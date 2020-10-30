<template>
  <v-list-group>
    <template v-slot:activator>
      <v-btn icon v-if="modeEdit" @click="showEditForm" :updater="showDelForm">
        <v-icon>edit</v-icon>
      </v-btn>
      <v-btn icon v-if="modeDel" @click="showDelForm">
        <v-icon>delete</v-icon>
      </v-btn>
      <v-list-item-title>
        ID: {{stock.id}} | {{stock.name}} {{stock.lastTerm}}
      </v-list-item-title>
    </template>
    <!--Информация об акции-->
    <v-list-group sub-group no-action color="indigo">
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>Информация об акции</v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-list-item-title>Название акции: {{stock.name}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Спонсор: {{stock.sponsor}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Скидка: {{stock.discount}} %</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Срок действия до: {{stock.lastTerm}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Дата обновления записи: {{stock.lastUpdateRow}}</v-list-item-title>
      </v-list-item>
    </v-list-group>
    <!---------------------------------------------------------------------------------->
  </v-list-group>
</template>

<script>
export default {
  name: "Stock",
  props: ['stock', 'modeEdit', 'modeDel', 'updater'],
  methods: {
    showEditForm() {
      this.updater(
          {infoItem:this.stock, showEdit: true, showDel: false}
      )
    },
    showDelForm() {
      this.updater(
          {infoItem:this.stock, showEdit: false, showDel: true}
      )
    }
  }
}
</script>

<style scoped>

</style>