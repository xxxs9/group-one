<style xmlns:30pxwidth="http://www.w3.org/1999/xhtml" xmlns:height="http://www.w3.org/1999/xhtml">
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
          <el-form class="small-space" inline="true" :model="tempComment">
            <el-form-item>
              <el-input type="text" v-model="tempComment.commentText" placeholder="输入评论内容搜索"/>
            </el-form-item>
            <el-form-item>
              <el-input type="text" v-model="commentUserName" placeholder="输入评论用户搜索"/>
            </el-form-item>
            <el-date-picker
              v-model="tempComment.commentTime"
              type="daterange"
              align="right"
              unlink-panels validate-event
              @change="getList"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd HH:mm:ss"
              :default-time="['00:00:00', '23:59:59']">
            </el-date-picker>
            <el-form-item>
              <el-button type="primary" class="el-icon-search" @click="getList">搜索</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-refresh" circle @click="refashList"></el-button>
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
      <el-table-column align="center" label="发帖用户" prop="postUserName" style="width: 50px;"></el-table-column>
      <el-table-column align="center" label="评论用户" prop="commentUserName" style="width: 50px;"></el-table-column>
      <el-table-column align="center" label="接收评论用户" prop="acceptUserName" style="width: 120px;"></el-table-column>
      <!--<el-table-column align="center" label="评论内容" prop="commentText" width="140">-->
        <!---->
      <!--</el-table-column>-->
      <el-table-column align="center" label="帖子内容" style="line-height:10px" width="300px">
        <template slot-scope="scope">
          <div v-text="scope.row.commentText" style="line-height:25px">
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="评论时间" prop="commentTime" width="170"></el-table-column>
      <el-table-column align="center" label="评论详情" width="120">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showDetail(scope.$index)"
                     size="medium" class="el-icon-document">查看
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" width="220">
        <template slot-scope="scope">
          <el-button type="warning" icon="el-icon-warning" @click="warningComment(scope.$index)">警告</el-button>
          <el-button type="primary" icon="el-icon-view" v-if="scope.row.commentState==0" @click="removeComment(scope.$index)">显示</el-button>
          <el-button type="info" icon="el-icon-delete" v-if="scope.row.commentState==1" @click="removeComment(scope.$index)">隐藏</el-button>
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
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="35%">
      <detail :addDetailData="detailData" v-if="dialogStatus=='postDetail'" ref="detail"></detail>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">确 认</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {mapGetters} from 'vuex'
  import Detail from "../post/postdetail";

  export default {
    components: {Detail},
    data() {
      return {
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          commentText: '',
          commentUserName: '',
          beforeDate:'',
          afterDate:'',
          pageNum: 1,//页码
          pageRow: 50,//每页条数
        },
        detailData:'',
        roles: [],//角色列表
        dialogStatus: 'postDetail',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建用户',
          postDetail: '帖子详情'
        },
        tempComment: {
          commentId: '',
          postId: '',
          postUserName: '',
          commentUserName: '',
          acceptUserName: '',
          commentText: '',
          commentTime: '',
          commentState: ''
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
      if (this.hasPerm('user:add') || this.hasPerm('user:update')) {
        this.getAllRoles();
      }
    },
    computed: {
      ...mapGetters([
        'commentId'
      ])
    },

    methods: {
      getList() {
        //查询列表
        this.listLoading = true;
        this.listQuery.commentText = this.tempComment.commentText;
        console.log(this.commentUserName);
        this.listQuery.commentUserName = this.commentUserName;
        console.log(this.tempComment.commentTime[0]);
        this.listQuery.beforeDate = this.tempComment.commentTime[0];
        this.listQuery.afterDate = this.tempComment.commentTime[1];
        this.api({
          url: "/comment/list",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
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
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },

      removeComment($index) {
        let _vue = this;
        this.$confirm('确定修改状态?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let comment = _vue.list[$index];
          if(comment.commentState==1){
            comment.commentState = '0';
          }else {
            comment.commentState = '1';
          }

          _vue.api({
            url: "/comment/removeComment",
            method: "post",
            data: comment
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            _vue.$message.error("修改失败")
          })
        })
      },

      warningComment($index) {
        let _vue = this;
        this.$confirm('确定警告此用户?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let comment = _vue.list[$index];
          _vue.api({
            url: "/user/updateUser",
            method: "post",
            data: comment
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            _vue.$message.error("警告失败")
          })
        })
      },

      showDetail($index) {
        console.log("--------------")
        let comment = this.list[$index];
        this.tempComment.postId = comment.postId;   //帖子Id
        this.tempComment.commentTime = '';
        this.api({
          url: "/post/queryPostById",
          method: "post",
          params: this.tempComment
        }).then(data => {
          this.detailData = data;
          console.log(data)
          // this.tempPost.postPhone = data.postPhone; //电话
          // this.tempPost.postAddress = data.postAddress; //地址
          // this.tempPost.priceFloor = data.priceFloor; //最低价
          // this.tempPost.priceTop = data.priceTop; //最高价
          // this.tempPost.postImgList = data.postImgList;//图片数组
          // this.tempPost.commentText = data.comments; //评论
        }).catch(error => {

        })
        this.dialogStatus = "postDetail"
        this.dialogFormVisible = true
        // this.refs.detail.getDetailData()
      },
      refashList() {
        this.tempComment.commentText = '';
        this.tempComment.commentTime = '';
        this.commentUserName = '';
        this.getList();
      }
    }
  }
</script>
