<template>

  <div>

      <span>{{addDetailData}}</span>
      <el-button @click="getDetailData">显示</el-button>




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
      console.log("传过来的数据"+this.addDetailData.postId)
    },
    mounted() {

    },
    methods: {

    }
  }
</script>


<style scoped>

</style>
