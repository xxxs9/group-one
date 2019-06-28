
<style>
  .el-tree-node__label{
    font-size: 25px;
    font-weight: bolder;
    font-family: "Helvetica Neue",Helvetica,"PingFang SC","Hiragino Sans GB","Microsoft YaHei","微软雅黑",Arial,sans-serif;

  }
</style>
<template>
  <div >
    <el-tree
      ref="tree"
      :data="treeData"
      :props="defaultProps"
      accordion
      show-checkbox
      node-key="id"

      @node-click="handleNodeClick" >
    </el-tree>
    <el-form>
      <form class="form-horizontal" enctype="multipart/form-data" >
        <br/>
        <br/>
        <label>请输入：</label>
          <input type="text" v-model="tempTag.queryText" placeholder="输入标签名字搜索"width="300"/>

          <el-button type="primary" class="el-icon-search" @click="getList"></el-button>

        <label>请选择excel表：</label>
        <input class="form-input" type="file" name="filename" @change="getFile($event)" width="300"></input>
        <el-button type="primary" id="my_file"  @click="uploadFileMethod($event)">增量添加</el-button>

        <el-button type="primary" icon="plus" @click="uploadFileCoverMethod($event)" >覆盖添加</el-button>
        <el-button type="danger" icon="delete" @click="removeUser()" v-if="hasPerm('post:delete')">删除
        </el-button>
      </form>
    </el-form>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="标签名称" prop="name" style="width: 60px;"></el-table-column>

      <el-table-column align="center" label="上级标签"  prop="parentId" width="100">
        <template slot-scope="scope">
          <el-tag  v-if="scope.row.rank==1" style="margin-right: 3px;" type="success">无</el-tag>
          <div v-if="scope.row.rank!=1" v-for="tags in list" style="text-align: center">
            <el-tag  v-if="scope.row.parentId == tags.id" style="margin-right: 3px;" type="danger" v-text="tags.name"></el-tag>

          </div>
        </template>
      </el-table-column>
      <!--<el-table-column align="center" label="等级" prop="rank" width="170"></el-table-column>-->
      <el-table-column align="center" label="管理" width="220">
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)">修改</el-button>
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

      <el-form class="small-space" :model="tempTag" label-position="left" label-width="80px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="上级标签 " required>
          <el-select v-model="tempTag.parentId" placeholder="请选择" >
            <el-option

            :key="beginTag.id"
            :label="beginTag.name"
            :value="beginTag.id">
            </el-option>
            <el-option
              v-for="item in typeOption"
              v-if="item.id != tempTag.parentId"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标签名称" required>
          <el-input type="text" v-model="tempTag.name">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateUser">修 改</el-button>
      </div>
    </el-dialog>
  </div>


</template>

<script>
  export default {
    name: "Test",
    data(){
      return {
        data : [],
        defaultProps: {
          children: 'children',
          label: 'name'
        },
        typeOption: '',
        totalCount: 0, //分页组件--数据总条数
        list: [],//表格的数据
        listLoading: false,//数据加载等待动画
        listQuery: {
          pageNum: 1,//页码
          pageRow: 50,//每页条数
          name: '',
          queryText: ''
        },
        dialogStatus: 'create',
        dialogFormVisible: false,
        textMap: {
          update: '编辑',
          create: '创建文章'
        },
        tempID:{
          id:""
        },
        tempTag: {
          id: "",
          name: "",
          parentId: "",
          queryText: ""
        },
        beginTag: {
          id: 0,
          name: '无',
          parentId: 1,
          rank: 1
        },
      }
    },
    created() {
      this.getList();
      this.getAllList();
    },
    computed:{
      treeData(){
        let cloneData = JSON.parse(JSON.stringify(this.data));    // 对源数据深度克隆
        return cloneData.filter(father=>{
          let branchArr = cloneData.filter(child=>father.id == child.parentId);   //返回每一项的子级数组
          branchArr.length>0 ? father.children = branchArr : '';   //如果存在子级，则给父级添加一个children属性，并赋值
          return father.parentId==0;      //返回第一层
        });
      }
    },
    methods:{
      getAllList() {
        //查询列表
        if (!this.hasPerm('article:list')) {
          return
        }
        this.api({
          url: "/tag/listAllTag",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.data = data.list;
          this.typeOption = data.list;
        })
      },
      getList() {
        //查询列表
        this.listQuery.queryText = this.tempTag.queryText;

        if (!this.hasPerm('article:list')) {
          return
        }
        this.listLoading = true;
        this.api({
          url: "/tag/listTag",
          method: "get",
          params: this.listQuery
        }).then(data => {
          this.listLoading = false;
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
      getIndex($index) {
        //表格序号
        return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
      },
      showUpdate($index) {

        this.tempTag.id = this.list[$index].id;
        this.tempTag.name = this.list[$index].name;
        this.tempTag.parentId = this.list[$index].parentId;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },

      handleNodeClick(data){
        // console.log(data)
        console.log(this.treeData)
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
         this.tempTag.parentId = "";
        this.tempTag.content = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },


      updateUser() {
        //修改用户信息
        let _vue = this;
        this.api({
          url: "/tag/updateTag",
          method: "post",
          data: this.tempTag
        }).then(() => {
          let msg = "修改成功";
          this.dialogFormVisible = false
          this.$message({
            message: msg,
            type: 'success',
            duration: 1 * 1000,
            onClose: () => {
              _vue.getList();
              _vue.getAllList();
            }
          })
        })
      },
      removeUser() {
        this.$confirm('确定删除此用户?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let temp = this.$refs.tree.getCheckedKeys();


          this.tempID.id = temp;


          this.api({
            url: "/tag/deleteTag",
            method: "post",
            data: this.tempID,
          }).then(() => {
            this.getAllList();
            this.getList();


          }).catch(() => {
            this.$message.error("未选定,删除失败")
          })
        })
      },
      getFile(event) {
        this.file = event.target.files[0];
        console.log(this.file);
      },
      uploadFileMethod(event){
        if(this.file == undefined){
          this.$message.error("文件为空");

        }else {
          event.preventDefault();
          let _vue = this;
          let formdata = new FormData();
          formdata.append('filename', this.file);
          let headers = {headers: {"Content-Type": "multipart/form-data"}}
          this.api.post("/tag/importTag",formdata,headers).then(function(data){

            _vue.getAllList();
            _vue.getList();
          },function(err){
            console.log("err------: ");
            console.log(err);
          })
        }


      },
      uploadFileCoverMethod(event){
        if(this.file == undefined){
          this.$message.error("文件为空");

        }else {
          event.preventDefault();
          let _vue = this;

          let formdata = new FormData();
          formdata.append('filename', this.file);
          let headers = {headers: {"Content-Type": "multipart/form-data"}}
          this.api.post("/tag/importCoverTag",formdata,headers).then(function(data){
            _vue.getAllList();
            _vue.getList();
          },function(err){
            console.log("err------: ");
            console.log(err);
          })
        }


      }
    },


  }
</script>
