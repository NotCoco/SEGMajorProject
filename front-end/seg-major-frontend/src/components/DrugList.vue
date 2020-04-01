<template>	

 <div class="root"  >
		<form class = "form-inline">
				<table  style="border-collapse:separate; border-spacing:0px 3px;">
					<!-- Buttons for adding/ changing drugs info -->
					<tr>
						<td>
							<input class="input" type="text"  id="searchBox" v-model="search" style="width:350px;height:40px;" placeholder="Search"  aria-describedby="basic-addon1">
						&nbsp;
						<button class="button" type="button" style="height:40px;width: 45px;" v-on:click="addInfo()" id = "addButton">ADD</button></td>
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
										<input  class="input" style="height: 38px;width: 165px;" id = "primaryKey" disabled/>
									</td>
									<td>
										<input  class="input" style="height: 38px;width: 165px;" id = "name" />
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
										<td><button  class="button" type="button" @click="updateDrug()" id = "saveButton">Save</button></td>
										<td><button  class="button" type="button" @click="deleteDrug()" id = "deleteButton">Delete</button></td>
									</tr>
									<tr v-if="this.AddDrug" >
										<td>
											<button class="button" type="button" @click="addDrug()" id="addDrug">Save</button>
											<!-- <button class="button" @click="Test()" id="a">Test</button> -->
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
			Medicines:[
				//list of all Medicines
			],
			test:[
				//name of Medicines (TEST)
				{name:"Aerobid",type:"Tablets"},
				{name:"African Mango",type:"Liquid"},
				{name:"Azmacort",type:"Suppositories"},
				{name:"Budesonide",type:"Inhalers"},
				{name:"Clindamycin",type:"Injections"},
				{name:"Fluticasone",type:"Drops"},
				{name:"Ferocon",type:"Capsules"},
				{name:"Kisqali",type:"Tablets"}, 
				{name:"Motrin",type:"Suppositories"},
				{name:"Mupirocin topical",type:"Inhalers"},
				{name:"Quinine",type:"Drops"},
				{name:"Qvar",type:"Liquid"},
				{name:"Uroxatral",type:"Tablets"},
				{name:"Victoza",type:"Suppositories"},
				{name:"Wellbutrin",type:"Drops"},
				{name:"Wormwood",type:"Implants/Patches"}
			],
			search:""
			}
		},
		async mounted(){
			/**
			 * fetch the drug list
			 */
			document.getElementById("table").style.visibility ="hidden"
			this.Medicines = await this.getDrug()
			
		},
		computed: {
			/**
			 * search function
			 */
			filteredBlogs: function(){
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
				var name = document.getElementById('name').value
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
							var data = {"id": id ,"name": name ,"type": type}
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