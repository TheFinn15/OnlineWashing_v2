<template>
  <v-list flat>
    <Person :updater="updater" :show-edit="showEdit" :show-del="showDel" :mode-del="modeDel" :item-info="itemInfo" :mode-edit="modeEdit" :person="item" v-for="(item, i) in tableInfo" :key="i" color="indigo"/>
  </v-list>
</template>

<script>
import Person from "@/components/tables/Person";
export default {
  name: "PersonsList",
  components: {Person},
  props: ['tableInfo', 'modeEdit', 'modeDel', 'itemInfo', 'showEdit', 'showDel', 'updater']
}
</script>

<style scoped>

</style>