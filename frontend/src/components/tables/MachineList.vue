<template>
  <v-list flat>
    <Machine
        :updater="updater"
        :show-edit="showEdit"
        :show-del="showDel"
        :mode-del="modeDel"
        :item-info="itemInfo"
        :mode-edit="modeEdit"
        :machine="item"
        v-for="(item, i) in tableInfo"
        :key="i"
        color="indigo"
    />
  </v-list>
</template>

<script>
import Machine from "@/components/tables/Machine";

export default {
  name: "MachineList",
  components: {Machine},
  props: ['tableInfo', 'modeEdit', 'modeDel', 'itemInfo', 'showEdit', 'showDel', 'updater']
}
</script>

<style scoped>

</style>