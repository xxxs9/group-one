<template>
  <el-collapse>
    <!--<el-collapse-item title="用户信息" name="1">-->
      <!--<div style="margin-left: 20px">-->
        <!--<span style="line-height: 30px!important;"><img :src="addDetailData.externalUser.iconUrl" width="35px" height="35px"/>{{addDetailData.externalUser.username}}</span>-->
      <!--</div>-->
    <!--</el-collapse-item>-->

    <el-collapse-item title="帖子正文和图片" name="2">
      <div style="margin-left: 20px">
        <div>
          {{addDetailData.postText}}
        </div>
        <br/>
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
      </div>
    </el-collapse-item>

    <el-collapse-item title="其他信息" name="3">
      <div style="margin-left: 20px">
        <span>电话：{{addDetailData.postPhone}}</span>
        <br/>
        <span>地址：{{addDetailData.postAddress}}</span>
        <br/>
        <span>帖子类型：{{"#"+addDetailData.postType+"#"}}</span>
        <br/>
        <span style="margin-top: 10px">
            <el-tag v-for="tag in addDetailData.postTagList" v-text="tag" />
        </span>
      </div>
    </el-collapse-item>

    <!--<el-collapse-item title="评论" name="4">-->
      <!--<div v-for="text in addDetailData.comments" style="margin-left: 25px">-->
        <!--<span v-if="addDetailData.comments.indexOf(text)==0"><el-button type="text">{{text.commentUserName}}</el-button>： {{text.commentText}}</span>-->
        <!--<span v-else><el-button type="text">{{text.commentUserName}}</el-button> 回复 <el-button type="text">{{"@"+text.acceptUserName}}</el-button>： {{text.commentText}}</span>-->
        <!--<br/>-->
      <!--</div>-->
    <!--</el-collapse-item>-->
  </el-collapse>
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
