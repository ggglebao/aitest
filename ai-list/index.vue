<template>
 <div class="add-container" height="120rem">
  <comHeader :show-header="false" />
  <div>
    <div class="add-wrap">
      <el-button class="add-button" plain>
        <router-link to="/aiListEdit">添加</router-link>
      </el-button>
    </div>
    <div class="back-wrap">
      <el-button class="add-button" plain>
        <router-link to="/aiSearch">返回</router-link>
      </el-button>
    </div>
    <div class="text-wrap">
      垃圾分类列表
    </div> 
    <div class="list-block" height="80rem">
      <el-table :data="tableData" style="width: 100%" height="80rem">
       <el-table-column prop="id" label="ID" width="60px">
       </el-table-column>
       <el-table-column prop="name" label="名称" width="100px">
       </el-table-column>
       <el-table-column prop="type" :formatter='showTypeName' label="垃圾分类" width="100px">
       </el-table-column>
       <el-table-column prop="tip" label="小提示" width="300px">
       </el-table-column>
       <el-table-column  fixed="right" label="操作" width="60rem">
         <template slot-scope="scope">
           <el-row>
           <el-button type="text" class="btn" size="small"  @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
           </el-row>
           <el-row>
           <el-button type="text" class="btn" size="small"  @click.native.prevent="open(scope.$index, tableData)">删除</el-button>
           </el-row>
         </template>
      </el-table-column>
    </el-table>
    </div>
  </div>
  </div>
</template>

<script>
import { get } from '../../utils/request.js'
// import { post } from '../../utils/request.js'
import { Message } from 'element-ui'
import comHeader from '../../components/ComHeader/index.vue'
export default {
  name: 'AiList',
  components: { comHeader },
  data() {
    return {
      tableData: []
    }
  },
  watch: {
  },
  created() {
    if(!this.$route.query.tableData) {
      this.getCategory()
    }else
      this.tableData = this.$route.query.tableData
  },
  mounted() {},
  destroyed() {},
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
    handleEdit(index, row) {
      this.$router.push({ path: '/aiListEdit', query: { index: index ,name: row.name ,tip: row.tip ,type: row.type+'' ,explain: row.explain }})
    },
    showTypeName(row, column) {
      switch (parseInt(row[column.property])) {
        case 0:
          return '可回收物'
        case 1:
          return '有害垃圾'
        case 2:
          return '湿垃圾'
        case 3:
          return '干垃圾'
        default:
          return '无法分类'
      }
    },
    open(index, rows) {
      this.$confirm('确认删除此条记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(({ value }) => {
        rows.splice(index, 1),
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
        this.$router.push({ path: '/aiList', query: { tableData: this.tableData }})
      })
    }
  }
}
</script>

<style lang="scss">
  .add-container {
    margin: 0 auto;
    width: 750px;
    height: 200px;
    position: relative;
  .add-wrap{
    padding-left:2.5rem;
    padding-top:3.5rem;
    position: center;
    float: left;
    width: 20%;
    height: 10%;
  }
  .add-button{
    padding:4px 5px 4px 5px;
    background-color:#1890ff;
    text-align: center;
    font-size: 15px;
    color: #fff;
  }
  .back-wrap{
    padding-left:1.5rem;
    padding-top:3.5rem;
    position: center;
    float: right;
    width: 20%;
    height: 10%;
  }
  .text-wrap{
    padding-left:15.3rem;
    padding-top:3.5rem;
    font-size: 30px;
  }
  .list-block{
    padding-left:3.3rem;
    padding-right:2.3rem;
    padding-top:0.4rem;
    width:100%;
    font-size:15px;
    float: center;
    height: 30%;
  }
}
</style>

