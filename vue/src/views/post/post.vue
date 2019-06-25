<style xmlns:30pxwidth="http://www.w3.org/1999/xhtml" xmlns:height="http://www.w3.org/1999/xhtml">
  .cell {
    height: 70px !important;
    overflow: auto !important;
    line-height: 70px !important;
  }
  .el-row {
    margin-bottom: 130px;
  }
</style>
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form class="small-space" inline :model="tempPost">
        <el-form-item>
          <el-input type="text" v-model="tempPost.queryText" placeholder="输入帖子内容搜索"/>
        </el-form-item>
        <el-form-item>
          <el-input type="text" v-model="tempPost.queryOwnewName" placeholder="输入发帖人昵称搜索"/>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="el-icon-search" @click="getList"></el-button>
        </el-form-item>
        <el-form-item>
          <el-date-picker v-model="tempPost.dataValue" type="daterange" align="right" unlink-panels validate-event
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
          <el-select v-model="tempPost.queryPostTypeId" filterable clearable placeholder="选择帖子类型" @change="getList">
            <el-option v-for="item in typeOption" :key="item.id" :label="'# '+item.sortname+' #'" :value="item.id">
            </el-option>
          </el-select>
          <el-form-item>
            <el-button type="primary" class="el-icon-close" @click="refashList"></el-button>
          </el-form-item>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="list" stripe default-sor v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row max-height = "100% ">
      <el-table-column align="center" label="序号" fixed="left" sortable width="100" prop="postId">
      </el-table-column>
      <el-table-column align="center" label="帖子类型" width="130">
        <template slot-scope="scope">
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==1"
                   color="#85D469"></el-tag>
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==2"
                   color="#0ACCCE"></el-tag>
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==3"
                   color="#FF90A3"></el-tag>
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==4"
                  color="#A29EDB"></el-tag>
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==5"
                   color="#0067C5"></el-tag>
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==6"
                   color="#F78700"></el-tag>
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==7"
                   color="#B4A294"></el-tag>
          <el-tag style="color:#fff;" v-text="'#'+scope.row.postType+'#'" size="medium" v-if="scope.row.postTypeId==8"
                   color="#798EA3"></el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="帖子内容" style="line-height:10px" width="300px">
        <template slot-scope="scope">
          <div v-text="scope.row.postText" style="line-height:25px">
          </div>
        </template>
      </el-table-column>

      <el-table-column align="center" label="发帖人昵称" prop="postOwnerName" width="100"></el-table-column>

      <el-table-column align="center" label="发帖时间" sortable prop="postTime" width="170"></el-table-column>

      <el-table-column align="center" label="点赞数/增加量" sortable prop="likeCount" width="140px">
        <template slot-scope="scope">
          <span style="font-size: 15px" v-text="scope.row.likeCount+'/'"></span>
          <el-button type="primary" icon="edit" v-text="scope.row.likeOffset" size="mini"
                     @click="showLikeOffset(scope.$index)"></el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" label="浏览数/增加量" sortable prop="browseOffset" width="140px">
        <template slot-scope="scope">
          <span v-text="scope.row.browseCount+'/'"></span>
          <el-button type="primary" icon="edit" v-text="scope.row.browseOffset" size="mini"
                     @click="showBrowseOffset(scope.$index)"></el-button>
        </template>
      </el-table-column>

      <el-table-column align="center" label="标签" style="width: 90px;" width="290">
        <template slot-scope="scope">
          <div v-for="posts in list" style="text-align: center">
            <el-tag v-for="tag in posts.postTagList" v-if="scope.row.postId==posts.postId" v-text="tag"
                    style="margin-right: 3px;" type="primary"/>
          </div>
        </template>
      </el-table-column>


      <el-table-column align="center" label="帖子详情" width="120" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showDetail(scope.$index)"
                     size="medium" class="el-icon-document">查看
          </el-button>
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" label="管理" width="350">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-arrow-up" size="medium" @click="showPostStick(scope.$index)">置顶</el-button>
          <el-button type="primary" icon="el-icon-edit" size="medium" @click="showUpdate(scope.$index)">修改</el-button>
          <el-button type="danger" icon="el-icon-delete" size="medium" v-if="scope.row.postState==0"
                     @click="removePost(scope.$index)">删除
          </el-button>
          <el-button type="success" icon="el-icon-refresh" size="medium" v-if="scope.row.postState==1"
                     @click="removePost(scope.$index)">恢复
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

    <el-dialog title="修改帖子内容" width="50%" :visible.sync="dialogUpdateVisible">
      <el-form class="small-space" :model="updateData" label-position="left" label-width="120px"
               style='width: 100%; margin-left:50px;'>
        <el-form-item label="帖子类型">
          <el-select v-model="updateData.postTypeId" filterable clearable :placeholder="'# '+tempPost.postType+' #'" :value="tempPost.postTypeId">
            <el-option :key="tempPost.postTypeId" :label="'# '+tempPost.postType+' #'" :value="tempPost.postTypeId" >{{'# '+tempPost.postType+' #'}}</el-option>
            <el-option-group>
              <el-option v-for="item in typeOption" :key="item.id" :label="'# '+item.sortname+' #'" :value="item.id">
              </el-option>
            </el-option-group>
          </el-select>
        </el-form-item>

        <el-form-item label="帖子内容" style="width: 80%" v-model="updateData.postText">
          <el-input type="textarea" :autosize="{ minRows: 4, maxRows: 8}" size="medium" v-model="tempPost.postText">{{tempPost.postText}}</el-input>
        </el-form-item>

        <el-form-item label="修改图片" size="medium" :inline="true">
          <div>
            <el-row style="margin-right: 280px">
              <el-col :span="8">
                <el-upload action="/post" list-type="picture-card" style="width: 30px!important; height: 30px!important;">
                  <i v-if="tempPost.postImgList[0]==null" class="el-icon-plus avatar-uploader-icon"></i>
                  <img v-else :src="tempPost.postImgList[0]" style="height: 100%;width: 100%" />
                </el-upload>
              </el-col>
              <el-col :span="8">
                <el-upload action="/post" list-type="picture-card" style="width: 30px!important; height: 30px!important;">
                  <i v-if="tempPost.postImgList[3]==null" class="el-icon-plus avatar-uploader-icon"></i>
                  <img v-else :src="tempPost.postImgList[3]" style="height: 100%;width: 100%" />
                </el-upload>
              </el-col>
              <el-col :span="8">
                <el-upload action="/post" list-type="picture-card" style="width: 30px!important; height: 30px!important;">
                  <i v-if="tempPost.postImgList[6]==null" class="el-icon-plus avatar-uploader-icon"></i>
                  <img v-else :src="tempPost.postImgList[6]" style="height: 100%;width: 100%" />
                </el-upload>
              </el-col>
            </el-row>

            <el-row style="margin-right: 280px">
              <el-col :span="8">
                <el-upload action="/post" list-type="picture-card" style="width: 30px!important; height: 30px!important;">
                  <i v-if="tempPost.postImgList[1]==null" class="el-icon-plus avatar-uploader-icon"></i>
                  <img v-else :src="tempPost.postImgList[1]" style="height: 100%;width: 100%" />
                </el-upload>
              </el-col>
              <el-col :span="8">
                <el-upload action="/post" list-type="picture-card" style="width: 30px!important; height: 30px!important;">
                  <i v-if="tempPost.postImgList[4]==null" class="el-icon-plus avatar-uploader-icon"></i>
                  <img v-else :src="tempPost.postImgList[4]" style="height: 100%;width: 100%" />
                </el-upload>
              </el-col>
              <el-col :span="8">
                <el-upload action="/post" list-type="picture-card" style="width: 30px!important; height: 30px!important;">
                  <i v-if="tempPost.postImgList[7]==null" class="el-icon-plus avatar-uploader-icon"></i>
                  <img v-else :src="tempPost.postImgList[7]" style="height: 100%;width: 100%" />
                </el-upload>
              </el-col>
            </el-row>

            <el-row style="margin-right: 280px">
              <el-col :span="8">
                <el-upload action="/post" list-type="picture-card" style="width: 30px!important; height: 30px!important;">
                  <i v-if="tempPost.postImgList[2]==null" class="el-icon-plus avatar-uploader-icon"></i>
                  <img v-else :src="tempPost.postImgList[2]" style="height: 100%;width: 100%" />
                </el-upload>
              </el-col>
              <el-col :span="8">
                <el-upload action="/post" list-type="picture-card" style="width: 30px!important; height: 30px!important;">
                  <i v-if="tempPost.postImgList[5]==null" class="el-icon-plus avatar-uploader-icon"></i>
                  <img v-else :src="tempPost.postImgList[5]" style="height: 100%;width: 100%" />
                </el-upload>
              </el-col>
              <el-col :span="8">
                <el-upload action="/post" list-type="picture-card" style="width: 30px!important; height: 30px!important;">
                  <i v-if="tempPost.postImgList[8]==null" class="el-icon-plus avatar-uploader-icon"></i>
                  <img v-else :src="tempPost.postImgList[8]" style="height: 100%;width: 100%" />
                </el-upload>
              </el-col>
            </el-row>

          </div>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogUpdateVisible = false">取 消</el-button>
       <el-button type="primary" @click="updatePost">确认修改</el-button>
      </div>
    </el-dialog>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" width="600px" lock-scroll>

      <!--详情页面组件-->
      <detail :addDetailData="postdetailDeta" v-if="dialogStatus=='postDetail'" ref="detail"></detail>

      <el-form class="small-space" :model="tempPost" label-position="left" label-width="80px"
               style='width: 400px; margin-left:50px;'>

        <!--点赞输入框-->
        <el-form-item label="输入数量" required v-if="dialogStatus=='likeOffset'">
          <el-input type="text" v-model="tempPost.likeOffset">
          </el-input>
        </el-form-item>

        <!--浏览量输入框-->
        <el-form-item label="输入数量" required v-if="dialogStatus=='browseOffset'">
          <el-input type="text" v-model="tempPost.browseOffset">
          </el-input>
        </el-form-item>

        <!--置顶多选框-->
        <el-form-item v-if="dialogStatus=='postStick'" label-width="0">
            <el-checkbox-group v-model="postStickValue" @change="handleCheckedCitiesChange">
              <div v-for="postStick in listStick">
                <el-checkbox  :label="postStick.stickId" v-if="postStick.stickState==0" :key="postStick.stickId">{{postStick.stickName}}</el-checkbox>
                <el-checkbox  :checked="true" :label="postStick.stickId" v-if="postStick.stickState==1" :key="postStick.stickId">{{postStick.stickName}}</el-checkbox>
              </div>
           </el-checkbox-group>

        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" v-if="dialogStatus=='browseOffset'" @click="updateBrowseOffset">修改浏览数量</el-button>
        <el-button type="primary" v-if="dialogStatus=='likeOffset'" @click="updateLikeOffset">修改点赞数量</el-button>
        <el-button type="primary" v-if="dialogStatus=='postStick'" @click="updatePostStick">修改置顶板块</el-button>
      </div>
    </el-dialog>
  </div>


