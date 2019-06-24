
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
      check-strictly="true"
      @node-click="handleNodeClick" >
    </el-tree>
    <el-form>
      <el-form-item>
        <el-button type="primary" icon="plus" @click="showCreate" v-if="hasPerm('article:add')">添加
        </el-button>
        <el-button @click="getData">获取数据</el-button>
        <el-button type="danger" icon="delete" @click="removeUser()">删除
        </el-button>
      </el-form-item>
    </el-form>
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form class="small-space" :model="tempArticle" label-position="left" label-width="60px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item label="文章">
          <el-input type="text" v-model="tempArticle.content">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createArticle">创 建</el-button>
        <el-button type="primary" v-else @click="updateArticle">修 改</el-button>
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
        tempArticle: {
          tid: "",
          tname: "",
          pid: ""
        }
      }
    },
    created() {
      this.getList();
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
          this.data = data.list;
          this.list = data.list;
        })
      },
      getData(){
        alert(this.$refs.tree.getCheckedKeys().concat(this.$refs.tree.getHalfCheckedKeys()));

      },
      handleNodeClick(data){
        // console.log(data)
        console.log(this.treeData)
      },
      showCreate() {
        //显示新增对话框
        this.tempArticle.content = "";
        this.dialogStatus = "create"
        this.dialogFormVisible = true
      },
      showUpdate($index) {
        //显示修改对话框
        this.tempArticle.id = this.list[$index].id;
        this.tempArticle.content = this.list[$index].content;
        this.dialogStatus = "update"
        this.dialogFormVisible = true
      },
      removeUser() {
        this.$confirm('确定删除此用户?', '提示', {
          confirmButtonText: '确定',
          showCancelButton: false,
          type: 'warning'
        }).then(() => {
          let temp = this.$refs.tree.getCheckedKeys().concat(this.$refs.tree.getHalfCheckedKeys());
          alert(temp);

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
