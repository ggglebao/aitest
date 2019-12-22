<template>
  <div>
    <comHeader :header-title="rubbishName" />
    <div class="trash-info-wrap">
      <div class="trash-item-block">
        <span class="tipsTitle">垃圾种类：</span>
        <ul class="trash-lable">
          <li :class="['type-item', itemBgc]">{{ rubbishType }}</li>
        </ul>
        <p class="info-des">{{ rubbishDes.explain }}</p>
      </div>
      <div class="trash-item-block">
        <span class="tipsTitle">{{ rubbishType }}包括：</span>
        <p class="info-des">{{ rubbishDes.contain }}</p>
      </div>
      <div class="trash-item-block">
        <span class="tipsTitle">{{ rubbishType }}投放要求：</span>
        <p class="info-des">{{ rubbishDes.tip }}</p>
      </div>
      <!-- <span class="error-correct"><router-link :to="{path: '/errCorrection'}">纠错</router-link></span> -->
    </div>
  </div>
</template>

<script>
import { get } from '../../utils/request.js'
import comHeader from '../../components/ComHeader/index.vue'
export default {
  name: "AiRubbishDes",
  components: { comHeader },
  data() {
    return {
      activeName: "first",
      trashCanImg: require('../../assets/ai_images/trash_can.jpg'),
      rubbishDes: { "explain": "", "contain": "暂无", "tip": "暂无" },
      rubbishType: '无法分类',
      rubbishName: '',
      itemBgc: ''
    }
  },
  watch: {
  },
  created() {
    this.rubbishName = this.$route.query.name
    this.getRubbishDes(this.$route.query.name)
  },
  mounted() {},
  destroyed() {},
  methods: {
    getRubbishDes(name) {
      get('/rubbish', { "name": name, "num": 1, "page": 1 }).then(res => {
        this.rubbishDes = res[0]
        this.rubbishName = res[0].name
        const type = this.rubbishDes.type
        switch (type) {
          case 0:
          this.rubbishType = '可回收物'
          this.itemBgc = "itemBgc0"
          break
          case 1:
          this.rubbishType = '有害垃圾'
          this.itemBgc = "itemBgc1"
          break
          case 2:
          this.rubbishType = '湿垃圾'
          this.itemBgc = "itemBgc2"
          break
          case 3:
          this.rubbishType = '干垃圾'
          this.itemBgc = "itemBgc3"
          break
          default:
            this.rubbishType = '无法分类'
            this.itemBgc = ""
        }
      })
    }
  }
}
</script>

<style lang="scss">

.trash-info-wrap{
  overflow:hidden;
  margin: 38px auto 0;
  width: 750px;
  padding: 10px;
  color: #000;
  font-size: 16px;
  .trash-item-block{
    margin-top: 10px;
  }
.tipsTitle{
  font-size: 16px;
  font-weight: bold;
}
.info-des{
  margin-block-start: 10px;
  color:rgb(106, 106, 119);
}
  .trash-lable{
    display: inline-block;
    padding: 0;
    margin-top: 10px;
    margin-block-start: 0;
    margin-block-end: 0;
    li{
      font-size: 14px;
      color: #fff;
      display: inline-block;
      list-style: none;
      padding: 4px 15px;
      border-radius: 15px;
      margin-right: 10px;
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
  .error-correct{
    float: right;
    padding: 5px 8px;
    background-color: #ce3b3b;
    color: #fff;
    font-size: 12px;
    border-bottom-left-radius: 12px;
    border-top-left-radius: 12px;
    margin-top: 1px;
  }
}
</style>
