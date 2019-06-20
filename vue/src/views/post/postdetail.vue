<template>
  <div>
    <p>{{postDetialId}}</p>
  </div>
</template>


<script>
  export default{
    name: 'Detail',
    props:['postDetialId'],
    data(){
      return{
        tempDetail:{
          postId: ''
        }
      }
    },
    created(){
    },
    methods:{

    }
  }
</script>


<style scoped>

</style>
