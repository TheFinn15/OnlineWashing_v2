<template>
  <v-list>
    <Wallet :locales="locales" :updater="updater" :show-edit="showEdit" :show-del="showDel" :mode-del="modeDel" :item-info="itemInfo" :mode-edit="modeEdit" :wallet="item" v-for="(item, i) in tableInfo" :key="i" color="indigo"/>
  </v-list>
</template>

<script>
import Wallet from "@/components/tables/Wallet";
export default {
  name: "WalletList",
  components: {Wallet},
  props: ['tableInfo', 'modeEdit', 'modeDel', 'itemInfo', 'showEdit', 'showDel', 'updater', 'locales']
}
</script>

<style scoped>

</style>