<template>
  <div>
    <el-form ref="pageForm" :model="pageForm" :rules="pageFromRules" label-width="80px">
      <el-form-item label="页面名称" prop="pageName">
        <el-input v-model="pageForm.pageName"></el-input>
      </el-form-item>
      <el-form-item label="别名" prop="pageAliase">
        <el-input v-model="pageForm.pageAliase"></el-input>
      </el-form-item>
      <el-form-item label="访问路径" prop="pageWebPath">
        <el-input v-model="pageForm.pageWebPath"></el-input>
      </el-form-item>
      <el-form-item label="物理路径" prop="pagePhysicalPath">
        <el-input v-model="pageForm.pagePhysicalPath"></el-input>
      </el-form-item>
      <el-form-item label="所属站点">
        <el-select v-model="pageForm.siteId" placeholder="请选择站点">
          <el-option
            v-for="item in siteList"
            :key="item.siteId"
            :label="item.siteName"
            :value="item.siteId">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="选择模板">
        <el-select v-model="pageForm.templateId" placeholder="请选择模板">
          <el-option label="门户主站" value="5edb9d6683712dfb8667abb7"></el-option>
          <el-option label="区域二" value="beijing"></el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="类型">
        <el-radio-group v-model="pageForm.pageType">
          <el-radio label="类型o" value="0"></el-radio>
          <el-radio label="类型1" value="1"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="活动时间">
          <el-date-picker
            v-model="pageForm.pageCreateTime"
            type="datetime"
            placeholder="选择日期">
          </el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">立即创建</el-button>
        <el-button type="primary" @click="go_back">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
  // 导入请求后端的方法
  import * as cmsApi from '../api/cms'

  export default {
    data() {
      return {
        siteList:[],

        pageForm: {
          pageName: '',
          pageAliase: '',
          pageWebPath: '',
          pagePhysicalPath: '',
          siteId: '',
          templateId: '',
          pageType: '',
          pageCreateTime: new Date()
        },
        pageFromRules: {
          siteId: [
            {required: true, message: "请选择站点", trigger: 'blur'}
          ],
          templateId: [
            {required: true, message: "请选择模板", trigger: 'blur'}
          ],
          pageName: [
            {required: true, message: "请输入页面名称", trigger: 'blur'}
          ],
          pageWebPath: [
            {required: true, message: "请输入访问路径", trigger: 'blur'}
          ],
          pagePhysicalPath: [
            {required: true, message: "请输入物理路径", trigger: 'blur'}
          ],
        }
      }
    },

    created(){
      this.querySiteAll();
    },

    methods: {
      // 提交保存
      onSubmit() {
        this.$refs.pageForm.validate((valid) => {
          if (valid) {
            cmsApi.pageAdd(this.pageForm).then(res => {
              if (res.success) {
                this.$message({
                  message: '保存成功',
                  type: 'success'
                });
                // 保存成功，返回列表页
                this.go_back();
                // 保存成功，清空表单
                // this.$refs.pageForm.resetFields();
              }
            })
          }
        });
      },

      // 返回
      go_back() {
        // 获取当前页路由，并赋值
        this.$router.push({
          path: '/cms/page/list',
          query: {
            page: this.$route.query.page,
            siteId: this.$route.query.siteId
          }
        })
      },

      querySiteAll: function () {
        cmsApi.siteAll().then((res) => {
          let code = res.code;
          if (code) {
            console.log(JSON.stringify(res.queryResult.list))
            this.siteList = res.queryResult.list;
          }
        })
      },

    }


  }

</script>

<style scoped>

</style>
