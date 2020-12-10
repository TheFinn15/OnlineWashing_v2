<template>
  <v-list flat>
    <Stock :locales="locales" :updater="updater" :show-edit="showEdit" :show-del="showDel" :mode-del="modeDel" :item-info="itemInfo" :mode-edit="modeEdit" :stock="item" v-for="(item, i) in tableInfo" :key="i" color="indigo"/>
  </v-list>
</template>

<script>
import Stock from "@/components/tables/Stock";
export default {
  name: "StockList",
  components: {Stock},
  props: ['tableInfo', 'modeEdit', 'modeDel', 'itemInfo', 'showEdit', 'showDel', 'updater', 'locales']
}
</script>

<style scoped>

</style>