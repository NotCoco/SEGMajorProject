<template>	

<div class="root"  >
  <form class = "form-inline">
    <table class="table-layout" >
      <!-- Buttons for adding/ changing drugs info -->
      <tr>
        <td>
          <input class="input" type="text"  id="searchBox" v-model="search" style="width:350px;height:40px;" placeholder="Search"  aria-describedby="basic-addon1">
          &nbsp;
          <button class="button" type="button" style="height:40px;width: 45px;" v-on:click="addInfo()" id = "addButton">ADD</button>
        </td>
      </tr>
      <tr>
        <td>
          <select  style="width:400px;height: 450px;" name="users-out" id="allDrugs"  multiple="multiple" size="10">
            <option type="button"   @click="changeInfo(medicine)" v-for="medicine in Search" :key='medicine.id' class='list-group-item'>{{medicine.name}}</option>
          </select>
        </td>
        <td>						
          <table v-if="this.DrugBox" id="table" class="searchBox-layout">
            <thead>
              <tr>
              <th v-if="this.ChangeDrug" >ID</th>
              <th>Name</th>
              <th>Type</th>
              </tr>
            </thead>
              <tr>
                <td v-if="this.ChangeDrug">
                  <input v-model="selectedMedicine.primaryKey" class="DrugList input"  id = "primaryKey" disabled/>
                </td>
                <td v-if="this.ChangeDrug">
                  <input v-model="selectedMedicine.name" class="DrugList input" id = "name" />
                </td>
                <td v-if="this.AddDrug">
                  <input  class="DrugList input" id = "add_name" />
                </td>
                <td >
                  <!-- multiSelect dropdown -->
                  <multiselect id = "type" v-model="selected" style="height: 38px;width: 165px;" placeholder="Select" label="title" track-by="title" :options="options" :option-height="104" :show-labels="false">
                    <template slot="option" slot-scope="props">
                      <img class="option__image" :src="props.option.img" alt="Page Lost">
                      <div class="option__desc"><span class="option__title">{{ props.option.title }}</span><span class="option__small">{{ props.option.desc }}</span></div>
                    </template>
                  </multiselect>
                </td>
              </tr>
                <!-- Buttons -->
              <tr v-if="this.ChangeDrug" >
                <td><button v-if="ShowSaveButton" class="button" type="button" @click="updateDrug()" id = "saveButton">Save</button></td>
                <td><button v-if="ShowDeleteButton" class="button" type="button" @click="deleteDrug()" id = "deleteButton">Delete</button></td>
              </tr>
              <tr v-if="this.AddDrug" >
                <td>
                  <button class="button" type="button" @click="addDrug()" id="addDrug">Save</button>
                </td>
              </tr>
          </table>
        </td>		
      </tr>
    </table>
  </form>
</div>	
</template>


