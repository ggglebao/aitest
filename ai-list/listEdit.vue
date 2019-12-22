<template>
 <div class="el_list_wrapper">
  <comHeader :show-header="false" />
  <div class="el_add_wrapper" style="z-index:2025">
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" class="demo-ruleForm" label-width="80px">
      <el-form-item label="垃圾名称" prop="name">
        <el-input v-model="ruleForm.name"></el-input>
      </el-form-item>
      <el-form-item label="垃圾说明" prop="explain">
        <el-input v-model="ruleForm.explain"></el-input>
      </el-form-item>
      <el-form-item label="垃圾分类" prop="type">
      <template>
          <el-row>
          <el-radio v-model="ruleForm.type" label='0'>可回收物</el-radio>
          </el-row>
          <el-row>
          <el-radio v-model="ruleForm.type" label='1'>有害垃圾</el-radio>
          </el-row>
          <el-row>
          <el-radio v-model="ruleForm.type" label='2'>湿垃圾</el-radio>
          </el-row>
          <el-row>
          <el-radio v-model="ruleForm.type" label='3'>干垃圾</el-radio>
          </el-row>
          <el-row>
          <el-radio v-model="ruleForm.type" label='4' >其他垃圾</el-radio>
          </el-row>
      </template>
      </el-form-item>
      <el-form-item label="小提示">
        <el-input type="textarea" v-model="ruleForm.tip"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="commit">确认</el-button>
        <el-button>
          <router-link to="/aiList">取消</router-link>
        </el-button>
      </el-form-item>
    </el-form>
  </div>
 </div>
</template>
<script>
// import { post } from '../../utils/request.js'
import { Message } from 'element-ui'
import { get } from '../../utils/request.js'
import comHeader from '../../components/ComHeader/index.vue'
export default {
  name: 'AiListEdit',
  components: { comHeader },
  data() {
    return {
      ruleForm: {
        name: '',
        explain: '',
        tip: '',
        type: '',
        id: ''
      },
      rules: {
        name: [
          { required: true, message: '请输入垃圾名称', trigger: 'blur' }
        ]
      }
    }
  },
  created() {
    this.getCategory()
    this.ruleForm.name = this.$route.query.name
    this.ruleForm.explain = this.$route.query.explain
    this.ruleForm.tip = this.$route.query.tip
    this.ruleForm.type = this.$route.query.type
    this.index = this.$route.query.index
  },
  methods: {
    getCategory() {
      get('/rubbish/all').then(res => {
        if (res) {
          this.tableData = res
        }
      }).catch(err => {
        Message({
          message: err,
          duration: 3000,
          type: 'error'
        })
      })
    },
    commit() {
      if (!this.ruleForm.name) {
        Message({
          message: '请输入垃圾名称',
          duration: 3000,
          type: 'error'
        })
        return false
      } else {
        if (!this.$route.query.name) {
          // console.log(this.ruleForm)
          this.ruleForm.id = this.tableData.length + 1
          this.tableData.push(this.ruleForm)
          // console.log(this.tableData)
          this.$router.push({ path: '/aiList', query: { tableData: this.tableData }})
        } else {
          this.tableData[this.index].name = this.ruleForm.name
          this.tableData[this.index].explain = this.ruleForm.explain
          this.tableData[this.index].type = this.ruleForm.type
          this.tableData[this.index].tip = this.ruleForm.tip
          this.$router.push({ path: '/aiList', query: { tableData: this.tableData }})
        }
      }
    }
  }
}
</script>
<style lang="scss">
.el_list_wrapper{
    margin: 0 auto;
    width: 750px;
    height: 200px;
    position: relative;
  .el_add_wrapper {
    padding:20px 20px 20px 20px;
    padding-top: 4.8rem;
  }
}
</style>
