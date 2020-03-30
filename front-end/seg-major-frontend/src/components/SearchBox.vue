<template>
	
 <div class="root" >
            <form class = "form-inline">
				<table>
					<tr><input type="text"  style="width:180px;" v-model="search" placeholder="searchBox">
					<tr>
					<select  style="width:180px;" name="users-out" id="students-out"  multiple="multiple" size="10">			           
						<option  v-on:click="getcube(medicine.name)"  v-for="medicine in filteredBlogs" :key='medicine' class='list-group-item'>{{medicine.name}}</option>
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
	components: {
		
	},
	async mounted(){
		this.Medicines = await this.getDrug()
	},
    computed: {
		filteredBlogs: function(){
		//this.Medicines = DrugList.data().Medicines;
		return this.Medicines.filter(
		(Medicines) => {return Medicines.name.match(this.search);}
		);
		}
    },
	methods:{
		getDrug: function(){
			//get medicine files from backend
			return medicineService.getAllMedicines();
		},
		getcube:function(id){
			this.search = id
		},
		getDrugName:function(){
			return this.search
		}  
	}
	
};

</script>

<style>
</style>
