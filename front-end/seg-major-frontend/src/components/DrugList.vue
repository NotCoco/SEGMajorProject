<template>
	
 <div class="root"  >
	
    <!-- Compiled and minified CSS -->

     
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
    
            <form class = "form-inline">
				<table  style="border-collapse:separate; border-spacing:0px 3px;">
					<!-- Buttons for adding/ changing drugs info -->
					<tr>
						<td>
							<input type="text"   id="searchBox" v-model="search" style="width:335px;" class="form-control" placeholder="Search"  aria-describedby="basic-addon1">
						&nbsp;
						<button type="button" class="btn btn-outline-primary" style="height:37px;" v-on:click="addInfo()" id = "addButton">ADD</button></td>
					</tr>
					<tr>
						<td>
							<select  style="width:400px;height: 750px;" name="users-out" id="students-out"  multiple="multiple" size="10">
								<option type="button"  @click="changeInfo(medicine)" v-for="medicine in filteredBlogs" :key='medicine' class='list-group-item'>{{medicine.name}}</option>
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
										<input  class="form-control" id = "id" disabled/>
									</td>
									<td>
										<input  class="form-control" id = "name" />
									</td>
									<td >
										<!-- multiSelect dropdown -->
										<multiselect id = "type"  v-model="value" style="width: 165px;" placeholder="Select" label="title" track-by="title" :options="options" :option-height="104" :custom-label="customLabel" :show-labels="false">
										<template slot="option" slot-scope="props">
											<img class="option__image" :src="props.option.img" alt="Page Lost">
											<div class="option__desc"><span class="option__title">{{ props.option.title }}</span><span class="option__small">{{ props.option.desc }}</span></div>
											</template>
										</multiselect>
									</td>
								</tr>
										<!-- Buttons -->
									<tr v-if="this.ChangeDrug" >
										<td><button  class="btn btn-outline-primary" type="button" id = "saveButton">Save</button></td>
										<td><button  class="btn btn-outline-primary" type="button" id = "deleteButton">Delete</button></td>
									</tr>
									<tr v-if="this.AddDrug" >
										<td><button class="btn btn-outline-primary" type="button" id = "saveButton2">Save</button></td>
									</tr>
							</table>
						</td>		
					</tr>
				</table>
               
                </form>
  </div>	
</template>


<script type="text/javascript" >
	//import multiselect dropdown
	import Multiselect from 'vue-multiselect'
	export default{
		name:"DrugList",
		components: { 
			Multiselect
		},
		data :function(){ 
			return {
			ChangeDrug: false,
			AddDrug: false,
			Icon: '',
			sortType: 'sort',
			value: { title: '', desc: '', img: '' },
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
				//name of Medicines
					{id:1,name:"Aerobid",type:"Tablets"},
					{id:2,name:"African Mango",type:"Liquid"},
					{id:3,name:"Azmacort",type:"Suppositories"},
					{id:4,name:"Budesonide",type:"Inhalers"},
					{id:5,name:"Clindamycin",type:"Injections"},
					{id:6,name:"Fluticasone",type:"Drops"},
					{id:7,name:"Ferocon",type:"Capsules"},
					{id:8,name:"Kisqali",type:"Tablets"}, 
					{id:9,name:"Motrin",type:"Suppositories"},
					{id:10,name:"Mupirocin topical",type:"Inhalers"},
					{id:11,name:"Quinine",type:"Drops"},
					{id:12,name:"Qvar",type:"Liquid"},
					{id:13,name:"Uroxatral",type:"Tablets"},
					{id:14,name:"Victoza",type:"Suppositories"},
					{id:15,name:"Wellbutrin",type:"Drops"},
					{id:16,name:"Wormwood",type:"Implants/Patches"},
			],
			search:""
			}
		},
		mounted() {
			document.getElementById("table").style.visibility ="hidden"
		},
		computed: {
			filteredBlogs: function(){
			return this.Medicines.filter(
			(Medicines) => {return Medicines.name.match(this.search);}
			);
			}
		},
		methods: {
			addDrug: function(){
				//place for adding drugs
			},
			deleteDrug: function(){
				//place for deleting drugs
			},
			// This method will display the drug info
			// which user selected.
			changeInfo : function(medicine) {
				
				this.ChangeDrug = true;
				this.AddDrug = false;
				
				document.getElementById("table").style.visibility = "visible"
				document.getElementById("saveButton").style.visibility = "visible"
				document.getElementById("deleteButton").style.visibility = "visible"

				document.getElementById("id").value = medicine.id
				document.getElementById("name").value = medicine.name
				this.value.title = medicine.type
				

			},
			// This method will display the adding frame
			// which user can input info.
			addInfo : function() {
				this.ChangeDrug = false;
				this.AddDrug = true;
				
				document.getElementById("name").value = ""
				this.value.title = ""
				
				document.getElementById("table").style.visibility = "visible"
				document.getElementById("saveButton2").style.visibility = "visible"
				

			}
		}
	}
</script>


<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>