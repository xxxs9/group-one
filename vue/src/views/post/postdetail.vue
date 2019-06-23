<template>
  <div>
      <span>
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
          <el-button type="text">评论</el-button>
        </el-header>
        <el-main v-for="text in addDetailData.commentText">
          <span>评论用户 回复 被评论用户： <el-tag v-text="text" /></span>
        </el-main>
      </span>
  </div>
</template>


<script>
  export default {
    name: 'Detail',
    props: ['addDetailData'],
    data() {
      return {

      }
    },
    created() {
      console.log("传过来的数据"+this.addDetailData.postId)
    },ods: {

    }
  }
</script>


<style scoped>
  .el-header {
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
