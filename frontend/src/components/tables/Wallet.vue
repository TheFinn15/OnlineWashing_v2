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
        ID: {{wallet.id}} | {{wallet.balance}} UAH
      </v-list-item-title>
    </template>
    <!--Информация об чеке-->
    <v-list-group sub-group no-action color="indigo">
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>Информация о кошельке</v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-list-group sub-group no-action color="indigo" v-if="wallet.histories !== undefined">
          <template v-slot:activator>
            <v-list-item-title>
              История пополнений кошелька
            </v-list-item-title>
          </template>
          <div>
            <v-list-item v-for="(item, i) in wallet.histories" :key="i">
              <v-list-item-title>
                ID: {{item.id}} <br>
                Сумма пополнений: {{item.sum}} <br>
                Дата пополнений: {{item.date}} <br>
                Дата изменения записи: {{item.lastUpdateRow}}
              </v-list-item-title>
            </v-list-item>
          </div>
        </v-list-group>
        <v-list-item v-else>
          <v-list-item-title>
            Пополнения отсутствуют
          </v-list-item-title>
        </v-list-item>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Дата обновления записи: {{wallet.lastUpdateRow}}</v-list-item-title>
      </v-list-item>
    </v-list-group>
    <!---------------------------------------------------------------------------------->
  </v-list-group>
</template>

<script>
export default {
  name: "Wallet",
  props: ['wallet', 'modeEdit', 'modeDel', 'updater'],
  methods: {
    showEditForm() {
      this.updater(
          {infoItem:this.wallet, showEdit: true, showDel: false}
      )
    },
    showDelForm() {
      this.updater(
          {infoItem:this.wallet, showEdit: false, showDel: true}
      )
    }
  }
}
</script>

<style scoped>

</style>