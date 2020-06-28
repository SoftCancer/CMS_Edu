<template>
  <div>

    <el-form :inline="true" :model="params">
      <el-form-item label="站点名称">
        <el-select v-model="params.siteId" clearable placeholder="请选择站点">
          <el-option
            v-for="item in options"
            :key="item.siteId"
            :label="item.siteName"
            :value="item.siteId">
          </el-option>
        </el-select>
      </el-form-item>

      <el-form-item label="页面别名">
        <el-input v-model="params.pageAliase" style="width: 100px"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" icon="el-icon-search" v-on:click="queryCMSList">搜索</el-button>
      </el-form-item>

      <router-link :to="{path:'/cms/page/add',query:{
          page:this.params.page,
          siteId:this.params.siteId
        }}">
        <el-button type="primary">新增页面</el-button>
      </router-link>
    </el-form>

    <el-table
      :data="tableData"
      style="width: 100%">
      <el-table-column
        prop="pageName"
        label="页面名称"
        width="180">
      </el-table-column>
      <el-table-column
        prop="pageAliase"
        label="别名">
      </el-table-column>
      <el-table-column
        prop="pageType"
        label="页面类型">
      </el-table-column>
      <el-table-column
        prop="pageWebPath"
        label="访问路径">
      </el-table-column>
      <el-table-column
        prop="pagePhysicalPath"
        label="物理路径">
      </el-table-column>
      <el-table-column
        prop="pageCreateTime"
        label="日期"
        width="180">
      </el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button
            size="small"
            @click="handleEdit(scope.$index, scope.row)">编辑
          </el-button>
          <el-button
            size="small"
            type="danger"
            slot="reference"
            @click="handleDelete(scope.$index, scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      background
      style="float: right"
      layout="prev, pager, next"
      @current-change="currentPageChange"
      :total="total"
      :page-size="params.size"
      :current-page="params.page">
    </el-pagination>
  </div>
</template>

<script>
  // 导入请求后端的方法
  import * as cmsApi from '../api/cms'

  export default {
    data() {
      return {
        options: [],
        tableData: [],
        total: 80,
        params: {
          page: 1,
          size: 10,
          siteId: '',
          siteName: ''
        }
      }
    },


    created() {

      this.params.page = Number.parseInt(this.$route.query.page || 1);
      this.params.siteId = this.$route.query.siteId || "";

      this.querySiteAll();
      this.queryCMSList();
    },

    methods: {

      /**
       * @Description: 列表查询
       * @Author: YaoGX
       * @Date: 2020/6/27 22:50
       **/
      queryCMSList: function () {
        cmsApi.pageList(this.params.page, this.params.size, this.params).then((res) => {
          let code = res.code;
          if (code) {
            this.tableData = res.queryResult.list;
            this.total = res.queryResult.total;
          }
        })
      },

      /**
       *  实现分页查询
       * page: 形参，页码。
       * @Author: YaoGX
       * @Date: 2020/6/18 22:03
       **/
      currentPageChange(page) {
        this.params.page = page;
        this.queryCMSList();
      },

      /**
       *  获取站点列表
       * @Description:
       * @Author: YaoGX
       * @Date: 2020/6/26 23:53
       **/
      querySiteAll: function () {
        cmsApi.siteAll().then((res) => {
          let code = res.code;
          if (code) {
            console.log(JSON.stringify(res.queryResult.list))
            this.options = res.queryResult.list;
          }
        })
      },

      /**
       * @Description: 跳转到编辑页面
       *  index：行号
       *  row：行数据
       * @Author: YaoGX
       * @Date: 2020/6/27 22:53
       **/
      handleEdit(index, row) {
        let pageId = row.pageId;
        this.$router.push({
          path: '/cms/page/edit/' + pageId, query: {
            page: this.params.page,
            siteId: this.params.siteId
          }
        })
      },

      /**
       * 删除操作
       * @Description:
       * @Author: YaoGX
       * @Date: 2020/6/28 11:51
       **/
      handleDelete(index, row) {
        this.$confirm('此操作将永久删除数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          let pageId = row.pageId;
          cmsApi.pageDel(pageId).then((res) => {
            if (res.success) {
              this.$message({
                type: 'success',
                message: '删除成功!'
              });
              // 查询cms列表
              this.queryCMSList();
            } else {
              this.$message({
                type: 'error',
                duration:1,
                message: '删除失败!'
              });
            }
          });
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '取消删除'
          });
        });

      }


    }


  }
</script>

<style scoped>

</style>
