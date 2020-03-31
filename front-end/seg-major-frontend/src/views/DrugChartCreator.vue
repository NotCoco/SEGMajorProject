<template>
  <div id="drug-chart-creator">
    <Navbar></Navbar>
<link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <section class="hero is-primary">
      <div class="hero-body">
        <div class="container">
          <h1 class="title">Drug Chart Creator</h1>
          <h2 class="subtitle">Create your own printable drug chart</h2>
        </div>
      </div>
    </section>

    <section class="section">
      <div class="container">
		<table  style="border-collapse:separate; border-spacing:15px 10px;">
		<tr>
			<!-- Header of the Drug chart -->
			<td><br><b>Time:</b><br><br><br><b>Name:</b><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><b>Dose:</b></td>
			<td>
				<table style="border-collapse:separate; border-spacing:15px 25px;">
						<!-- input time -->
						<tr>
						<input class="input" style="width:180px;" type="time" id="time" name="appt" min="00:00" max="24:00" required>
						</tr>
						<!-- Drug search box -->
						<tr><SearchBox ref="drug"></SearchBox></tr>
						<!-- input dozen -->
						<tr><input style="width:60px;height: 26px;" type="number" id="dose" name="appt"
						min="0.0" step=".1" required>
						<select style="width:50px;height: 26px;" id="unit">
								<option value="mg">mg</option>
								<option value="mls">mls</option>
						</select>						
						<!-- input frequency -->
						<select style="width:70px;height: 26px;" id="freq">
								<option value=""></option>
								<option value="daily">daily</option>
								<option value="daily for 1 week">daily for 1 week</option>
								<option value="to continue">to continue</option>
								
						</select>						
						</tr>
						<tr>
							<input type="checkbox" id="warn" value="As per warining regime" >
							<label for="As per warining regime">As per warining regime</label>
						</tr>
					<tr>
						<!-- buttons of the drug chart -->
						<td>
							<button valign="left" class="button is-success" @click="addCard()">Add</button>
							&nbsp;
							<button valign="right"  class="button is-success" v-print ="printObj" >Print</button>
						</td>
					</tr>
				</table>
			</td>
			<td>
				<!-- The printing unit -->
				<PrintBox ref="print"></PrintBox>
			</td>
		</tr>
		</table>
      </div>
    </section>
  </div>
</template>
<script>
import Navbar from "@/components/Navbar.vue";
import SearchBox from "@/components/SearchBox.vue"
import PrintBox from "@/components/PrintBox.vue"
export default{
	name: "DrugChart",
	data(){
		return {
			// control unit for printing
			printObj:{
				id: "printableTable",
				popTitle: 'Biliary Atresia',
				extraCss: 'https://www.google.com,https://www.google.com',
				}
			};
		},
	components: {
		SearchBox,
		PrintBox,
		Navbar
	},
	methods: {
		/**
		 * This method controls adding drug to printbox
		 */ 
		addCard : function(){
			//get all the info it needs
			var time = document.getElementById("time").value;
			var dose = document.getElementById("dose").value.toString();
			var unit = document.getElementById("unit").value.toString();
			var freq = document.getElementById("freq").value.toString();
			var bold = document.getElementById("warn").value.toString();
			var drug = this.$refs.drug.getDrugName();2
			var items = this.$refs.print.getItems();
			if(time != null && drug != null && dose != null ){
				var drugWithUnit = dose.concat(unit)
				//check if added drug is in the drug list
				if(this.drugCheck(drug)){
						if(items.length<30){
							if(dose.length == 0 && !document.getElementById("warn").checked){
								window.alert("Please fill in all the info!");
							}
							else if(time.length != 0 && (drugWithUnit.length!=0||document.getElementById("warn").checked)){
								if(document.getElementById("warn").checked){
									dose = "";
								}
								else{
									bold = "";
								}
								items.push({ Time: time, Drug: drug, Bold: bold, Unit: unit, Freq: freq, Dose: dose})
								items = items.sort(function (a, b) {
								return a.Time > b.Time ? 1 : -1;
								});
							}
							else{
								window.alert("Please fill in all the info!");
							}
						}
						else{ //can not print more than 30 medicines in one page.
								window.alert("You can only add 30 medicines at a time!")
						}
				}
				else{
					window.alert("Please fill in the right name!")
				}
			}
		},
		/**
		 * @param {Object} drug
		 * This method will check whether the drug selected is in the list
		 */
		drugCheck : function (drug){
			var i = 0
			for(i;i<this.$refs.drug.Medicines.length;i++)
			{
				if(this.$refs.drug.Medicines[i].name === drug){
					return true;
				}
			}
			return false;


		}

	}
};
</script>
<style >
	html, body {
		height: 100%;
		overflow: unset;
	}

</style>