
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
       <Upload
         ref="upload"
         action="此处填写你访问的后台接口地址"
         :on-format-error="handleFormatError"
         :on-success="handleSuccess"
         :on-error="handleError"
         enctype="multipart/form-data"
         :format ="['xlsx','xls']">
         <Button type="primary" icon="ios-cloud-upload-outline">批量导入</Button>
       </Upload>

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
    data() {
      return {
        uploadBtnIcon: 'el-icon-upload2',
        enabledUploadBtn: true,
        dialogVisible: false,
        btnText: '数据导入',
        jl: {
          name: '',
          titlelevel: '中级'
        },
        jls: [],
        tls: [{
          value: '员级',
          label: '员级'
        }, {
          value: '初级',
          label: '初级'
        }, {
          value: '中级',
          label: '中级'
        }, {
          value: '副高级',
          label: '副高级'
        }, {
          value: '正高级',
          label: '正高级'
        }],
        value: ''
      }
    },
    mounted() {
      this.initJls();
    },
    methods: {

      onSuccess(response, file, fileList) {
        this.enabledUploadBtn = true;
        this.uploadBtnIcon = "el-icon-upload2";
        this.btnText = '数据导入';
        alert("数据导入成功！");
        this.initJls();
      },

      onError(err, file, fileList) {
        this.enabledUploadBtn = true;
        this.uploadBtnIcon = "el-icon-upload2";
        this.btnText = '数据导入';
        alert("数据导入失败！请检查是否有重复数据,和网络连接状况！");
      },
      beforeUpload(file) {
        this.enabledUploadBtn = false;
        this.uploadBtnIcon = "el-icon-loading";
        this.btnText = '正在导入';
      },
      initJls() {
        this.getRequest("/system/basic/jl/").then(resp => {
          if (resp) {
            this.jls = resp;
          }
        })
      }
    }
  }
</script>
