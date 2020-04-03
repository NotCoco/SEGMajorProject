<template>
  
 <div class="root" >
            <form class = "form-inline">
        <table>
          <tr><input class="input" type="text"  style="width:180px;" v-model="search" placeholder="searchBox">
          <tr>
          <select class="select" style="width:180px;height: 350px;" name="users-out" id="students-out"  multiple="multiple" size="10">			           
            <option style="height: 30px;" v-on:click="getcube(medicine.name)"  v-for="medicine in Search" :key='medicine' class='list-group-item'>{{medicine.name}}</option>
          </select>
          </tr>
        </table>
            </form>
  </div>
</template>
<script></script>
<script type="text/javascript">
import medicineService from '../services/medicine-service.js'
export default {
  name:"SearchBox",
    data :function(){ 
        return {
      Medicines:[],
            search:"",
        }
    },

  async mounted(){
    this.Medicines = await this.getDrug()
  },
    computed: {
      Search: function(){
      //this.Medicines = DrugList.data().Medicines;
        return this.Medicines.filter(
          (Medicines) => {
            return Medicines.name.match(this.search);
            }
          );
      }
   },
  methods:{
    getDrug: function(){
      //get medicine files from backend
      return medicineService.getAllMedicines();
    },
    getcube:function(id){
      //get id of the drug
      this.search = id
    },
    getDrugName:function(){
      //return the search result
      return this.search
    }  
  }
  
};

</script>

<style>
</style>
