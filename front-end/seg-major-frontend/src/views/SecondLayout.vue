<template>
  <div id="second-layout">
<link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">

    <section class="section">
  <div class="container">
    <table class="table-layout">
    <tr>
      <!-- Header of the Drug chart -->
      <td><br><b>Name:</b><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><b>Dose:</b></td>
      <td>
        <table class="subtable-layout">
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
                <option value="daily for 1 week">daily for 1 week</option>
                <option value="daily to continue">daily to continue</option>
                
            </select>						
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
        <SecondPrintBox ref="print"></SecondPrintBox>
      </td>
    </tr>
    </table>
      </div>
    </section>
  </div>
</template>
<script>
import SearchBox from "@/components/SearchBox.vue"
import SecondPrintBox from "@/components/SecondPrintBox.vue"
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
    SecondPrintBox,
  },
  methods: {

    /**
     * This method controls adding drug to printbox
     */ 
    addCard : function(){
      //get all the info it needs
      var dose = document.getElementById("dose").value.toString();
      var unit = document.getElementById("unit").value.toString();
      var freq = document.getElementById("freq").value.toString();
      var drug = this.$refs.drug.getDrugName();
      var items = this.$refs.print.getItems();
      if(drug != null && dose != null ){
        var drugWithUnit = dose.concat(unit)
        //check if added drug is in the drug list
        if(this.drugCheck(drug)){
            if(items.length<10){
              if(dose.length == 0){
                window.alert("Please fill in all the info!");
              }
              else if(drugWithUnit.length!=0){
                if(items.length===0){
                  items.push({ Drug: drug, Unit: unit, Freq: freq, Dose: dose})
                }
                else if(items[0]['Drug']===drug){
                   items.push({ Drug: drug, Unit: unit, Freq: freq, Dose: dose})
                }
                else{
                  window.alert("You can only add dose frequencies to ur first medicine: ["+items[0]['Drug']+"]")
                }
               
              }
              else{
                window.alert("Please fill in all the info!");
              }
            }
            else{ //can not print more than 100 medicines in one page.
                window.alert("You can only add 10 doses.")
            }
        }
        else{
          window.alert("Please fill in the right name!")
        }
      }
      else{
        window.alert("Please fill in all the info!");
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