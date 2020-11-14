<template>
  <v-container>
    <v-row>
      <v-col cols="2" v-for="(item, i) in machines" :key="i">
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

</style>