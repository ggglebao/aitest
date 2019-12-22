<template>
  <div class="trash-type-container">
    <comHeader :header-title="rubbishTypeName" />
    <div class="type-des-wrap">
      <div class="item">
        <span>信息简介：</span>
        <p>{{ rubbishList[0].explain }}</p>
      </div>
      <div class="item">
        <span>{{ rubbishTypeName }}主要包括：</span>
        <p>{{ rubbishList[0].contain }}</p>
      </div>
      <div class="item">
        <span>{{ rubbishTypeName }}投放要求：</span>
        <p>{{ rubbishList[0].tip }}</p>
      </div>
      <div class="item">
        <span>同种类垃圾：</span>
        <ul>
          <li v-for="(item, index) in rubbishList" :key="index" :class="itemBgc">{{ item.name }}</li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import comHeader from '../../components/ComHeader/index.vue'
import { get } from '../../utils/request.js'
export default {
  name: "AiTrashTypeDes",
  components: { comHeader },
  data() {
    return {
      activeName: "first",
      rubbishList: [{ "explain": "", "contain": "", "tip": "", "name": '' }],
      rubbishTypeName: '可回收物',
      itemBgc: ''
    }
  },
  watch: {
  },
  created() {
    switch (this.$route.query.type) {
          case 0:
          this.rubbishTypeName = '可回收物'
          this.itemBgc = "itemBgc0"
          break
          case 1:
          this.rubbishTypeName = '有害垃圾'
          this.itemBgc = "itemBgc1"
          break
          case 2:
          this.rubbishTypeName = '湿垃圾'
          this.itemBgc = "itemBgc2"
          break
          case 3:
          this.rubbishTypeName = '干垃圾'
          this.itemBgc = "itemBgc3"
          break
          default:
            this.rubbishTypeName = '无法分类'
            this.itemBgc = ""
        }
      this.getRubbishTypeDes(this.$route.query.type)
  },
  mounted() {},
  destroyed() {},
  methods: {
    getRubbishTypeDes(type) {
      get('/rubbish/all/' + type).then(res => {
        if (res) {
          this.rubbishList = res
        }
      })
    }
  }
}
</script>

<style lang="scss">
  .trash-type-container {
     p{
      padding: 0;
      margin-block-start: 05px;
    }
    .type-des-wrap{
      width: 750px;
      margin: 0 auto;
      margin-top: 38px;
      padding: 15px;
      .item{
        span{
          font-size: 16px;
          color: #000;
          font-weight: bold;
        }
        p{
          font-size: 16px;
          color: #353131;
        }
        ul{
          margin: 0;
          margin-block-start: 5px;
          padding: 0;
          li{
            display: inline-block;
            margin-right: 10px;
            margin-top: 10px;
            color: #fff;
            font-size: 14px;
            padding: 5px 10px;
            border-radius: 14px;
          }
          .itemBgc0{
            background-color: #2b6ac7;
          }
          .itemBgc1{
            background-color: rgba(104,63,66,644);
          }
          .itemBgc2{
            background-color: #ce5a5a;
          }
          .itemBgc3{
            background-color: #363c44;
          }
        }
      }
    }

  }
</style>
