<style>
  .cell{height: 70px !important;overflow: auto !important; line-height:70px !important;}
</style>
<template>
  <div class="app-container">
    <div class="filter-container">
          <el-form class="small-space" inline="true" :model="tempPost">
            <el-form-item>
              <el-date-picker v-model="tempPost.dataValue" type="daterange" align="right" unlink-panels validate-event @change="getList"
                              start-placeholder="开始日期"
                              range-separator="-"
                              end-placeholder="结束日期"
                              value-format="yyyy-MM-dd HH:mm:ss"
                              :picker-options="pickerOptions"
                              :default-time="['00:00:00', '23:59:59']">
              </el-date-picker>
            </el-form-item>
            <el-form-item>
              <el-input type="text" v-model="tempPost.queryText" placeholder="输入帖子内容搜索"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" class="el-icon-search" @click="getList"></el-button>
            </el-form-item>
            <el-form-item>
               <el-button type="primary" class="el-icon-close" @click="refashList"></el-button>
            </el-form-item>
          </el-form>
    </div>
    <el-table :data="list" stripe="true" default-sor v-loading.body="listLoading" element-loading-text="拼命加载中" border fit highlight-current-row>
      <el-table-column align="center" label="序号" sortable width="80" prop="postId">
      </el-table-column>
      <el-table-column align="center" label="帖子类型" width="130">
        <template slot-scope="scope">
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==1" color="#85D469"></el-tag>
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==2" color="#0ACCCE"></el-tag>
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==3" color="#FF90A3"></el-tag>
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==4" color="#A29EDB"></el-tag>
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==5" color="#0067C5"></el-tag>
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==6" color="#F78700"></el-tag>
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==7" color="#B4A294"></el-tag>
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==8" color="#798EA3"></el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="帖子内容" width="250" style="line-height:10px">
        <template slot-scope="scope">
          <div v-text="scope.row.postText" style="line-height:25px">
          </div>
        </template>
      </el-table-column>

      <el-table-column align="center" label="发帖人昵称" prop="postOwnerName" width="100"></el-table-column>

      <el-table-column align="center" label="发帖时间" sortable prop="postTime" width="170"
                       :filters="[{text: '一天内', value: '2016-05-01'}, {text: '一周内', value: '2016-05-02'}, {text: '一个月内', value: '2016-05-03'}, {text: '半年内', value: '2016-05-04'}]"
                       :filter-method="filterHandler"
      ></el-table-column>

      <el-table-column align="center" label="点赞数/增加量" sortable prop="likeCount" width="140px">
        <template slot-scope="scope">
          <span style="font-size: 15px" v-text="scope.row.likeCount+'/'"></span>
          <el-button type="primary" icon="edit" v-text="scope.row.likeOffset" size="mini" @click="showLikeOffset(scope.$index)"></el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" label="浏览数/增加量" sortable prop="browseOffset" width="140px">
        <template slot-scope="scope">
          <span v-text="scope.row.browseCount+'/'"></span>
          <el-button type="primary" icon="edit" v-text="scope.row.browseOffset" size="mini" @click="showBrowseOffset(scope.$index)"></el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" label="标签" style="width: 90px;" width="250">
        <template slot-scope="scope">
          <div v-for="posts in list" style="text-align: center">
            <el-tag v-for="tag in posts.postTagList" :key="index" v-if="scope.row.postId==posts.postId" v-text="tag" style="margin-right: 3px;" type="primary" />
          </div>
        </template>
      </el-table-column>

      <el-table-column align="center" label="帖子详情" width="120" v-if="hasPerm('user:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="" size="medium" class="el-icon-document">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" label="管理" width="220" v-if="hasPerm('user:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="medium" @click="showUpdate(scope.$index)">修改</el-button>
          <el-button type="danger" icon="el-icon-delete" size="medium" v-if="scope.row.postState==0" @click="removePost(scope.$index)">删除</el-button>
          <el-button type="success" icon="el-icon-refresh" size="medium" v-if="scope.row.postState==1" @click="removePost(scope.$index)">恢复</el-button>
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
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="30%">
      <el-form class="small-space" :model="tempPost" label-position="left" label-width="80px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="用户名" required v-if="dialogStatus=='create'">
          <el-input type="text" v-model="tempPost.username">
          </el-input>
        </el-form-item>
        <el-form-item label="输入数量" required v-if="dialogStatus=='likeOffset'">
          <el-input type="text" v-model="tempPost.likeOffset">
          </el-input>
        </el-form-item>

        <el-form-item label="输入数量" required v-if="dialogStatus=='browseOffset'">
          <el-input type="text" v-model="tempPost.browseOffset">
          </el-input>
        </el-form-item>

        <el-form-item label="密码" v-if="dialogStatus=='create'" required>
          <el-input type="password" v-model="tempPost.password">
          </el-input>
        </el-form-item>
        <el-form-item label="新密码" v-if="dialogStatus=='update'" v-else>
          <el-input type="password" v-model="tempPost.password" placeholder="不填则表示不修改">
          </el-input>
        </el-form-item>
        <el-form-item label="角色" v-if="dialogStatus=='update'" required>
          <el-select v-model="tempPost.roleId" placeholder="请选择">
            <el-option
              v-for="item in roles"
              :key="item.roleId"
              :label="item.roleName"
              :value="item.roleId">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="昵称" v-if="dialogStatus=='update'" required>
          <el-input type="text" v-model="tempPost.nickname">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createUser">创 建</el-button>
        <el-button type="primary" v-if="dialogStatus=='update'" @click="updateUser">修 改</el-button>
        <el-button type="primary" v-if="dialogStatus=='browseOffset'" @click="updateBrowseOffset">修改浏览数量</el-button>
        <el-button type="primary" v-if="dialogStatus=='likeOffset'" @click="updateLikeOffset">修改点赞数量</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {mapGetters} from 'vuex'

  export default {
    data() {
      return {
        tagList: [],
        queryInput: '',
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          beforeDate:'',
          afterDate:'',
          queryText: '',
          pageNum: 1,//页码
          pageRow: 10,//每页条数
        },
        roles: [],//角色列表

        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建用户',
          likeOffset: '增加点赞数量',
          browseOffset: '增加浏览数量'
        },
        tempPost: {
          postId:'',
          postType: '',
          postText: '',
          likeOffset: '',
          browseOffset: '',
          likeCount: '',
          browseCount: '',
          postTypeId: '',
          queryText: '',
          dataValue: '', //日期选择初始化
          postState:'',
          tagList: [],
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
        'userId'
      ])
    },
    methods: {

      getList() {
        //查询列表
        this.listQuery.queryText = this.tempPost.queryText;
        this.listQuery.beforeDate = this.tempPost.dataValue[0];
        this.listQuery.afterDate = this.tempPost.dataValue[1];
        this.listLoading = true;
        this.api({
          url: "/post/list",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
      refashList(){
        this.tempPost.queryText = '';
        this.tempPost.dataValue = '';
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
      showCreate() {
        //显示新增对话框
        this.tempPost.username = "";
        this.tempPost.password = "";
        this.tempPost.nickname = "";
        this.tempPost.roleId = "";
        this.tempPost.userId = "";
        this.dialogStatus = "create";
        this.dialogFormVisible = true
      },
      showLikeOffset($index){
        let post = this.list[$index];
        this.tempPost.likeOffset = post.likeOffset;
        this.tempPost.postId = post.postId;
        this.dialogStatus = "likeOffset";
        this.dialogFormVisible = true;
      },
      showBrowseOffset($index){
        let post = this.list[$index];
        this.tempPost.browseOffset = post.browseOffset;
        this.tempPost.postId = post.postId;
        this.dialogStatus = "browseOffset";
        this.dialogFormVisible = true;
      },
      showUpdate($index) {
        let user = this.list[$index];
        this.tempPost.username = user.username;
        this.tempPost.nickname = user.nickname;
        this.tempPost.roleId = user.roleId;
        this.tempPost.userId = user.userId;
        this.tempPost.deleteStatus = '1';
        this.tempPost.password = '';
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createUser() {
        //添加新用户
        this.api({
          url: "/user/addUser",
          method: "post",
          data: this.tempPost
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },
      updateLikeOffset(){
        let _vue = this;
        this.api({
          url: "/post/updateLikeOffset",
          method: "post",
          data: this.tempPost
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          this.$message({
            message: msg,
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              _vue.getList();
            }
          })
        })
      },
      updateBrowseOffset(){
        let _vue = this;
        this.api({
          url: "/post/updateBrowseOffset",
          method: "post",
          data: this.tempPost
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          this.$message({
            message: msg,
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              _vue.getList();
            }
          })
        })
      },
      updateUser() {
        //修改用户信息
        let _vue = this;
        this.api({
          url: "/user/updateUser",
          method: "post",
          data: this.tempPost
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          if (this.userId === this.tempPost.userId) {
            msg = '修改成功,部分信息重新登录后生效'
          }
          this.$message({
            message: msg,
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              _vue.getList();
            }
          })
        })
      },
      removePost($index) {
        let _vue = this;
        let post = _vue.list[$index];
        let msg ;
        if(post.postState == 0){
          msg = '确定删除这个帖子?';
        }else if(post.postState == 1){
          msg = '确定恢复这个帖子?';
        }
        this.$confirm(msg, '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          if(post.postState == 0){
            post.postState = 1;
          }else if(post.postState == 1){
            post.postState = 0;
          }
          _vue.api({
            url: "/post/updatePostState",
            method: "post",
            data: post
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            let defmsg;
            if(post.postState == 0){
              defmsg = '删除失败';
            }else if(post.postState == 1){
              defmsg = '恢复失败';
            }
            _vue.$message.error(defmsg)
          })
        })
      },
    }
  }
</script>
