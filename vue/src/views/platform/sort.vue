<template>

  <div class="app-container">

    <div class="filter-container">


      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus"  @click="showCreate">添加
          </el-button>

          <el-select v-model="tempUser.selectId" placeholder="请选择"  @change="getList">
            <el-option
              v-for="item in typeOption"

              :key="item.id"
              :label="item.sortname"
              :value="item.id">
            </el-option>
          </el-select>
          <el-button type="primary" class="el-icon-close" @click="refashList"></el-button>

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
      <el-table-column align="center" label="类别名称" prop="sortname" style="width: 60px;"></el-table-column>
      <el-table-column label="头像" width="100">
        <template slot-scope="scope">
          <img :src="scope.row.imageUrl" width="40" height="40" class="head_pic"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" width="170"></el-table-column>
      <el-table-column align="center" label="最近修改时间" prop="updateTime" width="170"></el-table-column>
      <el-table-column align="center" label="标签"   style="width: 90px;" width="290">
        <template slot-scope="scope">
          <div  style="text-align: center">
            <el-tag  v-if="scope.row.status=='1'" style="margin-right: 3px;" type="success">显示</el-tag>
            <el-tag  v-if="scope.row.status=='2'" style="margin-right: 3px;" type="danger">隐藏</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column align="center" label="管理" width="220">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)" v-if="hasPerm('post:update')">修改</el-button>
          <el-button type="danger" v-if="scope.row.status=='1'" icon="delete" @click="removeSort(scope.$index)">删除
          </el-button>
          <el-button type="success" v-if="scope.row.status=='2'" icon="edit" @click="recoverySort(scope.$index)">恢复</el-button>

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
        <el-form-item label="分类名称" required>
          <el-input type="text" v-model="tempUser.sortname">
          </el-input>
        </el-form-item>
        <el-upload
          class="avatar-uploader"
          action="/api/sort/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload">
          <img v-if="dialogStatus=='update'"  :src="tempUser.imageUrl" class="avatar">
          <img v-else-if="imageUrl"  :src="tempUser.imageUrl" class="avatar">

          <i v-else="dialogStatus=='create'" class="el-icon-plus avatar-uploader-icon"></i>
        </el-upload>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createUser">创 建</el-button>
        <el-button type="primary" v-else @click="updateUser">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {mapGetters} from 'vuex'

  export default {
    data() {
      return {
        imageUrl: '',
        testname: '',
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          selectId: '',
          pageNum: 1,//页码
          pageRow: 50,//每页条数
        },
        typeOption: '',//帖子类型下拉框数据a
        roles: [],//角色列表
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '新建用户'
        },
        tempUser: {
          selectId:'',
          id: '',
          sortname: '',
          imageUrl: ''
        }
      }
    },
    created() {
      this.getList();
      this.getCategoriesList();
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
      handleAvatarSuccess(res, file) {
        this.imageUrl = URL.createObjectURL(file.raw);
        this.tempUser.imageUrl = this.imageUrl;

      },
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG) {
          this.$message.error('上传头像图片只能是 JPG 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return isJPG && isLt2M;
      },
      getList() {
        //查询列表
        this.listLoading = true;
        this.listQuery.selectId = this.tempUser.selectId;
        this.api({
          url: "/sort/listSort",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
      refashList() {
        this.tempUser.selectId = '';

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
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      getCategoriesList(){
        this.api({
          url: "/sort/listSort",
          method: "get"
        }).then(data => {
          this.listLoading = false;
          this.typeOption = data.list;

        })
      },
      showCreate() {
        //显示新增对话框
        this.tempUser.sortname = "";
        this.tempUser.imageUrl = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        let sort = this.list[$index];
        alert(sort.imageUrl);
        this.tempUser.id = sort.id;
        this.tempUser.sortname = sort.sortname;
        this.tempUser.imageUrl = sort.imageUrl;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createUser() {
        //添加新用户
        this.api({
          url: "/sort/addSort",
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
          url: "/sort/updateSort",
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
      removeSort($index) {
        let _vue = this;
        this.$confirm('确定删除此用户?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let user = _vue.list[$index];

          _vue.api({
            url: "/sort/deleteSort",
            method: "post",
            data: user
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            _vue.$message.error("删除失败")
          })
        })
      },
      recoverySort($index) {
        //修改用户信息
        let _vue = this;
        let sort = _vue.list[$index];
        this.api({
          url: "/sort/recoverySort",
          method: "post",
          data: sort
        }).then(() => {
          _vue.getList();
        })
      },
    }
  }
</script>
<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
