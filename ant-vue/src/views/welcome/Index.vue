<template>
  <div :style="{ padding: '24px', background: '#fff', minHeight: '360px' }">
    <VueMarkdown :source="mdData" v-highlight />
  </div>
</template>

<script>
import VueMarkdown from 'vue-markdown'
// 这种方式引入 数据可直接使用
import markdownData from '../../../README.md'

export default {
  name: 'WelcomePage',
 data: function () {
    return {
           mdData: markdownData,
    }
  },
  components: { VueMarkdown },
  mounted () { },
  methods: {},
}
</script>

<style lang="less" scoped>

</style>
