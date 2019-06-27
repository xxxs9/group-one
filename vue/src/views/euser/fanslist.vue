<template>
  <div class="app-container">
    <div class="filter-container">
      <el-table :data="fanslist" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
                highlight-current-row>
        <el-table-column align="center" label="用户ID" prop="userId" style="width: 60px;"></el-table-column>
        <el-table-column align="center" label="UUID" prop="uuId" style="width: 60px;"></el-table-column>
        <el-table-column align="center" label="昵称" prop="username" style="width: 60px;"></el-table-column>
        <el-table-column align="center" label="头像"  style="width: 60px;">
          <template slot-scope="scope">
            <img :src="scope.row.iconUrl" width="60" height="60"
                 style="border-radius: 50%; display: block" class="head_pic"/>
          </template>
        </el-table-column>
        <el-table-column align="center" label="电话" prop="mobile" style="width: 60px;"></el-table-column>
        <el-table-column align="center" label="性别" prop="sex" style="width: 60px;"></el-table-column>


      </el-table>
      <!--<el-pagination-->
        <!--@size-change="handleSizeChange"-->
        <!--@current-change="handleCurrentChange"-->
        <!--:current-page="listQuery.pageNum"-->
        <!--:page-size="listQuery.pageRow"-->
        <!--:total="totalCount"-->
        <!--:page-sizes="[10, 20, 50, 100]"-->
        <!--layout="total, sizes, prev, pager, next, jumper">-->
      <!--</el-pagination>-->

    </div>
  </div>
</template>
<script>
  export default {
    props: ['fanslist'],
    data(){
      return {
        listLoading: false,
      }
    }



  }
</script>
