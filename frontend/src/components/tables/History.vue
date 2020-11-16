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
        ID: {{history.id}} | {{history.sum}} UAH | {{history.date}}
      </v-list-item-title>
    </template>
    <v-list-group sub-group no-action color="indigo">
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>Информация о пополнение</v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-card-text>
          Сумма оплаты: {{history.sum}} UAH <br>
          Вид оплаты: {{history.paymentType}} <br>
          <span v-if="history.paymentType === 'Кредитной картой'">
            Кредитная карта: {{history.creditCard}} <br>
          </span>
          Дата пополнения: {{history.date}}
        </v-card-text>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Дата обновления записи: {{history.lastUpdateRow}}</v-list-item-title>
      </v-list-item>
    </v-list-group>
  </v-list-group>
</template>

<script>
export default {
  name: "History",
  props: ['history', 'modeEdit', 'modeDel', 'updater'],
  methods: {
    showEditForm() {
      this.updater(
          {infoItem:this.history, showEdit: true, showDel: false}
      )
    },
    showDelForm() {
      this.updater(
          {infoItem:this.history, showEdit: false, showDel: true}
      )
    }
  }
}
</script>

<style scoped>

</style>