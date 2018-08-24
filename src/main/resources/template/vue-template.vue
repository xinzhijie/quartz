<template>
  <el-card class="box-card" >
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="姓名" v-model="listQuery.name"></el-input>
      <el-button class="filter-item" type="primary" icon="search" @click="handleFilter" title="搜索">搜索</el-button>
      <el-button class="filter-item" type="primary" icon="plus" title="新增" @click="handleCreate"  style="margin-left:10px;">新增</el-button>
    </div>
    <el-card  :body-style="{ padding: '6px' }">
      <el-table
        :data="list"
        style="width: 100%;">
        <el-table-column
          type="index"
          label="序号"
          width="50">
        </el-table-column>
        <#list entities as p>
            <#if p.show >
        <el-table-column align="center" label="${p.name}" sortable prop="${p.name}" width="140">
            <template scope="scope">
                <span>{{scope.row.${p.value}}}</span>
            </template>
        </el-table-column>
            </#if>
        </#list>
        <el-table-column label="操作" width="180">
          <template scope="scope">
            <el-button @click="handleUpdate(scope.row)"
                       size="mini"
                       type="success"
            >编辑</el-button>
            <el-button @click="handleDelete(scope.row)"
                       size="mini"
                       type="danger"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!--分页-->
      <div v-show="!listLoading" class="pagination-container">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                       :current-page.sync="listQuery.page"
                       :page-sizes="[<#list pageSize as p>${p}<#sep>,</#list>]" :page-size="listQuery.limit"
                       layout="total,sizes,prev,pager,next,jumper" :total="total">
        </el-pagination>
      </div>
    </el-card>
    <el-dialog
            :title="textMap[dialogStatus]"
            :visible.sync="dialogFormVisible"
            width="30%"
            :before-close="handleClose">
        <el-form :model="form" :inline="true" :rules="rules" ref="form" label-width="90px">
            <#list entities as d>
            <el-form-item label="${d.name}" prop="${d.value}">
                <el-input v-if="formStatus == 'see'" disabled="true" v-model="form.${d.value}" placeholder="请输入${d.name}"></el-input>
                <el-input v-else v-model="form.${d.value}" placeholder="请输入${d.name}" ></el-input>
            </el-form-item>
            </#list>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="cancel('form')">取 消</el-button>
            <el-button v-if="dialogStatus=='create'" type="primary" @click="create('form')">确 定</el-button>
            <el-button v-else type="primary" @click="update('form')">确 定</el-button>
        </div>
    </el-dialog>
  </el-card>
</template>
<script>
  import { page, delObj, getObj, addObj, putObj} from 'api/admin/${entity}/index'
  export default {
      data() {
          return {
              list: null,
              listQuery: {
                  page: 1,
                  limit: 20,
                  <#list entities as p>
                      ${p.value}<#sep>,
                  </#list>
              },
              listLoading: true,
              total: null,
              textMap: {
                  update: '更新',
                  create: '创建'
              },
              form: this.initObj(),
              dialogStatus: '',
              dialogFormVisible: false,
          }
      },
      methods: {
          initObj() {
              return {
                  <#list entities as p>
                  ${p.value}<#sep>,
                  </#list>
              }
          },
          getList() {
              this.listLoading = true
              page(this.listQuery).then(response => {
                  this.list = response.data.rows
                  this.total = response.data.total
                  this.listLoading = false
              })
          },
          handleFilter() {
              this.getList()
          },
          handleDelete(row) {
              this.$confirm('是否刪除该记录？', '记录', {
                  confirmButtonText: '确定',
                  cancelButtonText: '取消',
                  type: 'warning'
              }).then(() => {
                  delObj(row.id).then(() => {
                      this.$notify({
                          title: '成功',
                          message: '删除成功',
                          type: 'success',
                          duration: 2000
                      })
                      this.getList();
                  })
              })
          },
          handleCreate() {
              this.dialogFormVisible = true
              this.dialogStatus = 'create'
          },
          create(formName) {
              const set = this.$refs
              set[formName].validate(valid => {
                  if (valid) {
                      addObj(this.form).then(() => {
                          this.$notify({
                              title: '成功',
                              message: '创建成功',
                              type: 'success',
                              duration: 2000
                          })
                          this.formStatus = 'see'
                          this.$emit('getUserId', this.form.id)
                      })
                  } else {
                      return false
                  }
              })
          },
          update(formName) {
              const set = this.$refs
              set[formName].validate(valid => {
                  if (valid) {
                      putObj(this.userId, this.form).then(() => {
                          this.$notify({
                              title: '成功',
                              message: '创建成功',
                              type: 'success',
                              duration: 2000
                          })
                          this.formStatus = 'see'
                      })
                  } else {
                      return false
                  }
              })
          },
          handleSizeChange(val) {
              this.listQuery.limit = val
              this.getList()
          },
          handleCurrentChange(val) {
              this.listQuery.page = val
              this.getList()
          },
          handleClose(done) {
              done();
          },
          cancel(formName) {
              this.dialogFormVisible = false
              this.$refs[formName].resetFields()
          }
      }
  }
</script>
<style>
  .input-selects-width{
    width: 340px;
  }
</style>
