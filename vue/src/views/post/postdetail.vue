<template>

  <div>

        <el-header>
          用户信息
        </el-header>
        <el-main>
          {{addDetailData.postText}}
        </el-main>
        <el-footer>
          <span>
            电话：{{addDetailData.postPhone}}
          </span>
          <br/>
          <span>
            地址：{{addDetailData.postAddress}}
          </span>
        </el-footer>
        <el-main>
          <span>
            <img :src="addDetailData.postImgList[0]" style="width: 130px;height: 130px"/>
            <img :src="addDetailData.postImgList[1]" style="width: 130px;height: 130px"/>
            <img :src="addDetailData.postImgList[2]" style="width: 130px;height: 130px"/>
          </span>
          <br/>
          <span>
            <img :src="addDetailData.postImgList[3]" style="width: 130px;height: 130px"/>
            <img :src="addDetailData.postImgList[4]" style="width: 130px;height: 130px"/>
            <img :src="addDetailData.postImgList[5]" style="width: 130px;height: 130px"/>
          </span>
          <br/>
          <span>
            <img :src="addDetailData.postImgList[6]" style="width: 130px;height: 130px"/>
            <img :src="addDetailData.postImgList[7]" style="width: 130px;height: 130px;border: none!important;"/>
            <img :src="addDetailData.postImgList[8]" style="width: 130px;height: 130px"/>
          </span>
        </el-main>
        <el-footer>
          <span>
            <el-tag v-for="tag in addDetailData.postTagList" v-text="tag" />
          </span>
        </el-footer>
        <el-header>
          <el-button type="text" style="font-size: 20px">评论</el-button>
        </el-header>
      <div v-for="text in addDetailData.comments" style="margin-left: 25px">
      <span v-if="addDetailData.comments.indexOf(text)==0"><el-button type="text">{{text.commentUserName}}</el-button>： {{text.commentText}}</span>

      <span v-else><el-button type="text">{{text.commentUserName}}</el-button> 回复 <el-button type="text">{{"@"+text.acceptUserName}}</el-button>： {{text.commentText}}</span>
      <br/>
    </div>
  </div>
</template>


<script>
  export default {
    name: 'Detail',
    props: ['addDetailData'],
    data() {
      return {
        detailData: {
          postId: ''
        }
      }
    },
    created() {
      console.log(this.addDetailData)
    },
    mounted() {

    },
    methods: {

    }
  }
</script>


<style scoped>
  .el-header {
    margin-left: -20px;
    color: #333;
    text-align: left;
    line-height: 20px;
  }

  .el-footer {
    color: #333;
    text-align: left;
    line-height: 20px;
  }

  .el-main {
    color: #333;
    text-align: left;
    line-height: 20px;
  }
</style>
