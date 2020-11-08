<template>
  <v-container>
    <v-row>
      <v-col v-for="(item, i) in machines"  v-bind:key="i">
        <Machine :locales="locales"
                 v-bind:machine="item"/>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import Machine from "@/components/Machine";

export default {
  name: "MachineList",
  components: {
    Machine
  },
  props: ['machines', 'locales']
}
</script>

<style scoped>
  #grid {
    display: grid;
    grid-template-columns: repeat(6, auto);
    grid-gap: 6px;
  }
</style>