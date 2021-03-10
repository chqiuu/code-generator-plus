<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
        <#list columns as column>
            <#if column.columnName != pk.columnName>
    <el-form-item label="${column.comment}" prop="${column.attrNameLowerCase}">
      <el-input v-model="dataForm.${column.attrNameLowerCase}" placeholder="${column.comment}"></el-input>
    </el-form-item>
            </#if>
        </#list>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  export default {
    data () {
      return {
        visible: false,
        dataForm: {
      <#list columns as column>
      <#if column.columnName == pk.columnName>
          ${column.attrNameLowerCase}: 0,
<#else>
          ${column.attrNameLowerCase}: ''<#if column?has_next>,</#if>
</#if>
      </#list>
        },
        dataRule: {
          <#list columns as column>
          <#if column.columnName != pk.columnName>
          ${column.attrNameLowerCase}: [
            { required: true, message: '${column.comment}不能为空', trigger: 'blur' }
          ]<#if column?has_next>,</#if>
          </#if>
          </#list>
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.${pk.attrNameLowerCase} = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.${pk.attrNameLowerCase}) {
            this.$http({
              url: this.$http.adornUrl(`/${moduleName}/${mappingName}/info/${r'${'}this.dataForm.${pk.attrNameLowerCase}${r'}'}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                <#list columns as column>
                <#if column.columnName != pk.columnName>
                this.dataForm.${column.attrNameLowerCase} = data.${classNameLowerCase}.${column.attrNameLowerCase}
                </#if>
                </#list>
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/${moduleName}/${mappingName}/${r'${'}!this.dataForm.${pk.attrNameLowerCase} ? 'save' : 'update'${r'}'}`),
              method: 'post',
              data: this.$http.adornData({
            <#list columns as column>
            <#if column.columnName == pk.columnName>
                '${column.attrNameLowerCase}': this.dataForm.${column.attrNameLowerCase} || undefined,
<#else>
                '${column.attrNameLowerCase}': this.dataForm.${column.attrNameLowerCase}<#if column?has_next>,</#if>

            </#if>
            </#list>
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      }
    }
  }
</script>