<script type="text/javascript" >
	import Multiselect from 'vue-multiselect'
	import medicineService from '../services/medicine-service.js'
	export default{
    name:"DrugList",
    components: { 
      Multiselect,
    },
    data :function(){ 
      return {
      selectedMedicine:{ id: '', name: '', type: '' },
      DrugBox: false,
      ChangeDrug: false,
      AddDrug: false,
      ShowTable: false,
      ShowDeleteButton : false,
      ShowSaveButton : false,
      Icon: '',
      sortType: 'sort',
      selected: { title: '', desc: '', img: '' },
      //list of all types
      options:[
        { title: 'Liquid', img: require("./img/liquid.png") },
        { title: 'Tablets', img: require("./img/tablets.png") },
        { title: 'Capsules', img:require("./img/capsules.png")},
        { title: 'Topical', img: require("./img/topical.png")},
        { title: 'Suppositories', img: require("./img/suppositories.png")},
        { title: 'Drops', img: require("./img/drops.png") },
        { title: 'Inhalers', img: require("./img/inhalers.png") },
        { title: 'Injections', img: require("./img/injection.png")},
        { title: "Implants/Patches", img: require("./img/implants.png")},
      ],
      Medicines:[
        //list of all Medicines
      ],
      search:""
      }
    },
    async mounted(){
      /**
      * fetch the drug list
      */
      // document.getElementById("table").style.visibility ="hidden"
      this.Medicines = await this.getDrug()
    },
    computed: {
      /**
      * search function
      */
      Search: function(){
      return this.Medicines.filter(
        (Medicines) => {return Medicines.name.match(this.search);}
        );
      }
    },
    methods: {
      /**
      * Test data
      */
      Test: function(){
        for(var i=0;i<this.test.length;i++){
          var data = {"name": this.test[i].name,"type":  this.test[i].type}
          medicineService.createMedicine(data)
        }
      },
      /**
      * get all medicines
      */
      getDrug: function(){
        //get medicine files from backend
        return medicineService.getAllMedicines();
      },
      /**
      * @param {Object} matching string
      * match if theres invalid word
      */
      regTest: function(string){
        let reg = /[@#%&+;":{}'*^!.,~_=><]+/g
        return reg.test(string)
      },
      //add drug to db
      addDrug: function(){
        //add drug to backend
        var name = document.getElementById('add_name').value
        var type = this.selected.title
        var name_len = name.length
        
        if(name!=="" && type !==""){
          console.log(name_len)
          if(name_len >40){
            window.alert("The medicine name can not be longer than 40 words!")
          }
          else{
            if(this.regTest(name)){
              window.alert("Please do NOT enter invalid words!\n"+
              'Invalid: @#%&+;"'+":{}"+"'*^!.,~_=><")
            }else{
              var data = {"name": name,"type": type}
              medicineService.createMedicine(data);
              location.reload()
            }
                
          }

        }
        else{
          window.alert("Please Enter full Info!")
        }

      },
      //delete drug from db
      deleteDrug: function(){
        var id = document.getElementById('primaryKey').value
        var name = document.getElementById('name').value
        var type = this.selected.title
        if(id!=="" && name!=="" && type !==""){
          var data = {"id": id ,"name": name ,"type": type}
          medicineService.deleteMedicine(data);
          location.reload()
        }
        else{
          window.alert("Can`t delete a non-exist drug!")
        }
      },
      //update drug
      updateDrug: function(){
        var id = document.getElementById('primaryKey').value
        var name = document.getElementById('name').value
        var type = this.selected.title
        var name_len = name.length
        
        if(id!=="" && name!=="" && type !==""){
          if(name_len >40){
            window.alert("The medicine name can not be longer than 40 words!")
          }
          else{
            if(this.regTest(name)){
              window.alert("Please do NOT enter invalid words!\n"+
              'Invalid: @#%&+;"'+":{}"+"'*^!.,~_=><")
            }else{
              var data = {"primaryKey": id ,"name": name ,"type": type}
              medicineService.updateMedicine(data);
              location.reload()
            }
          } 

        }
        else{
          window.alert("Please Enter full info!")
        }
      },
      // This method will display the drug info
      // which user selected.
      changeInfo : function(medicine) {
        this.DrugBox = true;
        this.ChangeDrug = true;
        this.AddDrug = false;
        if(medicine!==null){
          this.ShowTable = true;
          this.ShowSaveButton = true;
          this.ShowDeleteButton = true;
          this.selectedMedicine=medicine;
          this.selected.title = medicine.type
        }


      },
      // This method will display the adding frame
      // which user can input info.
      addInfo : function() {
        this.DrugBox = true;
        this.ChangeDrug = false;
        this.AddDrug = true;
        this.selected.title = ""
        this.ShowTable = true;
      }
    }
  }
</script>


<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>
<style lang="scss" scoped>
 .table-layout{
    border-collapse:separate; 
    border-spacing:0px 3px;
    .searchBox-layout{
      border-collapse:separate; 
      border-spacing:5px 5px;
    }
 }
 .input.DrugList{
   height: 38px;
   width: 165px;
 }
</style>