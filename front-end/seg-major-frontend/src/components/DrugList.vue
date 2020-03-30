<template>
	
 <div class="root"  >
		<form class = "form-inline">
				<table  style="border-collapse:separate; border-spacing:0px 3px;">
					<!-- Buttons for adding/ changing drugs info -->
					<tr>
						<td>
							<input type="text"  id="searchBox" v-model="search" style="width:350px;height:40px;" class="form-control" placeholder="Search"  aria-describedby="basic-addon1">
						&nbsp;
						<button type="button" class="btn btn-outline-primary" style="height:35px;" v-on:click="addInfo()" id = "addButton">ADD</button></td>
					</tr>
					<tr>
						<td>
							<select   style="width:400px;height: 450px;" name="users-out" id="allDrugs"  multiple="multiple" size="10">
								<option type="button"   @click="changeInfo(medicine)" v-for="medicine in filteredBlogs" :key='medicine' class='list-group-item'>{{medicine.name}}</option>
							</select>
							
						</td>
						
						<td>						
							<table id="table" style="border-collapse:separate; border-spacing:5px 5px;">
								<thead>
									<tr>
									<th v-if="this.ChangeDrug" >ID</th>
									<th>Name</th>
									<th>Type</th>
									</tr>
								</thead>
								<tr>
									<td v-if="this.ChangeDrug">
										<input  class="form-control" style="height: 38px;" id = "primaryKey" disabled/>
									</td>
									<td>
										<input  class="form-control" style="height: 38px;" id = "name" />
									</td>
									<td >
										<!-- multiSelect dropdown -->
										<multiselect id = "type" v-model="selected" style="width: 165px;" placeholder="Select" label="title" track-by="title" :options="options" :option-height="104" :show-labels="false">
										<template slot="option" slot-scope="props">
											<img class="option__image" :src="props.option.img" alt="Page Lost">
											<div class="option__desc"><span class="option__title">{{ props.option.title }}</span><span class="option__small">{{ props.option.desc }}</span></div>
											</template>
										</multiselect>
									</td>
								</tr>
										<!-- Buttons -->
									<tr v-if="this.ChangeDrug" >
										<td><button  class="btn btn-outline-primary" type="button" @click="updateDrug()" id = "saveButton">Save</button></td>
										<td><button  class="btn btn-outline-primary" type="button" @click="deleteDrug()" id = "deleteButton">Delete</button></td>
									</tr>
									<tr v-if="this.AddDrug" >
										<td>
											<button class="btn btn-outline-primary" type="button"  @click="addDrug()" id="addDrug">Save</button>
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
			ChangeDrug: false,
			AddDrug: false,
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
			// sortOptions: [
			Medicines:[
				//name of Medicines (TEST)
					// {id:1,name:"Aerobid",type:"Tablets"},
					// {id:2,name:"African Mango",type:"Liquid"},
					// {id:3,name:"Azmacort",type:"Suppositories"},
					// {id:4,name:"Budesonide",type:"Inhalers"},
					// {id:5,name:"Clindamycin",type:"Injections"},
					// {id:6,name:"Fluticasone",type:"Drops"},
					// {id:7,name:"Ferocon",type:"Capsules"},
					// {id:8,name:"Kisqali",type:"Tablets"}, 
					// {id:9,name:"Motrin",type:"Suppositories"},
					// {id:10,name:"Mupirocin topical",type:"Inhalers"},
					// {id:11,name:"Quinine",type:"Drops"},
					// {id:12,name:"Qvar",type:"Liquid"},
					// {id:13,name:"Uroxatral",type:"Tablets"},
					// {id:14,name:"Victoza",type:"Suppositories"},
					// {id:15,name:"Wellbutrin",type:"Drops"},
					// {id:16,name:"Wormwood",type:"Implants/Patches"},
			],
			search:""
			}
		},
		async mounted(){
			document.getElementById("table").style.visibility ="hidden"
			this.Medicines = await this.getDrug()
		},
		computed: {
			filteredBlogs: function(){
			return this.Medicines.filter(
				(Medicines) => {return Medicines.name.match(this.search);}
				);
			}
		},
		methods: {
			getDrug: function(){
				//get medicine files from backend
				return medicineService.getAllMedicines();
			},
			//add drug to db
			addDrug: function(){
				//add drug to backend
				var name = document.getElementById('name').value
				var type = this.selected.title
				if(name!=="" && type !==""){
					var data = {"name": name,"type": type}
					medicineService.createMedicine(data);
					location.reload()
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
				if(id!=="" && name!=="" && type !==""){
					var data = {"id": id ,"name": name ,"type": type}
					medicineService.updateMedicine(data);
					location.reload()
				}
				else{
					window.alert("Please Enter full info!")
				}
			},
			// This method will display the drug info
			// which user selected.
			changeInfo : function(medicine) {
				this.ChangeDrug = true;
				this.AddDrug = false;
				if(medicine!==null){
					document.getElementById("table").style.visibility = "visible"
					document.getElementById("saveButton").style.visibility = "visible"
					document.getElementById("deleteButton").style.visibility = "visible"
					
					document.getElementById("primaryKey").value = medicine.primaryKey
					document.getElementById("name").value = medicine.name
					this.selected.title = medicine.type
				}

			
			},
			// This method will display the adding frame
			// which user can input info.
			addInfo : function() {
				this.ChangeDrug = false;
				this.AddDrug = true;
				
				document.getElementById("name").value = ""
				this.selected.title = ""
				
				document.getElementById("table").style.visibility = "visible"
				document.getElementById("addDrug").style.visibility = "visible"


			}
		}
	}
</script>


<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>