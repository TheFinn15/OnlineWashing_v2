<template>
  <v-list>
    <History :updater="updater" :show-edit="showEdit" :show-del="showDel" :mode-del="modeDel" :item-info="itemInfo" :mode-edit="modeEdit" :history="item" v-for="(item, i) in tableInfo" :key="i" color="indigo"/>
  </v-list>
</template>

<script>
import History from "@/components/tables/History";
export default {
  name: "HistoryList",
  components: {History},
  props: ['tableInfo', 'modeEdit', 'modeDel', 'itemInfo', 'showEdit', 'showDel', 'updater']
}
</script>

<style scoped>

</style>