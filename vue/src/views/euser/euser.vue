<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-form class="small-space" inline="true" :model="tempUser">
            <el-form-item>
              <el-input type="text" :type="queryInput" v-model="tempUser.querykey" placeholder="输入用户昵称关键字搜索"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" class="el-icon-search" @click="getList"></el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" class="el-icon-close" @click="refleshList"></el-button>
            </el-form-item>
          </el-form>
        </el-form-item>
      </el-form>
    </div>
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
      <el-table-column align="center" label="头像" prop="iconUrl" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="电话" prop="mobile" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="性别" prop="sex" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="粉丝数量/偏移量" prop="fansOffset" style="width: 60px;">
        <template slot-scope="scope">
          <span style="font-size: 15px" v-text="scope.row.likeCount+'/'"></span>
          <el-button type="primary" icon="edit" v-text="scope.row.fansOffset" size="mini" @click="showfansOffset(scope.$index)"></el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" label="登录时间" prop="loginTime" width="170"></el-table-column>
      <el-table-column align="center" label="修改时间" prop="modifyTime" width="170"></el-table-column>
      <el-table-column align="center" label="管理" width="220" v-if="hasPerm('user:update')">
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
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="tempUser" label-position="left" label-width="80px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="UUID" >
          <el-input type="text" v-model="tempUser.uuId" disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="用户昵称" >
          <el-input type="text" v-model="tempUser.username" disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="头像" >
          <el-input type="text" v-model="tempUser.iconUrl" disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="电话" >
          <el-input type="text" v-model="tempUser.mobile" disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="性别" >
          <el-input type="text" v-model="tempUser.sex" disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="粉丝偏移量" >
          <el-input type="text" v-model="tempUser.fansOffset">
          </el-input>
        </el-form-item>
        <el-form-item label="登录时间" >
          <el-input type="text" v-model="tempUser.loginTime" disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="修改时间" >
          <el-input type="text" v-model="tempUser.modifyTime" disabled="true">
          </el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <!--<el-button v-if="dialogStatus=='create'" type="success" @click="createUser">创 建</el-button>-->
        <el-button type="primary"  @click="updateUser">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {mapGetters} from 'vuex'

  export default {
    data() {
      return {
        queryInput: '',
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          querykey:'',
          pageNum: 1,//页码
          pageRow: 50,//每页条数
        },
        roles: [],//角色列表
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建用户'
        },
        tempUser: {
          userId: '',
          uuId: '',
          username: '',
          iconUrl: '',
          mobile: '',
          fansOffset: '',
          sex: '',
          loginTime: '',
          modifyTime: '',
          querykey:''
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
      getAllRoles() {
        this.api({
          url: "/user/getAllRoles",
          method: "get"
        }).then(data => {
          this.roles = data.list;
        })
      },
      getList() {
        //查询列表
        this.listQuery.querykey = this.tempUser.querykey;
        this.listLoading = true;
        this.api({
          url: "/euser/userlist",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
      refleshList(){
        // this.queryInput = "";
        this.listLoading = true;
        this.tempUser.querykey="";
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
      // getIndex($index) {
      //   //表格序号
      //   return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      // },
      showfansOffset($index){
        let user = this.list[$index];
        this.tempUser.fansOffset = user.fansOffset;
        this.tempUser.uuId = user.uuId;
        this.dialogStatus = "fansOffset";
        this.dialogFormVisible = true;
      },
      showCreate() {
        //显示新增对话框
        this.tempUser.username = "";
        this.tempUser.password = "";
        this.tempUser.nickname = "";
        this.tempUser.roleId = "";
        this.tempUser.userId = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        let user = this.list[$index];
        this.tempUser.userId = user.userId;
        this.tempUser.uuId = user.uuId;
        this.tempUser.username = user.username;
        this.tempUser.iconUrl = user.iconUrl;
        this.tempUser.mobile = user.mobile;
        this.tempUser.sex = user.sex;
        this.tempUser.fansOffset = user.fansOffset;
        this.tempUser.loginTime = user.loginTime;
        this.tempUser.modifyTime = user.modifyTime;
        this.dialogStatus = "update";
        this.dialogFormVisible = true;
      },
      createUser() {
        //添加新用户
        this.api({
          url: "/user/addUser",
          method: "post",
          data: this.tempUser
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false
        })
      },
      updateUser() {
        //修改用户信息
        let _vue = this;
        this.api({
          url: "/euser/updateFO",
          method: "post",
          data: this.tempUser
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          if (this.userId === this.tempUser.userId) {
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
