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
          <v-list-item-title>
            {{locale.tabs.tab1.context.fullInfoTable.person.user.title}}
          </v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.person.user.labels[0]}} {{person.login}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.person.user.labels[1]}} {{person.pwd}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.person.user.labels[2]}} {{person.email}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.person.user.labels[3]}} {{person.phone}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.person.user.labels[4]}} {{person.lastUpdateRow}}</v-list-item-title>
      </v-list-item>
    </v-list-group>
    <!---------------------------------------------------------------------------------->
    <!--Отображение баланса-->
    <v-list-group sub-group no-action>
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.person.wallet.title}} | ID: {{person.wallet.id}} | {{person.wallet.balance === null?info.wallet.balance:0}} UAH</v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-group sub-group no-action v-if="person.wallet.historyTransactions.length > 0">
        <template v-slot:activator>
          <v-list-item-content>
            <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.person.wallet.history}}</v-list-item-title>
          </v-list-item-content>
        </template>
        <v-list-item-group v-for="(history, j) in person.wallet.historyTransactions" :key="j">
          <v-list-item>
            <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.person.wallet.labels[0]}} {{history.sum}}</v-list-item-title>
            <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.person.wallet.labels[1]}} {{history.date}}</v-list-item-title>
            <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.person.wallet.labels[2]}} {{history.lastUpdateRow}}</v-list-item-title>
          </v-list-item>
        </v-list-item-group>
      </v-list-group>
      <v-list-item else>
        <v-list-item-title>
          {{locale.tabs.tab1.context.fullInfoTable.person.wallet.notFound}}
        </v-list-item-title>
      </v-list-item>
    </v-list-group>
    <!--Отображение стиральных машин связанных с юзером-->
    <v-list-group sub-group no-action v-if="person.machine.length > 0">
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>
            {{locale.tabs.tab1.context.fullInfoTable.person.machine.title}}
          </v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-group sub-group no-action v-for="(item, i) in person.machine" :key="i">
        <template v-slot:activator>
          <v-list-item-content>
            <v-list-item-title>ID: {{item.id}} | {{item.name}} | {{item.capacity}} л.</v-list-item-title>
          </v-list-item-content>
        </template>
        <v-list-item>
          <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.person.machine.labels[0]}} {{item.capacity}} л.</v-list-item-title>
        </v-list-item>
        <v-list-item>
          <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.person.machine.labels[1]}} {{item.description}}</v-list-item-title>
        </v-list-item>
        <v-list-item>
          <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.person.machine.labels[2]}} {{item.price}} UAH.</v-list-item-title>
        </v-list-item>
        <v-list-item>
          <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.person.machine.labels[3]}} {{item.lastUpdateRow}}</v-list-item-title>
        </v-list-item>
      </v-list-group>
    </v-list-group>
    <v-list-group sub-group no-action v-else>
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>
            {{locale.tabs.tab1.context.fullInfoTable.person.machine.title}}
          </v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-list-item-title>
          {{locale.tabs.tab1.context.fullInfoTable.person.machine.notFound}}
        </v-list-item-title>
      </v-list-item>
    </v-list-group>
  </v-list-group>
</template>

<script>
export default {
  name: "Person",
  props: ['person', 'modeEdit', 'modeDel', 'updater', 'locales'],
  methods: {
    showEditForm() {
      this.updater(
          {infoItem:this.person, showEdit: true, showDel: false}
      )
    },
    showDelForm() {
      this.updater(
          {infoItem:this.person, showEdit: false, showDel: true}
      )
    }
  }
}
</script>

<style scoped>

</style>