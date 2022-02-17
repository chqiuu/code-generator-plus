<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <h3>系统正在启动，请稍候……</h3>
    <a id="startUrl" href="{{ this.appUrl }}" style="visibility: collapse">
      home
    </a>
    
                  <a-button
                    :style="{ marginLeft: '8px' }"
                    @click="test"
                  >
                    测试按钮
                  </a-button>
  </div>
</template>

<script>
      const requestPromise = require("minimal-request-promise");
// const { remote } = require("electron");
 const log = require("electron-log");

export default {
  name: "Welcome",
  props: {
    msg: String,
  },
  data: function () {
    return {
      // 后台服务启动状态
      appUrl: "http://localhost:803",
      healthUrl: "http://localhost:803/generator/health",
    };
  },
  created() {
    // 加载完成后执行
  },
  mounted: function () {
    // 实例被挂载后调
  },
  methods: {
    test(){
     this.startUp();
    },
   async startUp() {
      let isSuccess = false;
      log.log("startUp start");
      while (!isSuccess) {
        requestPromise
          .get(this.healthUrl)
          .then((response) => {
            isSuccess = true;
            log.log("startUp http success", response.body);
            document.getElementById("startUrl").click();
            // remote.getCurrentWindow().setSize(200, 1000);
          })
          .catch((error) => {
            const p = new Promise((resolve, reject) => setTimeout(reject, 1000));
            p.catch(() => {}); // suppress unhandled rejection
            log.log("startUp sleep 500", error);
          });
      }
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
