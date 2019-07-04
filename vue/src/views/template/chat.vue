<template>
  <form class="form-horizontal" enctype="multipart/form-data" >
    <br/>
    <br/>
    <button type="button" id="my_file" class="btn btn-primary" @click="uploadFileMethod($event)">导入</button>
    <input class="form-input" type="file" name="filename" @change="getFile($event)"></input>
    <el-button type="primary" icon="plus" @click="uploadFileCoverMethod($event)" >批量添加</el-button>
    <el-button type="primary" icon="plus" @click="getLike" >dianzan</el-button>

  </form>
</template>

<script>

  export default {
    name: 'productUpload',
    data() {
      return {
        imageUrl: '',
        file: '',
        tempUser: {
          id: '',
          selectId:'',
          sortname: '',
          imageUrl: '',
        },
        tempLike: {
          userId: 10001,
        }
      }
    },

    methods: {
      // 文件上传
      getFile(event) {
        this.file = event.target.files[0];
        console.log(this.file);
      },
      uploadFileMethod(event){
        event.preventDefault();
        alert(1);
        let formdata = new FormData();
        formdata.append('filename', this.file);
        let headers = {headers: {"Content-Type": "multipart/form-data"}}
        this.api.post("/tag/importTag",formdata,headers).then(function(data){
          console.log(data);
        },function(err){
          console.log("err------: ");
          console.log(err);
        })

      },
      getLike() {
        //查询列表

        this.api({
          url: "/api/like/getThumbsUp",
          method: "post",
          params: this.tempLike
        }).then(data => {
          console.log(data);
          this.list = data.list;
          this.totalCount = data.totalCount;
        })
      },
      uploadFileCoverMethod(event){
        event.preventDefault();
        alert(2);
        let formdata = new FormData();
        formdata.append('filename', this.file);
        let headers = {headers: {"Content-Type": "multipart/form-data"}}
        this.api.post("/tag/importCoverTag",formdata,headers).then(function(data){
          console.log(data);
        },function(err){
          console.log("err------: ");
          console.log(err);
        })

      }
    }
  }
</script>

