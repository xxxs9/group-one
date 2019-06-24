<template>
  <div class="app-container">
    <div class="filter-container">
      <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
                highlight-current-row>
        <!--<el-table-column align="center" label="序号" width="80">-->
        <!--<template slot-scope="scope">-->
        <!--<span v-text="getIndex(scope.$index)"> </span>-->
        <!--</template>-->
        <!--</el-table-column>-->
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
        <el-table-column align="center" label="粉丝数量/偏移量" prop="fansOffset" width="120">
          <template slot-scope="scope">
            <span style="font-size: 15px" v-text="scope.row.fansCount+'/'"></span>
            <el-button type="primary" icon="edit" v-text="scope.row.fansOffset" size="mini" @click="showfansOffset(scope.$index)"></el-button>
          </template>
        </el-table-column>
        <el-table-column align="center" label="登录时间" prop="loginTime" width="170"></el-table-column>
        <el-table-column align="center" label="修改时间" prop="modifyTime" width="170"></el-table-column>
        <el-table-column align="center" label="管理" width="100" v-if="hasPerm('user:update')">
          <template slot-scope="scope">
            <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
            <!--<el-button type="danger" icon="delete" v-if="scope.row.userId!=userId "-->
            <!--@click="removeUser(scope.$index)">删除-->
            <!--</el-button>-->
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="listQuery.pageNum"
        :page-size="listQuery.pageRow"
        :total="totalCount"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper">
      </el-pagination>
    </div>
  </div>
</template>
<script>
  export default {
    props: ['fanslist'],


    methods: {

    }
  }
</script>
