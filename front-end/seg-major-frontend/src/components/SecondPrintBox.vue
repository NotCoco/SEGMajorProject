<template>
  
  <div class="print" id="printableTable">
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
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
          <div class="print-hide button-group">
            <button class="layout button is-danger" v-show="showtable" @click="showTable()">
              <i class="fa fa-minus-square">&nbsp;Hide Table</i>
            </button>
            <button class="layout button is-success" v-show="!showtable" @click="showTable()">
              <i class="fa fa-plus-square">&nbsp;Show Table</i>
            </button>
            <br><br>
          </div>
          
          <table style="width: 1000px;"  v-show="showtable">
            <!-- table head -->
            
            <thead class="thead-light">
              <th valign="left">Drug&nbsp;</th>
              <th style="width:90px;" valign="left">Dose&nbsp;</th>
            </thead>

            <tbody >
              <!-- drug names -->
              <tr class="print"  valign="left"  v-for="(item,index) in items"  v-bind:index="index" v-bind:key="item">
                  <td class="print" v-if="index===0">{{split(item.Drug)}}&nbsp;</td>
                  <td class="print" v-else>&nbsp;</td>
                  <td class="print"><b>{{item.Dose}}{{item.Unit}}</b><br>{{item.Freq}}&nbsp;</td>
                  <!-- delete items -->
                  <td class="print-hide" align="absmiddle"><button class="button" style="width: 10px;height: 30px;" @click="delCard(item)"><i class="fa fa-close"></i></button><br></td>
                  <td class="print" style="width: 60px;"></td>
                  <td class="print" style="width: 60px;"></td>
                  <td class="print" style="width: 60px;"></td>
                  <td class="print" style="width: 60px;"></td>
                  <td class="print" style="width: 60px;"></td>
                  <td class="print" style="width: 60px;"></td>
                  <td class="print" style="width: 60px;"></td>
                  <td class="print" style="width: 60px;"></td>
                  <td class="print" style="width: 60px;"></td>
                  <td class="print" style="width: 60px;"></td>
                  <td class="print" style="width: 60px;"></td>
                  <td class="print" style="width: 60px;"></td>
                  <td class="print" style="width: 60px;"></td>
                 
              </tr>
            </tbody>
            
          </table>
          
        </tr>
        <tr>
          
            <div class="print-hide button-group">
              <br>
              <button class="layout button is-danger" v-show="showediter" @click="showEditer()">
                <i class="fa fa-minus-square">&nbsp;Hide Editer</i>
              </button>
              
              <button class="layout button is-success" v-show="!showediter" @click="showEditer()">
                <i class="fa fa-plus-square">&nbsp;Show Editer</i>
              </button>
              <br><br>
            </div>
            <transition name="fade-transform" mode="out-in">
             <rich-text-editor v-show="showediter" v-model="page.content" class="textEditer"></rich-text-editor>
            </transition>
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
import RichTextEditor from "@/components/RichTextEditor";
  export default{
  
    name: "DrugChart",
    components:{
      RichTextEditor
    },
    data(){
      return {
        showediter:true,
        showtable:true,
        page:{},
        //empty list for current added drugs
        items:[]
      };
    },
    watch:{
      showEditer: {
        deep: true,
        handler (curVal, oldVal) {
          console.log(curVal+oldVal)
          let self = this
          setTimeout(function () {
            self._ChangeAlertInfo({
              'show': false
            })
          }, 1300)
        }
      }
    },
    methods:{
      change: function(){
        this.$set(this.items[0], 'Dose', 666);
      },
      showTable: function(){
         this.showtable = !this.showtable
      },
      showEditer: function(){
        this.showediter = !this.showediter
      },
      /**
       * @param {Object} medicine
       * if the length of the drug is longer than  15 words
       * split it
       */
      split: function(medicine){
        var firsthalf=""
        var secondhalf=""
        if(medicine.length>15&&medicine.length<=30){
          firsthalf = medicine.substr(0,15)
          secondhalf = medicine.substr(15,medicine.length)
          medicine = firsthalf+"\n"+secondhalf
          return medicine
        }
        else if(medicine.length>30){
          firsthalf = medicine.substr(0,20)
          secondhalf = medicine.substr(20,medicine.length)
          medicine = firsthalf+"\n"+secondhalf
          return medicine
        }
        return medicine
      },
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
  
  .button.layout{
    width: 140px;
  }
  .textEditer{
    height: 300px;
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
<style scope lang='scss'>

/* fade */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.28s;
}

.fade-enter,
.fade-leave-active {
  opacity: 0;
}

/* fade-transform */
.fade-transform-leave-active,
.fade-transform-enter-active {
  transition: all .5s;
}

.fade-transform-enter {
  opacity: 0;
  transform: translateX(-30px);
}

.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}

/* breadcrumb transition */
.breadcrumb-enter-active,
.breadcrumb-leave-active {
  transition: all .5s;
}

.breadcrumb-enter,
.breadcrumb-leave-active {
  opacity: 0;
  transform: translateX(20px);
}

.breadcrumb-move {
  transition: all .5s;
}

.breadcrumb-leave-active {
  position: absolute;
}
</style>