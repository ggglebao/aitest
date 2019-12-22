<template>
  <div>
    <div v-show="showlayer" class="upload-layer">
      <el-upload class="upload-state" drag :action="uploadUrl" :on-success="uploadSuccess">
        <i class="el-icon-upload" />
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
      </el-upload>
      <i class="upload-close el-icon-close" @click="closeLayer" />
    </div>
  </div>
</template>

<script>
export default {
  name: 'ImgUpload',
  components: {},
  props: {
    showlayerFlag: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      showlayer: '',
      uploadUrl: process.env.VUE_APP_BASE_API + '/ai/getRecordString'
    }
  },
  watch: {
    showlayerFlag: function(newValue, oldValue) {
      this.showlayer = newValue
    }
  },
  created() {},
  mounted() {
      this.showlayer = this.showlayerFlag
  },
  destroyed() {},
  methods: {
    closeLayer() {
      this.showlayer = false
      this.$emit('changeShowLayFlag',this.showlayer)
    },
    uploadSuccess(rep){
      console.log(rep.data)
    }
  }
}
</script>

<style lang="scss">
.upload-layer{
  background-color: #fff;
  position: absolute;
  border: 1px solid #3385ff;
  z-index: 101;
  .upload-state{
    padding: 23px 20px 27px;
  }
  .upload-close{
    position: absolute;
    right: 18px;
    bottom: 13px;
    z-index: 2;
    background-position: -20px 0;
    width: 18px;
    height: 18px;
  }
}
</style>
