<!--表单类型配置-->
<template>
  <div class="app-container">
    <el-button  type="text" @click="setParam()">设置</el-button>



    <el-dialog title="设置下拉选择参数" :visible.sync="setSelectVisible" width="600px" append-to-body>
      <div style="height: 350px;overflow: auto">
        <el-form :model="setData.select" ref="form"  label-width="80px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="是否多选">
                <el-switch  v-model="setData.select.multiple" :active-value="true" :inactive-value="false"
                            active-color="#13ce66" inactive-color="#ff4949">
                </el-switch>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="是否扩展">
                <el-switch @change="cutIsextend(setData.select.isextend)"  v-model="setData.select.isextend" :active-value="true" :inactive-value="false"
                           active-color="#13ce66" inactive-color="#ff4949">
                </el-switch>
              </el-form-item>
            </el-col>
          </el-row>
          <el-card>
            <div slot="header" class="clearfix">
              <span>选项配置</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="addOpt()">新增</el-button>
            </div>
            <el-row>
              <template v-for="(item,index) in setData.select.data">
                <el-row>
                  <el-col :span="10"><el-input v-model="item.name" placeholder="label"></el-input></el-col>
                  <el-col :span="10"><el-input v-model="item.value" placeholder="value"></el-input></el-col>
                  <el-col :span="4"> <i class="el-icon-remove-outline" @click="removeOpt(index)"></i> </el-col>
                </el-row>
              </template>
              <!--<el-form-item label="配置选项">
                   <el-row>
                     <el-col :span="20">
                       <el-button style="width: 100%">新增</el-button>
                     </el-col>
                   </el-row>
              </el-form-item>-->
            </el-row>
          </el-card>
          <template v-if="setData.select.isextend">
            <el-divider content-position="left">扩展设置</el-divider>
            <el-row>
              <el-col :span="8"><el-input v-model="setData.select.extend.table" placeholder="扩展别名"></el-input></el-col>
              <el-col :span="8"><el-input v-model="setData.select.extend.name" placeholder="label"></el-input></el-col>
              <el-col :span="8"><el-input v-model="setData.select.extend.value" placeholder="value"></el-input></el-col>
            </el-row>
            <el-row>
              <form-where :value="setData.select.extend.where"></form-where>
            </el-row>
          </template>
        </el-form>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="removeForm()">清除</el-button>
        <el-button type="primary" @click="saveForm()">确 定</el-button>
      </div>
    </el-dialog>


   <el-dialog title="设置参数" :visible.sync="setHtmlVisible" width="500px" append-to-body>
      <el-input type="textarea"  :rows="10"  v-model="params" ></el-input>
      <div slot="footer" class="dialog-footer">
        <!--<el-button @click="removeForm()">清除</el-button>-->
        <el-button type="primary" @click="saveForm_Html()">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>

module.exports = {
  //inject:['save'],
  props: {
    type: String,
    value: String
  },
  inject:['save'],
  created(){

  },
  watch: {
    // 每当 question 改变时，这个函数就会执行
    value(newQuestion, oldQuestion) {
      console.log("this.value:",this.value);
     /* this.varset = JSON.parse(this.value.varset)
      this.data = JSON.parse(this.value.confjson)
      this.src = this.showpath+this.value.id+"?type=pdf&isyl=1&r="+Math.random();*/
    }
  },
    components: {
        'form-where': httpVueLoader('components/form_where.vue'),
    },
  data() {

    return {
      setSelectVisible:false,
      setHtmlVisible:false,
      params: this.value,
      setData:{
        select:{
          "multiple":false,
          "isextend":false,
          "data":[],
          "extend":{
            "table":"",
            "name":"NAME",
            "value":"ID",
            "where":{}
          }
        }
      }
    }
  },

  methods: {
    addOpt(){
      this.setData.select.data.push({"name":"","value":""})
    },
    removeOpt(i){
      this.setData.select.data.splice(i, 1)
    },
    cutIsextend(flag){//切换是否扩展
      var extend = this.setData.select.extend
      if(flag&&extend == null){
        this.setData.select.extend = {"table":"","name":"","value":"","where":{}}
      }
    },
    setParam(){
      var type = this.type;
      if(type.indexOf('select')>=0){ //下拉选择框参数配置
        if(this.params!=null){
          this.setData.select = JSON.parse(this.params);
        }
        this.setSelectVisible = true
      }else{
        this.setHtmlVisible = true
      }

      //console.log("setParam",this.value);
    },removeForm(){
      //this.form = {"key":"","oper":"","value":""};
    },
    saveForm(){//添加保存where条件
      var that = this;
      console.log("select",this.setData.select)
      this.params = JSON.stringify(this.setData.select);
      this.setSelectVisible = false;
      console.log("this.params:",this.params)
      this.$emit('input', this.params);
    },
    saveForm_Html(){
      this.$emit('input', this.params);
      this.setHtmlVisible = false;
    }

  }
};
</script>

<style>

</style>
