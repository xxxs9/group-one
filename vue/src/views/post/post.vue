<style>
  .cell{max-height: 50px !important;overflow: auto !important;}
</style>
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-form class="small-space" inline="true" :model="tempPost">
            <el-form-item>
              <el-input type="text" :type="queryInput" v-model="tempPost.queryText" placeholder="输入帖子内容搜索"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" class="el-icon-search" @click="getList"></el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" class="el-icon-close" @click="refashList"></el-button>
            </el-form-item>
          </el-form>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" stripe="true" default-sor v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column align="center" label="序号" sortable="true" width="100" prop="postId">
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

      <el-table-column align="center" label="帖子内容" prop="postText" style="width: 100px;"></el-table-column>

      <el-table-column align="center" label="发帖人昵称" prop="postOwnerName" width="100"></el-table-column>

      <el-table-column align="center" label="发帖时间" sortable="true" prop="postTime" width="170"></el-table-column>

      <el-table-column align="center" label="点赞数/增加量" prop="likeCount" width="120px">
        <template slot-scope="scope">
          <span style="font-size: 15px" v-text="scope.row.likeCount+'/'"></span>
          <el-button type="primary" icon="edit" v-text="scope.row.likeOffset" size="mini" @click="showLikeOffset(scope.$index)"></el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" label="浏览数/增加量" prop="browseOffset" width="120px">
        <template slot-scope="scope">
          <span v-text="scope.row.browseCount+'/'"></span>
          <el-button type="primary" icon="edit" v-text="scope.row.browseOffset" size="mini" @click="showBrowseOffset(scope.$index)"></el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" label="标签" style="width: 90px;" width="290">
        <template slot-scope="scope">
          <div v-for="posts in list" style="text-align: center">
            <el-tag v-for="tag in posts.postTagList" :key="index" v-if="scope.$index+1==posts.postId" v-text="tag" style="margin-right: 3px;" type="primary" />
          </div>
        </template>
      </el-table-column>

      <el-table-column align="center" label="帖子详情" width="120" v-if="hasPerm('user:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="" size="medium" class="el-icon-document">查看</el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" width="220" v-if="hasPerm('user:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" size="medium" @click="showUpdate(scope.$index)">修改</el-button>
          <el-button type="danger" icon="delete" size="medium" v-if="scope.row.userId!=userId "
                     @click="removeUser(scope.$index)">删除
          </el-button>
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
          likeOffset: '',
          browseOffset: '',
          likeCount: '',
          browseCount: '',
          postTypeId: '',
          queryText: '',
          tagList: [],
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
        this.listLoading = true;
        this.api({
          url: "/post/list",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          for (var i = 0; i < this.list.length ;i++){
              this.tagList = this.list[i].postTagList;
              console.log(this.list[i].postId+this.list[i].postTagList)
          }
          this.totalCount = data.totalCount;
        })
      },
      refashList(){
        this.queryInput = '';
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
      removeUser($index) {
        let _vue = this;
        this.$confirm('确定删除此用户?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let user = _vue.list[$index];
          user.deleteStatus = '2';
          _vue.api({
            url: "/user/updateUser",
            method: "post",
            data: user
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            _vue.$message.error("删除失败")
          })
        })
      },
    }
  }
</script>
