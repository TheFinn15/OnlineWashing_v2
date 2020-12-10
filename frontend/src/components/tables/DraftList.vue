<template>
  <v-list>
    <Draft :locales="locales" :updater="updater" :show-edit="showEdit" :show-del="showDel" :mode-del="modeDel" :item-info="itemInfo" :mode-edit="modeEdit" :draft="item" v-for="(item, i) in tableInfo" :key="i" color="indigo"/>
  </v-list>
</template>

<script>
import Draft from "@/components/tables/Draft";
export default {
  name: "DraftList",
  components: {Draft},
  props: ['tableInfo', 'modeEdit', 'modeDel', 'itemInfo', 'showEdit', 'showDel', 'updater', 'locales']
}
</script>

<style scoped>

</style>