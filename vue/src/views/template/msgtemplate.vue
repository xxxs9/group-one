<style>
  .cell{height: 100px }
</style>
<template>
  <div class="app-container">
    <div class="filter-container">
      <el-form>
        <el-form-item>
          <el-button type="primary" icon="plus" @click="showCreate" v-if="hasPerm('message:add')">添加
          </el-button>
        </el-form-item>
      </el-form>

    </div>
    <el-table :data="list" v-loading.body="listLoading" element-loading-text="拼命加载中" border fit
              highlight-current-row>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="scope">
          <span v-text="getIndex(scope.$index)"> </span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="tname" label="名字" style="width: 60px;"></el-table-column>
      <el-table-column align="center" prop="content" label="内容" style="width: 60px;"></el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" width="170"></el-table-column>
      <el-table-column align="center" label="最近修改时间" prop="updateTime" width="170"></el-table-column>
      <el-table-column align="center" label="管理" >
        <template slot-scope="scope">
          <el-button type="primary" icon="edit" @click="showUpdate(scope.$index)" v-if="hasPerm('message:update')">修改</el-button>
          <el-button type="danger" icon="delete" @click="removeTemplate(scope.$index)" v-if="hasPerm('message:delete')">删除</el-button>
          <el-button type="danger" icon="delete" @click="sendAlltest(scope.$index)">发送所有人</el-button>

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
      <el-form class="small-space" :model="tempTemplate" label-position="left" label-width="60px"
               style='width: 300px; margin-left:50px;'>
        <el-form-item  v-if="dialogStatus=='create'" label="名字">
          <el-input type="text" v-model="tempTemplate.tname" style="width: 500px ">
          </el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input type="textarea" v-model="tempTemplate.content"    placeholder="请输入内容"
                    maxlength="100" show-word-limit style="width: 500px ">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="createArticle">创 建</el-button>
        <el-button type="primary" v-if="dialogStatus=='update'" @click="updateArticle">修 改</el-button>
        <el-button type="primary" v-if="dialogStatus=='send'" @click="sendAll">发送</el-button>

      </div>
    </el-dialog>
  </div>
</template>

<script>
    export default {
      data() {
        return {
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
            create: '创建文章',
            send: '发送'
          },
          tempTemplate: {
            id: "",
            tname: "",
            content: ""
          }
        }
      },
      created() {
        this.getList();
      },
      methods: {
        getList() {
          //查询列表

          this.listLoading = true;
          this.api({
            url: "/template/listTemplate",
            method: "get",
            params: this.listQuery
          }).then(data => {
            this.listLoading = false;
            this.list = data.list;
            this.totalCount = data.totalCount;
          })
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
        getIndex($index) {
          //表格序号
          return (this.listQuery.pageNum - 1) * this.listQuery.pageRow + $index + 1
        },
        showCreate() {
          //显示新增对话框
          this.tempTemplate.tname = "";
          this.tempTemplate.content = "";
          this.dialogStatus = "create"
          this.dialogFormVisible = true
        },
        showUpdate($index) {
          //显示修改对话框
          this.tempTemplate.id = this.list[$index].id;
          this.tempTemplate.tname = this.list[$index].tname;
          this.tempTemplate.content = this.list[$index].content;
          this.dialogStatus = "update"
          this.dialogFormVisible = true
        },
        sendAlltest($index) {
          //显示修改对话框
          this.tempTemplate.id = this.list[$index].id;
          this.tempTemplate.tname = this.list[$index].tname;
          this.tempTemplate.content = this.list[$index].content;
          this.dialogStatus = "send"
          this.dialogFormVisible = true
        },
        createArticle() {
          //保存新文章
          this.api({
            url: "/template/addTemplate",
            method: "post",
            data: this.tempTemplate
          }).then(() => {
            this.getList();
            this.dialogFormVisible = false
          })
        },
        updateArticle() {
          //修改文章
          this.api({
            url: "/template/updateTemplate",
            method: "post",
            data: this.tempTemplate
          }).then(() => {
            this.getList();
            this.dialogFormVisible = false
          })
        },
        sendAll() {
          //修改文章
          this.api({
            url: "/template/sendTemplate",
            method: "post",
            data: this.tempTemplate
          }).then(() => {
            this.getList();
            this.dialogFormVisible = false
          })
        },
        removeTemplate($index) {
          let _vue = this;
          this.$confirm('确定删除此模板?', '提示', {
            confirmButtonText: '确定',
            showCancelButton: false,
            type: 'warning'
          }).then(() => {
            let user = _vue.list[$index];

            _vue.api({
              url: "/template/deleteTemplate",
              method: "post",
              data: user
            }).then(() => {
              _vue.getList()
            }).catch(() => {
              _vue.$message.error("删除失败")
            })
          })
        },
      }
    }
</script>

<style scoped>

</style>
