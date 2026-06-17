<!--Where条件配置-->
<template>
  <div class="app-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>WHERE条件</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="add()">添加条件</el-button>
      </div>

        <el-row :gutter="5">
          <el-col :span="12" v-for="(v,k) in value">
            <div class="tag">
              <span>{{k}} {{Object.keys(v)[0]}} {{v[Object.keys(v)[0]]}}</span>
              <span class="close"><i class="el-icon-close" @click="removeWhere(k)"></i></span>
            </div>

          </el-col>
          <!--<template v-for="(v1,k1) in v">
            <el-col :span="8"><el-input v-model="k1" placeholder="比较符"></el-input></el-col>
            <el-col :span="8"><el-input v-model="v1" placeholder="值"></el-input></el-col>
          </template>-->

        </el-row>

    </el-card>

    <el-dialog title="设置WHERE条件" :visible.sync="setWhereVisible" width="500px" append-to-body>
      <el-form :model="form" ref="form" >
        <el-row>
          <el-col :span="8"><el-input v-model="form.key" placeholder="字段名"></el-input></el-col>
          <el-col :span="6">
            <el-select v-model="form.oper" placeholder="比较符">
              <el-option  v-for="value in selects.operList" :key="value" :label="value" :value="value"></el-option>
            </el-select>
          </el-col>
          <el-col :span="10"><el-input v-model="form.value" placeholder="值"></el-input></el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="removeForm()">清除</el-button>
        <el-button type="primary" @click="saveForm()">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>
<style>
  .tag{background-color: #f5f7fa;border: 1px solid #EBEEF5;border-radius: 5px;padding: 6px}
  .close {float: right;}
</style>

<script>

module.exports = {
  //inject:['save'],
  props: {
    value: {
      type:Object,
      default:{}
    }
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
  data() {

    return {
      setWhereVisible:false,
      form:{"key":"","oper":"","value":""},
      selects:{
        operList:["=",">",">=","<","<=","!=","<>","in","like"],
      }
    }
  },

  methods: {
    add(){
      this.setWhereVisible = true;
      this.form = {"key":"","oper":"","value":""};
    },
    removeForm(){
      this.form = {"key":"","oper":"","value":""};
    },
    saveForm(){//添加保存where条件
      var that = this;
      var key = this.form.key;
      var oper = this.form.oper;
      var value = this.form.value;
      this.value[key] = {[oper]:value}
      that.setWhereVisible = false;
    },
    removeWhere(k){//删除where条件
      this.$delete(this.value,k);
    }

  }
};
</script>

<style>

</style>
