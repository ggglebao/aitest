<template>
  <div class="search-container">
    <comHeader :show-header="false" />
    <div class="search-wrap">
      <div ref="inputWrap" class="header-search-select">
        <input v-model="trashName" placeholder="在此输入垃圾名称">
        <svg-icon class-name="search-icon" icon-class="search" @click="search" />
      </div>
    </div>
    <div class="kind-block">
      <div v-for="(item,index) in categoryList" :key="index" class="kind-item">
        <router-link class="item-link" :to="{path: '/rubbishType', query: {type: item.id}}">
          <p class="cn-des">{{ item.description }}</p>
          <p class="cn-title">{{ item.name }}</p>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script>
import { get } from '../../utils/request.js'
import { Message } from 'element-ui'
import comHeader from '../../components/ComHeader/index.vue'
export default {
  name: "AiSearch",
  components: { comHeader },
  data() {
    return {
      activeName: "first",
      trashName: '',
      categoryList: [{ "name": "", "id": "", "description": "" }]
    }
  },
  watch: {
  },
  created() {
    this.getCategory()
  },
  mounted() {},
  destroyed() {},
  methods: {
    search() {
      if (!this.trashName) {
        Message({
          message: '请输入垃圾名称',
          duration: 3000,
          type: 'error'
        })
        return false
      }
      this.$router.push({ path: '/rubbishDes', query: { name: this.trashName }})
    },
    getCategory() {
      get('/category').then(res => {
        if (res) {
          this.categoryList = res
        }
      }).catch(err => {
        Message({
            message: err,
            duration: 3000,
            type: 'error'
          })
      })
    }
  }
}
</script>

<style lang="scss">
.el-message{
  min-width: 95%;
  padding: 10px 10px 10px 20px;
}
  .search-container {
  // font-size: 0 !important;
  .search-wrap{
     margin: 0 auto;
    width: 750px;
    height: 200px;
    position: relative;
    background-color:rgb(13, 49, 133);
    .header-search-select {
      background-color: #fff;
      border-radius: 5px;
      width: 80%;
      height: 36px;
      border: 1px solid rgb(175, 175, 190);
      position: absolute;
      bottom: -18px;
      left: 50%;
      transform: translate(-50%, 0);
      font-size: 18px;
      transition: width 2s;
      display: inline-block;
      vertical-align: middle;
      input{
        border-radius: 5px;
        padding-left: 10px;
        border: none;
        outline: none;
        height: 100%;
        width: 95%;
      }
    }
  }
  .kind-block{
    padding-top: 3px;
    width:750px;
    height: 300px;
    margin: 50px auto;
    .kind-item{
      height: 130px;
      width: calc(100%/2 - 20px);
      float: left;
      margin-left: 13px;
      margin-bottom: 13px;
      border-radius: 10px;
      .item-link{
        display: inline-block;
        width: 100%;
        height: 100%;
      }
      .cn-des{
        padding: 0 05px;
        height: 60px;
        font-size: 14px;
        color: rgba(255, 255, 255, 0.7);
      }
      .cn-title{
        font-size: 16px;
        color: #fff;
        text-align: center;
      }
    }
    .kind-item:nth-child(1){
      background-color: rgb(43, 106, 199);
    }
    .kind-item:nth-child(2){
      background-color: rgba(104, 63, 66, 644);
    }
    .kind-item:nth-child(3){
      background-color: rgb(206, 90, 90);
    }
    .kind-item:nth-child(4){
      background-color: rgb(54, 60, 68);
    }
  }
}
</style>
