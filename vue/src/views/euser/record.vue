<style>
  .cell {
    height: 70px !important;
    overflow: auto !important;
    line-height: 70px !important;
  }
</style>
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-form class="small-space" :inline="true" :model="tempRecord">
            <el-form-item>
              <el-input type="text" v-model="tempRecord.querykey" placeholder="输入用户昵称搜索"/>
            </el-form-item>
            <el-form-item>
              <el-input type="text" v-model="tempRecord.queryText" placeholder="输入帖子内容搜索"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" class="el-icon-search" @click="getList"></el-button>
            </el-form-item>
            <el-form-item>
              <el-date-picker v-model="tempRecord.dateValue" type="daterange" align="right" unlink-panels validate-event
                              @change="getList"
                              start-placeholder="开始日期"
                              range-separator="-"
                              end-placeholder="结束日期"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              :picker-options="pickerOptions"
                              :default-time="['00:00:00', '23:59:59']">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" class="el-icon-close" @click="refreshList"></el-button>
            </el-form-item>
          </el-form>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
      <template slot-scope="scope">
      <span v-text="getIndex(scope.$index)"> </span>
      </template>
      </el-table-column>
      <!--<el-table-column align="center" label="用户ID" prop="userId" style="width: 60px;"></el-table-column>-->
      <!--<el-table-column align="center" label="UUID" prop="uuId" style="width: 60px;"></el-table-column>-->
      <el-table-column align="center" label="昵称" prop="username" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="性别" prop="sex" style="width: 60px;"></el-table-column>
      <!--<el-table-column align="center" label="帖子ID" prop="postId" style="width: 60px;"></el-table-column>-->
      <el-table-column align="center" label="帖子类型" prop="categoriesName" width="100"></el-table-column>

      <el-table-column align="center" label="帖子内容" width="100">
        <template slot-scope="scope">
          <div v-text="scope.row.postText" style="line-height:25px">
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="帖子状态" prop="postState" width="100"></el-table-column>
      <el-table-column align="center" label="浏览时间" property="browseTime" sortable width="170" ></el-table-column>
      <el-table-column align="center" label="帖子详情" width="150">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showDetail(scope.$index)"
                     size="medium" class="el-icon-document">查看
          </el-button>          <!--<el-button type="danger" icon="delete" v-if="scope.row.userId!=userId "-->
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
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="50%">
      <detail :addDetailData="detailData" v-if="dialogStatus=='postDetail'" ref="detail"></detail>
      <!--<detail :addDetailData="postdetailDeta" v-if="dialogStatus=='postDetail'" ref="detail"></detail>-->

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">确 认</el-button>
      </div>
    </el-dialog>

  </div>
</template>
<script>
  import {mapGetters} from 'vuex';
  import Detail from "../post/postdetail";
  export default {
    components: {Detail},
    data() {
      return {
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        detailData: '',
        listQuery: {
          querykey: '',
          queryText: '',
          beforeDate: '',
          afterDate: '',
          pageNum: 1,//页码
          pageRow: 50,//每页条数
        },
        dialogStatus: '',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建用户',
          fansOffset: '粉丝偏移量',
          postDetail: '帖子详情'
        },
        tempRecord: {
          userId: '',
          uuId: '',
          username: '',
          sex: '',
          postId: '',
          postType: '',
          postText: '',
          postState: '',
          browseTime: '',
          querykey: '',
          queryText: '',
          dateValue: ''
        },
        pickerOptions: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近三个月',
            onClick(picker) {
              const end = new Date();
              const start = new Date();
              start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
              picker.$emit('pick', [start, end]);
            }
          }]
        }
      }
    },
    created() {
      this.getList();

    },
    methods: {
      getList() {
        //查询列表
        this.listQuery.querykey = this.tempRecord.querykey;
        this.listQuery.queryText = this.tempRecord.queryText;
        this.listQuery.beforeDate = this.tempRecord.dateValue[0];
        this.listQuery.afterDate = this.tempRecord.dateValue[1];
        this.listLoading = true;
        this.api({
          url: "/euser/recordlist",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;

        })
      },
      refreshList(){
        // this.queryInput = "";
        this.listLoading = true;
        this.tempRecord.querykey="";
        this.tempRecord.queryText="";
        this.tempRecord.dateValue="";
        this.getList();
      },
      handleSizeChange(val) {
        //改变每页数量
        this.listQuery.pageRow = val
        this.handleFilter();
      },
      handleCurrentChange(val) {
        //改变页码
        this.listQuery.pageNum = val
        this.getList();
      },
      handleFilter() {
        //查询事件
        this.listQuery.pageNum = 1
        this.getList()
      },
      showDetail($index) {
        let record = this.list[$index];
        this.tempRecord.postId = record.postId;
        console.log(this.tempRecord.postId);
        this.api({
          url: "/post/queryPostById",
          method: "post",
          params: this.tempRecord
        }).then(data => {
          this.detailData = data;
          this.dialogStatus = "postDetail";
          this.dialogFormVisible = true;
        }).catch(error => {

        })
        this.dialogStatus = "postDetail";
        this.dialogFormVisible = true;
      },
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },

    }
  }
</script>
