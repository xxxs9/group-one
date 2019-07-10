<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-form class="small-space" :inline="true" :model="tempPerm">
            <el-form-item>
              <el-input type="text"  v-model="tempPerm.querykey" placeholder="输入用户昵称关键字搜索"/>
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
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <!--<el-table-column align="center" label="用户ID" prop="userId" width="150"></el-table-column>-->
      <!--<el-table-column align="center" label="UUID" prop="uuId" width="150"></el-table-column>-->
      <el-table-column align="center" label="用户昵称" prop="username"></el-table-column>
      <el-table-column align="center" label="用户权限" width="420">
        <template slot-scope="scope">
          <div v-for="eperms in list">
            <el-tag v-for="eperm in eperms.epermissionList" v-if="scope.row.uuId==eperms.uuId" v-text="eperm" :key="eperm.uuId"
                    style="margin-right: 3px;"
                    type="primary">
            </el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" width="350" v-if="hasPerm('euser:update')" >
        <template slot-scope="scope">
          <el-button type="primary" size="medium" icon="el-icon-edit" @click="showUpdate(scope.$index)" >修改
          </el-button>
          <el-button type="danger" size="medium" icon="el-icon-circle-close" @click="removePerm(scope.$index)" >禁用
          </el-button>
          <el-button type="success" size="medium" icon="el-icon-refresh" @click="refreshPerm(scope.$index)" >恢复
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
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="tempPerm" label-position="left" label-width="100px"
               style='width: 600px; margin-left:50px;' >
        <!--<el-form-item label="UUID" required>-->
          <!--<el-input type="text" v-model="tempPerm.uuId" style="width: 250px;" disabled="true">-->
          <!--</el-input>-->
        <!--</el-form-item>-->
        <el-form-item label="用户昵称" required>
          <el-input type="text" v-model="tempPerm.username" style="width: 250px;" disabled="true">
          </el-input>
        </el-form-item>
        <el-form-item label="用户权限" required>

            <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
            <el-checkbox-group v-model="permlist" @change="handleCheckedPermChange">
              <div v-for="eperms in perms">
                <el-checkbox :checked="true" v-if="eperms.state==1" :label="eperms" :key="eperms">{{eperms.permName}}
                </el-checkbox>
                <el-checkbox :checked="false" v-if="eperms.state==0" :label="eperms" :key="eperms">{{eperms.permName}}
                </el-checkbox>
              </div>
            </el-checkbox-group>

        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='update'" type="primary" @click="updatePerm">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>

  export default {
    data() {
      return {
        list: [],//表格的数据
        totalCount: 0, //分页组件--数据总条数
        listQuery: {
          querykey:'',
          pageNum: 1,//页码
          pageRow: 50,//每页条数
        },
        checkAll: false,
        permlist : [],
        perms: [],
        isIndeterminate: true,
        listLoading: false,//数据加载等待动画
        dialogStatus: 'update',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
        },
        tempPerm: {
          userId: '',
          uuId: '',
          username: '',
          querykey: '',
          epermissionList: [],

        },
        tempUUId: {
          uuId: '',
        }
      }
    },
    created() {
      this.getList();
      // this.getPerm();
    },
    methods: {
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      getList() {
        //查询列表
        this.listQuery.querykey = this.tempPerm.querykey;
        this.listLoading = true;
        this.api({
          url: "/euser/userPerm",
          method: "get",
          params: this.listQuery,
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
      refleshList(){
        this.listLoading = true;
        this.tempPerm.querykey="";
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
      showUpdate($index) {
        let perms = this.list[$index];
        this.tempPerm.uuId = perms.uuId;
        this.tempUUId.uuId = perms.uuId;
        this.perms = [];
        this.permlist = [];
        console.log(perms.uuId);
        this.tempPerm.username = perms.username;
        // this.tempPerm.epermissionList = perms.epermissionList;
        // this.permlist = perms.epermissionList;
        this.api({
          url: "/euser/perms",
          method: "get",
          params: this.tempUUId,
        }).then(data => {
          this.listLoading = false;
          this.perms = data.list;
          console.log(this.perms);
        })
        // console.log(this.tempPerm.epermissionList);

        this.dialogStatus = "update";
        this.dialogFormVisible = true;

      },
      handleCheckAllChange(val) {
        console.log(val);

        console.log(this.perms);

        this.permlist = val ? this.perms : [];
        console.log(this.permlist);
        this.tempPerm.epermissionList = this.permlist;
        console.log(this.tempPerm.epermissionList);
        this.isIndeterminate = false;
      },
      handleCheckedPermChange(value) {
        let checkedCount = value.length;
        // console.log(value===this.permlist);

        console.log(value);
        this.tempPerm.epermissionList = value;
        console.log(this.tempPerm.epermissionList);
        this.checkAll = checkedCount === this.perms.length;
        this.isIndeterminate = checkedCount > 0 && checkedCount < this.perms.length;
      },
      updatePerm(){
        //删除用户权限
        let _vue = this;
        this.api({
          url: "/euser/updatePerm",
          method: "post",
          data: this.tempPerm
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          // if (this.userId === this.tempUser.userId) {
          //   msg = '修改成功,部分信息重新登录后生效'
          // }
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
      refreshPerm($index){
        //恢复用户权限
        let _vue = this;
        let perms = this.list[$index];
        this.tempPerm.uuId = perms.uuId;
        this.api({
          url: "/euser/refreshPerm",
          method: "post",
          data: this.tempPerm
        }).then(() => {
          let msg = "恢复成功";
          this.dialogFormVisible = false;
          // if (this.userId === this.tempUser.userId) {
          //   msg = '修改成功,部分信息重新登录后生效'
          // }
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
      removePerm($index){
        //禁用用户权限
        let _vue = this;
        let perms = this.list[$index];
        this.tempPerm.uuId = perms.uuId;
        this.api({
          url: "/euser/removePerm",
          method: "post",
          data: this.tempPerm
        }).then(() => {
          let msg = "已禁用所有权限";
          this.dialogFormVisible = false
          // if (this.userId === this.tempUser.userId) {
          //   msg = '修改成功,部分信息重新登录后生效'
          // }
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

    }
  }
</script>
<style scoped>
  .requiredPerm {
    color: #ff0e13;
  }
</style>
