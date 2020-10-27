<template>
  <div>
    <Machine :locales="locales" v-for="(item, i) in machines" v-bind:key="i"
             v-bind:machine="item"/>
  </div>
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