<template>
  
  <div class="print" id="printableTable">
    <div class="header" >
      <!-- Header of printbox -->
      <table class="table-layout striped" >
          <tr>
            <th>
              <p>Patient Name</p>
              <input class="height input" size="45"/>
            </th>
            <th>
              <p>Hospital Number</p>
              <input class="height input" size="25"/>
            </th>
            <th>
              <p>DOB</p>
              <input class="height input" type="date" value="2000-01-01"/>
            </th>
            <th>
              <p>Drug Allergies</p>
              <input class="height input"  size="45"/>
            </th>

          </tr>

      </table>
    </div>
  
      <table class="print">
        <tbody style="display: none;">
          <tr>The information below is an aid to help with the timing of your child's medicines after the Kasai Prucedure. Always read the Patient Information Leaflet supplied with the medication and the label on the front of each medication. A nurse on the ward will go through the medications with you prior to discharge.</tr>
        </tbody >
        <!-- The table displays all of the added medicines -->
        <tr>
          <table style="width: 1000px;" >
            <!-- table head -->
            <thead class="thead-light">
              <th valign="left">Time&nbsp;</th>
              <th valign="left">Drug&nbsp;</th>
              <th style="width:90px;" valign="left">Dose&nbsp;<br></th>
            </thead>
              
            <tbody >
              <!-- drug names -->
              <tr class="print" valign="left" v-for="(item,index) in items" v-bind:index="index" v-bind:key="index">
                  <td class="print" v-if="item === items[0]">{{item.Time}}&nbsp;</td>
                  <td class="print" v-else-if="items[index-1].Time === item.Time"></td>
                  <td class="print" v-else>{{item.Time}}&nbsp;</td>
                  <td class="print" >{{item.Drug}}&nbsp;</td>
                  <td class="print" v-if="item.Bold=='As per warining regime'"><b>{{item.Bold}}&nbsp;</b></td>
                  <td class="print" v-else><b>{{item.Dose}}{{item.Unit}}</b><br>{{item.Freq}}&nbsp;</td>
                  <!-- delete items -->
                  <td class="print-hide" align="absmiddle"><button class="button" style="width: 10px;height: 30px;" @click="delCard(item)"><i class="fa fa-close"></i></button><br></td>
                  <td class="print" style="width: 50px;" v-for="n in 31" :key="n"></td>
              </tr>
              
            </tbody>
          </table>
          
        </tr>
        <tbody style="display: none;">
          <tr>
          <!-- Footer for notification -->
          <div class="relative" >
          <br><b>If you have queries regarding your child`s medication please contact the Liver Clinical Nurse Specialist Team on 0203-299-3773 or the pharmacy team at
          King`s directly on 0203-299-9000 ext 5723 between 9:00 AM- 5:00 PM Monday to Friday. Out of these hours please contact rays of Sunshine Ward on 0203-299-3577.</b>
          </div>
          </tr>
        </tbody>
      </table>

  </div>
</template>

<script>
  export default{
    name: "DrugChart",
    data(){
      return {
        //empty list for current added drugs
        items:[],
      };
    },
    methods:{
      /**
       * @param {Object} item
       * Deleting items from print list
       */
      delCard: function(item){
        var i= 0,index = -1
        for(i;i<this.items.length;i++){
          if(this.items[i]===item){
            index=i
          }
        }
        if(index!=-1){
          this.items.splice(index,1)
        }
      },
      getItems: function(){
        return this.items;
      }
    }
  }

</script>

<style lang="scss" media="print" scoped>
  /* Style the body */
  .striped.table-layout{
    border-collapse:separate; 
    border-spacing:5px 25px;
  }
  .input.height{
    height: 25px;
  }
  /* Header/Logo Title */
  .header {
  padding: 0px;
  text-align: center;
  color: white;
  font-size: 10px;
  }

  Page Content
  .content {padding:20px;}
  .footer {
  position: fixed;
  left: 0;
  bottom: 0;
  width: 100%;
  color: black;
  text-align: center;
  }
  /* Style for the footer of print list */
  div.relative {
  position: relative;
  width: 100%;
  bottom: 10px;
  color: black;
  }
  td.print{
  border:1px solid #000000; 
  }
  tr.print{
  border:1px solid #000000; 
  }
  @media print {
    .print-hide {
      visibility: hidden!important;
      display: none!important;
    }
    .page-break {
    page-break-after: always;
    }
    @page{
      size: landscape; 
      margin-top: 1mm;
    }
  }
  
</style>
