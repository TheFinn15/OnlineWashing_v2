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
        ID: {{person.id}} | {{person.fName}} {{person.sName}}
      </v-list-item-title>
    </template>
    <!--Информация о юзере-->
    <v-list-group sub-group no-action color="indigo">
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>Информация о пользователе</v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-list-item-title>Логин: {{person.login}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Пароль: {{person.pwd}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Е-маил: {{person.email}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Телефон: {{person.phone}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Дата обновления записи: {{person.lastUpdateRow}}</v-list-item-title>
      </v-list-item>
    </v-list-group>
    <!---------------------------------------------------------------------------------->
    <!--Отображение баланса-->
    <v-list-group sub-group no-action>
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>ID: {{person.wallet.id}} | {{person.wallet.balance === null?info.wallet.balance:0}} UAH</v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-group sub-group no-action v-if="person.wallet.historyTransactions.length > 0">
        <template v-slot:activator>
          <v-list-item-content>
            <v-list-item-title v-if="person.wallet.historyTransactions !== null">История пополнений</v-list-item-title>
          </v-list-item-content>
        </template>
        <v-list-item-group v-for="(history, j) in person.wallet.historyTransactions" :key="j">
          <v-list-item>
            <v-list-item-title>Сумма пополнения: {{history.sum}}</v-list-item-title>
            <v-list-item-title>Дата пополнения: {{history.date}}</v-list-item-title>
            <v-list-item-title>Дата изменения строки: {{history.lastUpdateRow}}</v-list-item-title>
          </v-list-item>
        </v-list-item-group>
      </v-list-group>
      <v-list-item else>
        <v-list-item-title>Пополнений не найдено</v-list-item-title>
      </v-list-item>
    </v-list-group>
    <!--Отображение стиральных машин связанных с юзером-->
    <v-list-group sub-group no-action v-if="person.machine.length > 0">
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>{{person.machine.name}} {{person.machine.capacity}}</v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-list-item-title>Описание: {{person.machine.description}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Цена: {{person.machine.price}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Дата измений строки: {{person.machine.lastUpdateRow}}</v-list-item-title>
      </v-list-item>
    </v-list-group>
    <v-list-group sub-group no-action v-else>
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>Информация о стиральных машинах</v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-list-item-title>Стиральная машина не используется</v-list-item-title>
      </v-list-item>
    </v-list-group>
  </v-list-group>
</template>

<script>
export default {
  name: "Person",
  props: ['person', 'modeEdit', 'modeDel', 'updater'],
  methods: {
    showEditForm() {
      console.log('here')
      this.updater(
          {infoItem:this.person, showEdit: true, showDel: false}
      )
      // this.itemInfo = this.curPerson;
      // this.showEdit = true;
    },
    showDelForm() {
      this.updater(
          {infoItem:this.person, showEdit: false, showDel: true}
      )
      // this.itemInfo = this.curPerson;
      // this.showDel = true;
    }
  }
}
</script>

<style scoped>

</style>