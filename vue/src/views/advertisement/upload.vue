<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-form class="small-space" inline="true" :model="tempAdvertisement">
            <el-form-item>
              <el-button type="primary" icon="plus"  @click="showCreate">添加
              </el-button>
            </el-form-item>
            <el-form-item>
              <el-input type="text" v-model="tempAdvertisement.name" placeholder="输入评论内容搜索"/>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" class="el-icon-search" @click="getList">搜索</el-button>
            </el-form-item>
          </el-form>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column align="center" label="序号" style="width: 60px;">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="广告ID" prop="advertisementId" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="广告类型" prop="advertisementType" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="广告链接" prop="advertisementRef" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="图片路径" style="width: 60px;">
        <template slot-scope="scope">
          <img :src="scope.row.srcUrl" width="160" height="80" class="head_pic" style="display: block"/>
        </template>
      </el-table-column>
      <el-table-column align="center" label="广告状态" prop="advertisementStatus" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="管理" width="220" v-if="hasPerm('user:update')">
        <template slot-scope="scope">
          <el-button type="warning" v-if="" icon="el-icon-edit" @click="showUpdate(scope.$index)">修改</el-button>
          <el-button type="primary" icon="el-icon-view" v-if="scope.row.advertisementStatus==0" @click="removeAdvertisement(scope.$index)">显示</el-button>
          <el-button type="info" icon="el-icon-delete" v-if="scope.row.advertisementStatus==1" @click="removeAdvertisement(scope.$index)">隐藏</el-button>
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
        <el-form-item label="类型">
          <el-input type="text" v-model="tempAdvertisement.advertisementType">
          </el-input>
        </el-form-item>
        <el-form-item label="链接">
          <el-input type="text" v-model="tempAdvertisement.advertisementRef">
          </el-input>
        </el-form-item>
        <el-form-item label="上传" required>
          <el-upload
            ref="upload"
            action="/api/src/upload"
            list-type="picture-card"
            :on-preview="handlePictureCardPreview"
            :on-success="handleAvatarSuccess"
            :on-remove="handleRemove">
            <img v-if="dialogStatus=='update'" :src="tempAdvertisement.srcUrl" style="width: 100%;height:100%;" ref="updateImg"/>
            <i v-if="dialogStatus=='create'" class="el-icon-plus"></i>
          </el-upload>
          <el-dialog :visible.sync="dialogVisible" >
            <img width="100%" :src="dialogImageUrl" alt="" ref="dialogImage">
          </el-dialog>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createAdvertisement">添 加</el-button>
        <el-button type="primary" v-if="dialogStatus=='update'" @click="updateAdvertisement">修 改</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
  import {mapGetters} from 'vuex'

  export default {
    data() {
      return {
        imgData: {
          desFilePath: '',
          url:''
        },
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          name: '',
          src: '',
          pageNum: 1,//页码
          pageRow: 50,//每页条数
        },
        roles: [],//角色列表
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '修改广告',
          create: '新增广告'
        },
        tempAdvertisement: {
          advertisementId: '',
          advertisementType: '',
          advertisementRef: '',
          srcUrl: '',
          advertisementStatus: '',
          url: '',
          imgList:[]
        },
        dialogImageUrl: '',
        dialogVisible: false
      }
    },
    created() {
      this.getList();
      if (this.hasPerm('advertisement:add') || this.hasPerm('advertisement:update')) {
      }
    },
    computed: {
      ...mapGetters([
        'advertisementId'
      ])
    },
    methods: {
      getList() {
        //查询列表
        this.listLoading = true;
        this.listQuery.name = this.tempAdvertisement.name;
        console.log(this.listQuery)
        this.api({
          url: "/src/list",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          console.log(data)
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
      showCreate() {
        //显示新增对话框
        this.tempAdvertisement.advertisementType = "";
        this.tempAdvertisement.advertisementStatus = "";
        this.tempAdvertisement.advertisementId = "";
        this.tempAdvertisement.advertisementRef = "";
        this.tempAdvertisement.srcUrl = "";
        this.dialogStatus = "create";
        this.dialogFormVisible = true
        this.$refs.upload.clearFiles();

      },
      showUpdate($index) {
        let advertisement = this.list[$index];
        this.tempAdvertisement.advertisementType = advertisement.advertisementType;
        this.tempAdvertisement.advertisementRef = advertisement.advertisementRef;
        this.tempAdvertisement.srcUrl = advertisement.srcUrl;
        this.tempAdvertisement.advertisementStatus = advertisement.advertisementStatus;
        this.tempAdvertisement.advertisementId = advertisement.advertisementId;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      createAdvertisement() {
        //添加新用户
        this.api({
          url: "/src/add",
          method: "post",
          data: this.tempAdvertisement
        }).then(() => {
          this.getList();
          this.dialogFormVisible = false;
          this.$refs.upload.clearFiles();
          this.tempAdvertisement.imgList = [];
        })
      },
      updateAdvertisement() {
        //修改用户信息
        let _vue = this;
        this.api({
          url: "/src/update",
          method: "post",
          data: this.tempAdvertisement
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          this.tempAdvertisement.imgList = [];
          if (this.advertisementId === this.tempAdvertisement.advertisementId) {
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
      removeAdvertisement($index) {
        let _vue = this;
        this.$confirm('确定改变状态?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let advertisement = _vue.list[$index];
          if (advertisement.advertisementStatus == 0) {
            advertisement.advertisementStatus = '1';
          } else {
            advertisement.advertisementStatus = '0';
          }
          _vue.api({
            url: "/src/remove",
            method: "post",
            data: advertisement
          }).then(() => {
            _vue.getList()
          }).catch(() => {
            _vue.$message.error("删除失败")
          })
        })
      },

      handleRemove(file, fileList) {
        let list = this.tempAdvertisement.imgList;
        this.imgData.desFilePath = file.response.desFilePath;
        this.imgData.url = file.response.url;
        this.api({
          url: "/src/delete",
          method: 'post',
          data: this.imgData
        }).then(() => {
          for(let i =0;i<list.length;i++){
            if(file.response.url==list[i]){
              list.splice(i,1);
            }
          }
        })
      },

      handleAvatarSuccess(response, file, fileList) {
        //response这个
        this.imgData.desFilePath = response.desFilePath;
        this.tempAdvertisement.imgList.push(response.url);
        console.log(response.url);
        console.log("传回的地址：" + response.desFilePath)
        // this.$ref.upload.clearFiles();
        this.$refs.updateImg.style.display = 'none';
        this.$refs.upload.clearFiles();
      },
      handlePictureCardPreview(file) {
        this.dialogImageUrl = file.url;
        this.dialogVisible = true;
      }
    }
  }
</script>
