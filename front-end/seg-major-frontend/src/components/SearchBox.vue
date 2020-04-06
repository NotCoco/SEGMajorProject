<template>
  <div class="root" >
    <form class="form-inline">
      <table>
        <tr>
          <input class="input" type="text" style="width:180px;" v-model="search" placeholder="searchBox">
        </tr>
        <tr>
          <select style="width:180px;height:350px;" name="users-out" id="students-out" size="10" v-model="selectedDrugName">
            <option style="height: 30px;" v-for="medicine in searchResults" :key="medicine.primaryKey" :value="medicine.name" class="list-group-item">{{ medicine.name }}</option>
          </select>
        </tr>
      </table>
    </form>
  </div>
</template>

<script>
import medicineService from '../services/medicine-service.js'

export default {
  name: "SearchBox",
  data: function() { 
    return {
      medicines: [],
      search: "",
      selectedDrugName: "",
    }
  },
  async mounted() {
    this.medicines = await medicineService.getAllMedicines();
  },
  computed: {
    searchResults: function() {
      const query = this.search.toLowerCase();
      return this.medicines.filter(medicine => medicine.name.toLowerCase().includes(query));
    }
  },
};
</script>

<style lang="scss" scoped>
</style>
