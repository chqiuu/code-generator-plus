<template>
  <global-footer class="footer custom-render">
    <template v-slot:links>
      <a href="https://github.com/chqiuu/code-generator-plus-ant-vue" target="_blank">前端界面</a>
      <a href="https://github.com/chqiuu/code-generator-plus" target="_blank">Github</a>
      <a href="https://github.com/chqiuu" target="_blank">@chqiuu</a>
    </template>
    <template v-slot:copyright>
      <a href="https://github.com/chqiuu" target="_blank">CHQIUU</a>
    </template>
  </global-footer>
</template>

<script>
import { GlobalFooter } from '@ant-design-vue/pro-layout'

export default {
  name: 'ProGlobalFooter',
  components: {
    GlobalFooter,
  },
}
</script>
