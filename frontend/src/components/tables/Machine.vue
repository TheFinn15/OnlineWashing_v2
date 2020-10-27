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
        ID: {{machine.id}} | {{machine.name}} {{machine.capacity}}
      </v-list-item-title>
    </template>
    <!--Информация о стиральной машине-->
    <v-list-group sub-group no-action color="indigo">
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>Информация о стиральной машине</v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-list-item-title>Процесс стирки: {{machine.percentReady}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Описание: {{machine.description}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Статус машини: {{machine.status}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Цена за 1 кг: {{machine.price}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Дата обновления записи: {{machine.lastUpdateRow}}</v-list-item-title>
      </v-list-item>
    </v-list-group>
    <!--Отображение скидок на стиральных машин-->
    <v-list-group sub-group no-action v-if="machine.stock !== null">
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>ID: {{machine.stock.id}} {{machine.stock.name}} {{machine.stock.lastTerm}}</v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-list-item-title>Спонсор: {{machine.stock.sponsor}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Сумма скидки: {{machine.stock.discount}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Дата измений строки: {{person.machine.lastUpdateRow}}</v-list-item-title>
      </v-list-item>
    </v-list-group>
    <v-list-group sub-group no-action v-else>
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>Информация о скидках на стиральные машини</v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-list-item-title>Машина не имеет скидок</v-list-item-title>
      </v-list-item>
    </v-list-group>
  </v-list-group>
</template>

<script>
export default {
  name: "Machine",
  props: ['machine', 'modeEdit', 'modeDel', 'updater'],
  methods: {
    showEditForm() {
      console.log('here')
      this.updater(
          {infoItem:this.machine, showEdit: true, showDel: false}
      )
    },
    showDelForm() {
      this.updater(
          {infoItem:this.machine, showEdit: false, showDel: true}
      )
    }
  }
}
</script>

<style scoped>

</style>