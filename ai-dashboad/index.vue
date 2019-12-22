<template>
  <div class="head_wrapper">
    test
    <input v-model="resolvedSpeech">
    <button>
      <router-link to="/aiSearch">检索</router-link>
    </button>
    <dashRecord @returnSpeechString="getSpeechString($event)"/>
    <button @click="showlayerFlag=true">上传图片</button>
    <imgUpload @changeShowLayFlag="flagchanged($event)" :showlayerFlag="showlayerFlag" />
  </div>
</template>

<script>
import dashRecord from "./dashRecord"
import imgUpload from "./imgUpload"
export default {
  name: "Ai",
  components: { dashRecord, imgUpload },
  data() {
    return {
      resolvedSpeech:'',
      showlayerFlag: false,
      activeName: "first"
    }
  },
  watch: {},
  created() {},
  mounted() {},
  destroyed() {},
  methods: {
    getSpeechString(speechString) {
      this.resolvedSpeech = speechString
    },
    flagchanged(flag) {
      this.showlayerFlag = flag
    }
  }
}
</script>

<style lang="scss">
  .index-logo-srcnew {
    display: none;
  }
  @media (-webkit-min-device-pixel-ratio: 2),
    (min--moz-device-pixel-ratio: 2),
    (-o-min-device-pixel-ratio: 2),
    (min-device-pixel-ratio: 2) {
    .index-logo-src {
      display: none;
    }
    .index-logo-srcnew {
      display: inline;
    }
  }
</style>
