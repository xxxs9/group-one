
<style>
  .el-tree-node__label{
    font-size: 25px;
    font-weight: bolder;
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
      <el-form-item>
        <el-button type="primary" icon="plus" @click="showCreate" v-if="hasPerm('post:add')">添加
        </el-button>
        <el-button type="primary" icon="plus" @click="showCreate" v-if="hasPerm('article:add')">批量添加
        </el-button>
        <el-button @click="getData">获取数据</el-button>
        <el-button type="danger" icon="delete" @click="removeUser()" v-if="hasPerm('post:delete')">删除
        </el-button>
      </el-form-item>
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
            key="0"
            label="无"
            value="0"
            ></el-option>
            <el-option
              v-for="item in typeOption"
              :key="item.id"
              :label="item.name"
              :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标签名称" required>
          <el-input type="text" v-model="tempTag.tname">
          </el-input>
        </el-form-item>
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
          name: ''
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
          parentId: ""
        }
      }
    },
    created() {
      this.getList();
      this.getAllList();
    },
    computed:{
      treeData(){
        let cloneData = JSON.parse(JSON.stringify(this.data))    // 对源数据深度克隆
        return cloneData.filter(father=>{
          let branchArr = cloneData.filter(child=>father.id == child.parentId)    //返回每一项的子级数组
          branchArr.length>0 ? father.children = branchArr : ''   //如果存在子级，则给父级添加一个children属性，并赋值
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
        this.listLoading = true;
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
        let sort = this.list[$index];
        alert(sort.imageUrl);
        this.tempUser.id = sort.id;
        this.tempUser.sortname = sort.sortname;
        this.tempUser.imageUrl = sort.imageUrl;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      getData(){
        // alert(this.$refs.tree.getCheckedKeys().concat(this.$refs.tree.getHalfCheckedKeys()));
        // console.log(this.$refs.tree.getCheckedKeys());
        alert(this.$refs.tree.getCheckedKeys())
      },
      handleNodeClick(data){
        // console.log(data)
        console.log(this.treeData)
      },
      showCreate() {
        //显示新增对话框
         this.tempTag.parentId = "";
        this.tempTag.content = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        //显示修改对话框
        this.tempTag.id = this.list[$index].id;
        this.tempTag.content = this.list[$index].content;
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
            this.getList()
          }).catch(() => {
            this.$message.error("删除失败")
          })
        })
      },
    },


  }
</script>
