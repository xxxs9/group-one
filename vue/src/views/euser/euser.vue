<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-form class="small-space" :inline="true" :model="tempUser">
            <el-form-item>
              <el-input id="inputkey" type="text" v-model="tempUser.querykey" placeholder="输入用户昵称关键字搜索" @keyup.native="checkKey()"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" class="el-icon-search" @click="getList"></el-button>
            </el-form-item>
            <el-form-item>
              <el-date-picker v-model="tempUser.dateValue" type="daterange" align="right" unlink-panels validate-event
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
      <el-table-column align="center" label="头像"  style="width: 60px;">
        <template slot-scope="scope">
          <img :src="scope.row.iconUrl" width="60" height="60"
               style="border-radius: 50%; display: block" class="head_pic"/>
        </template>
      </el-table-column>
      <!--<el-table-column align="center" label="电话" prop="mobile" style="width: 60px;"></el-table-column>-->
      <el-table-column align="center" label="性别" prop="sex" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="粉丝数量/偏移量" prop="fansOffset" width="150">
        <template slot-scope="scope">
          <el-button type="primary"  v-text="scope.row.fansCount" size="mini" @click="showfansList(scope.$index)"></el-button>
          <span>+</span>
          <el-button v-if="hasPerm('euser:update')" type="primary"  v-text="scope.row.fansOffset" size="mini" @click="showfansOffset(scope.$index)"></el-button>
          <span v-else v-text="scope.row.fansOffset"></span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="登录时间" prop="loginTime" width="170"></el-table-column>
      <el-table-column align="center" label="修改时间" prop="modifyTime" width="170"></el-table-column>
      <el-table-column align="center" label="管理" width="140" v-if="hasPerm('euser:update')">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" @click="showUpdate(scope.$index)">修改</el-button>
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
    <el-dialog title="粉丝列表" :visible.sync="dialogFansVisible" width="50%" >
      <all-fans :fanslist="fanslist"></all-fans>
      <div slot="footer" class="dialog-footer">
      <el-button @click="dialogFansVisible = false">确认</el-button>
      </div>
    </el-dialog>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">

      <el-form class="small-space" :model="tempUser" label-position="left" label-width="80px"
               style='width: 300px; margin-left:50px;'>
        <!--<el-form-item label="UUID" v-if="dialogStatus=='fansOffset' ||'update'" >-->
          <!--<el-input type="text" v-model="tempUser.uuId" disabled="true">-->
          <!--</el-input>-->
        <!--</el-form-item>-->
        <el-form-item label="用户昵称" v-if="dialogStatus=='fansOffset' ||'update'" >
          <el-input type="text" v-model="tempUser.username" disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="头像" v-if="dialogStatus=='update'">
          <!--<el-input type="text" v-model="tempUser.iconUrl" disabled="true">-->
          <!--</el-input>-->
          <template slot-scope="scope">
            <img :src="tempUser.iconUrl" width="60" height="60"
                 style="border-radius: 50%; display: block" class="head_pic"/>
          </template>
        </el-form-item>
        <!--<el-form-item label="电话" v-if="dialogStatus=='update'">-->
          <!--<el-input type="text" v-model="tempUser.mobile" disabled="true">-->
          <!--</el-input>-->
        <!--</el-form-item>-->
        <el-form-item label="性别" v-if="dialogStatus=='update'">
          <el-input type="text" v-model="tempUser.sex" disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="粉丝偏移量" v-if="dialogStatus=='fansOffset' ||'update'" >
          <el-input type="text" v-model="tempUser.fansOffset" @keyup.native="checkFO()">
          </el-input>
          <span ref="text" id="text" style="display: none;color: red" >请输入有效的偏移量（1-6位数字）</span>
        </el-form-item>
        <el-form-item label="登录时间" v-if="dialogStatus=='update'">
          <el-input type="text" v-model="tempUser.loginTime" disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="修改时间" v-if="dialogStatus=='update'">
          <el-input type="text" v-model="tempUser.modifyTime" disabled="true">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false" v-if="dialogStatus=='fansOffset' ||'update'">取 消</el-button>
        <!--<el-button v-if="dialogStatus=='create'" type="success" @click="createUser">创 建</el-button>-->
        <el-button type="primary"  @click="updateUser" v-if="dialogStatus=='fansOffset' ||'update'">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {mapGetters} from 'vuex'

  import allFans from './fanslist'

  export default {
    components:{
      allFans
    },
    data() {
      return {
        fanslist: '',
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          querykey:'',
          beforeDate: '',
          afterDate: '',
          pageNum: 1,//页码
          pageRow: 50,//每页条数
        },
        roles: [],//角色列表
        dialogStatus: 'create',
        dialogFormVisible: false,
        dialogFansVisible: false,
        textMap: {
          update: '编辑',
          create: '新建用户',
          fansOffset: '粉丝偏移量',
          fanslist: '粉丝列表'
        },
        tempUser: {
          userId: '',
          uuId: '',
          username: '',
          iconUrl: '',
          mobile: '',
          fansCount: '',
          fansOffset: '',
          sex: '',
          loginTime: '',
          modifyTime: '',
          querykey:'',
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
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
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
        this.listQuery.beforeDate = this.tempUser.dateValue[0];
        this.listQuery.afterDate = this.tempUser.dateValue[1];
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
      refreshList(){
        // this.queryInput = "";
        this.listLoading = true;
        this.tempUser.querykey="";
        this.tempUser.dateValue="";
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

      showfansOffset($index){
        let user = this.list[$index];
        this.tempUser.fansOffset = user.fansOffset;
        this.tempUser.uuId = user.uuId;
        this.tempUser.username = user.username;
        this.dialogStatus = "fansOffset";
        this.dialogFormVisible = true;
      },
      showfansList($index){
        let user = this.list[$index];
        this.tempUser.uuId = user.uuId;
        this.api({
          url: "/euser/fanslist",
          method: "get",
          params: this.tempUser
        }).then(data => {
          // this.listLoading = false;
          this.fanslist = data.list;


        })
        this.dialogFansVisible = true;
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
      checkFO(){
        let reg = /^\d{1,6}$/;
        let string1=String(this.tempUser.fansOffset).match(reg)
        if(!string1){
          document.getElementById("text").style.display="block";
        }else {
          document.getElementById("text").style.display="none";
        }
      },
      checkKey(){
        let reg = /[a-zA-Z0-9\u0391-\uFFE5]+$/;
        let flag = this.tempUser.querykey.match(reg);
        if(!flag){
          this.tempUser.querykey = '';
          document.getElementById("inputkey").placeholder="（下划线、特殊符号除外）";
        }else {
          document.getElementById("inputkey").placeholder="输入用户昵称关键字搜索";
        }
      }
    }
  }
</script>
