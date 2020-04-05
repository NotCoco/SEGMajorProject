<template>
  <div id="first-layout">
    <section class="section">
      <div class="container">
        <table class="table-layout">
          <tr>
            <!-- Header of the Drug chart -->
            <td><br><b>Time:</b><br><br><br><b>Name:</b><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><b>Dose:</b></td>
            <td>
              <table class="subtable-layout">
                <!-- input time -->
                <tr><input class="input" style="width:180px;" type="time" id="time" name="appt" min="00:00" max="24:00" required></tr>
                <!-- Drug search box -->
                <tr><SearchBox ref="drug"></SearchBox></tr>
                <!-- input dozen -->
                <tr>
                  <input style="width:60px;height: 26px;" type="number" id="dose" name="appt" min="0.0" step=".1" required>
                  <select style="width:50px;height: 26px;" id="unit">
                    <option value="mg">mg</option>
                    <option value="mls">mls</option>
                  </select>						
                  <!-- input frequency -->
                  <select style="width:70px;height: 26px;" id="freq">
                    <option value=""></option>
                    <option value="daily">daily</option>
                    <option value="daily for 1 week">daily for 1 week</option>
                    <option value="daily to continue">daily to continue</option>
                  </select>						
                </tr>
                <tr>
                  <input type="checkbox" id="warn" value="As per warining regime" >
                  <label for="As per warining regime" style="font-size: 15px;">As per warining regime</label>
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
    PrintBox
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
      var drug = this.$refs.drug.getDrugName();
      var items = this.$refs.print.getItems();
      if(time != null && drug != null && dose != null ){
        var drugWithUnit = dose.concat(unit)
        //check if added drug is in the drug list
        if(this.drugCheck(drug)){
            if(items.length<100){
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
            else{ //can not print more than 100 medicines in one page.
                window.alert("You can only add 100 medicines at a time!")
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
<style lang="scss" scoped>
 .table-layout{
    border-collapse:separate; 
    border-spacing:15px 10px;
    .subtable-layout{
      border-collapse:separate;
      border-spacing:15px 25px;
    }
 }
</style>
