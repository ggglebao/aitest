<template>
  <div class="head_wrapper">
    <el-form v-model="form">
      <el-form-item>
        <input v-model="form.time" type="button" class="btn-record-voice" @mousedown.prevent="mouseStart" @mouseup.prevent="mouseEnd">
        <audio v-if="playflag&&form.audioUrl" :src="form.audioUrl" controls="controls" class="content-audio">语音</audio>
      </el-form-item>
    </el-form>
    <el-button @click="playflag=true">重温经典</el-button>
    <el-button @click="playflag=false">重温够了</el-button>
    <div>
      <label>识别结果：{{ resolvedString }} </label>
    </div>
  </div>
</template>

<script>
import recording from '@/utils/recorder'
import { getRecordString } from '@/api/ai'
export default {
  name: 'DashRecord',
  components: {},
  data() {
    return {
      form: {
        time: '按住说话（60秒）',
        audioUrl: ''
      },
      num: 60, // 按住说话时间
      recorder: null,
      interval: '',
      startTime: '', // 语音开始时间
      endTime: '', // 语音结束
      resolvedString: '',
      playflag: false
    }
  },
  watch: {},
  created() {},
  mounted() {},
  destroyed() {},
  methods: {
    // 清除定时器
    clearTimer() {
      if (this.interval) {
        this.num = 60
        clearInterval(this.interval)
      }
    },
    // 长按说话
    mouseStart() {
      this.clearTimer()
      this.startTime = new Date().getTime()
      recording.get((rec) => {
        // 当首次按下时，要获取浏览器的麦克风权限，所以这时要做一个判断处理
        if (rec) {
          // 首次按下，只调用一次
          if (this.flag) {
            this.mouseEnd()
            this.flag = false
          } else {
            this.recorder = rec
            this.interval = setInterval(() => {
              if (this.num <= 0) {
                this.recorder.stop()
                this.num = 60
                this.clearTimer()
              } else {
                this.num--
                this.form.time = '松开结束（' + this.num + '秒）'
                this.recorder.start()
              }
            }, 1000)
          }
        }
      })
    },
    // 松开时上传语音
    mouseEnd() {
      this.clearTimer()
      this.endTime = new Date().getTime()
      if (this.recorder) {
        this.recorder.stop()
        // 重置说话时间
        this.num = 60
        this.form.time = '按住说话（' + this.num + '秒）'
        // 获取语音二进制文件
        const bolb = this.recorder.getBlob()
        this.form.audioUrl = window.URL.createObjectURL(bolb)
        // 将获取的二进制对象转为二进制文件流
        const file = new File([bolb], 'test.wav', { type: 'audio/wav', lastModified: Date.now() })
        const fd = new FormData()
        fd.append('file', file)
        // 这里是通过上传语音文件的接口，获取接口返回的路径作为语音路径
        this.uploadFile(fd)
      }
    },
    uploadFile(fd) {
      getRecordString(fd).then(response => {
        if(response.err_msg != null && response.err_msg != "success."){
          this.$emit("returnSpeechString",'')
          this.playfl = false
          alert(response.err_msg)
        } else if(response!=null && response.length > 0) {
          this.resolvedString = response.result[0]
          this.$emit("returnSpeechString",this.resolvedString)
        }
      })
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