</template>
<script>
  import {mapGetters} from 'vuex'
  import Detail from "./postdetail";

  export default {
    components: {Detail},
    data() {
      return {
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          beforeDate: '',//结束时间
          afterDate: '',//开始时间
          queryText: '',//返回内容搜索数据
          queryOwnewName: '',//返回昵称搜索数据
          queryPostTypeId: '',//返回类型搜索数据
          pageNum: 1,//页码
          pageRow: 10,//每页条数
        },
        postdetailDeta:'',
        typeOption: '',//帖子类型下拉框数据a
        listStick:[],//置顶列表数据
        dialogStatus: '',
        postStickData:{
          values:[],
          postId:''
        },
        postStickValue:[],
        dialogFormVisible: false,
        dialogUpdateVisible: false,
        textMap: {
          likeOffset: '增加点赞数量',
          browseOffset: '增加浏览数量',
          postDetail: '帖子详情',
          postStick: '置顶帖子',
          postUpdate: '修改帖子内容'
        },
        detailData:{
          postId: '',//帖子ID
        },
        updateData:{
          postId:'',
          postTypeId:'',
          postText:'',
          postImgList:''
        },
        tempPost: {
          postId: '',//帖子ID
          postOwnerId: '',//帖子ID
          postType: '',//帖子类型
          postText: '',//帖子内容
          likeOffset: '',//点赞偏移量
          browseOffset: '',//浏览偏移量
          likeCount: '',//点赞总数
          browseCount: '',//浏览总数
          postTypeId: '',//帖子类型ID
          queryText: '',//搜索输入框数据
          queryPostTypeId: '',//帖子类型下拉框选中的ID
          dataValue: '', //日期选择初始化
          postState: '',//帖子状态数据
          queryOwnewName: '',//搜索昵称输入框数据
          postDetialId: '',
          postImgList: [],//帖子图片数组
          postTagList: [],//帖子标签数组
          postPhone: '',//帖子电话
          postAddress: '',//帖子地址
          priceFloor: '',//最低价
          priceTop: '',//最高价
          commentText: '',//评论
          postStick: ''
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
      this.getCategoriesList();
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
        this.listQuery.queryOwnewName = this.tempPost.queryOwnewName;
        this.listQuery.queryPostTypeId = this.tempPost.queryPostTypeId;
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
      getCategoriesList(){
        this.api({
          url: "/sort/listSort",
          method: "get"
        }).then(data => {
          this.listLoading = false;
          this.typeOption = data.list;

        })
      },
      refashList() {
        this.tempPost.queryText = '';
        this.tempPost.dataValue = '';
        this.tempPost.queryOwnewName = '';
        this.tempPost.queryPostTypeId = '';
        this.getList();
      },
      showDetail($index) {
        //显示帖子详情窗口
        let post = this.list[$index];
        this.detailData.postId = post.postId;   //帖子Id
        console.log(this.detailData.postId)
        this.api({
          url: "/post/queryPostById",
          method: "post",
          params: this.detailData
        }).then(data => {
          this.postdetailDeta = data;
          this.dialogStatus = "postDetail";
          this.dialogFormVisible = true;
        }).catch(error => {

        })
        this.dialogStatus = "postDetail";
        this.dialogFormVisible = true;
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
      showLikeOffset($index) {
        //显示点赞修改框
        let post = this.list[$index];
        this.tempPost.likeOffset = post.likeOffset;
        this.tempPost.postId = post.postId;
        this.dialogStatus = "likeOffset";
        this.dialogFormVisible = true;
      },
      showBrowseOffset($index) {
        //显示浏览修改框
        let post = this.list[$index];
        this.tempPost.browseOffset = post.browseOffset;
        this.tempPost.postId = post.postId;
        this.dialogStatus = "browseOffset";
        this.dialogFormVisible = true;
      },
      showPostStick($index){
        this.postStickValue = []
        this.listStick = []
        let post = this.list[$index];
        this.detailData.postId = post.postId;
        this.api({
          url: "/stick/listStickByPostId",
          method: "post",
          params: this.detailData
        }).then(data =>{
          this.listLoading = false;
          this.listStick = data;
        })
        this.dialogStatus = 'postStick'
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        //显示修改框
        let post = this.list[$index];
        this.tempPost.postText = post.postText;
        this.updateData.postId = post.postId;
        this.tempPost.postType = post.postType;
        this.tempPost.postTypeId = post.postTypeId;
        this.tempPost.postImgList = post.postImgList;
        this.dialogUpdateVisible = true
      },
      updatePostStick($index){
        let post = this.list[$index];
        this.postStickData.values = this.postStickValue
        this.postStickData.postId = this.detailData.postId
        console.log(this.postStickData)
        this.api({
          url: "/stick/updatePostStick",
          method: "post",
          data: this.postStickData
        }).then(() => {
          let msg = "置顶成功";
          this.dialogFormVisible = false;
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
      updateLikeOffset() {
        //改变点赞数请求
        let _vue = this;
        this.api({
          url: "/post/updateLikeOffset",
          method: "post",
          data: this.tempPost
        }).then(() => {
          let msg = "修改成功";
          this.dialogUpdateVisible = false;
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
      handleCheckedCitiesChange(value) {
        this.postStickValue = value;
      },
      updateBrowseOffset() {
        //改变浏览数请求
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
      updatePost($index){
        let _vue = this;
        this.updateData.postText = this.tempPost.postText;
        this.updateData.postTypeId = this.tempPost.postTypeId;
        console.log(this.updateData)
        this.api({
          url: "/post/updatePost",
          method: "post",
          data: this.updateData
        }).then(() => {
          let msg = "修改成功";
          this.dialogUpdateVisible = false
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
        //改变帖子状态请求
        let _vue = this;
        let post = _vue.list[$index];
        let msg;
        if (post.postState == 0) {
          msg = '确定删除这个帖子?';
        } else if (post.postState == 1) {
          msg = '确定恢复这个帖子?';
        }
        this.$confirm(msg, '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          if (post.postState == 0) {
            post.postState = 1;
          } else if (post.postState == 1) {
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
            if (post.postState == 0) {
              defmsg = '删除失败';
            } else if (post.postState == 1) {
              defmsg = '恢复失败';
            }
            _vue.$message.error(defmsg)
          })
        })
      },
    }
  }
</script>
