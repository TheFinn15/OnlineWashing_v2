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
        ID: {{draft.id}} | {{draft.price}} UAH | {{draft.date}}
      </v-list-item-title>
    </template>
    <!--Информация об чеке-->
    <v-list-group sub-group no-action color="indigo">
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>
            {{locale.tabs.tab1.context.fullInfoTable.draft.title}}
          </v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-list-group sub-group no-action color="indigo">
          <template v-slot:activator>
            <v-list-item-title>
              {{locale.tabs.tab1.context.fullInfoTable.draft.person[0]}} ID: {{draft.person.id}} | {{draft.person.fName}} {{draft.person.sName}}
            </v-list-item-title>
          </template>
          <v-list-item>
            <v-list-item-title>
              {{locale.tabs.tab1.context.fullInfoTable.draft.person[1]}} {{draft.person.wallet.balance}} UAH
            </v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title>
              {{locale.tabs.tab1.context.fullInfoTable.draft.person[2]}} {{draft.person.login}}
            </v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title>
              {{locale.tabs.tab1.context.fullInfoTable.draft.person[3]}} {{draft.person.email}}
            </v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title>
              {{locale.tabs.tab1.context.fullInfoTable.draft.person[4]}} {{draft.person.phone}}
            </v-list-item-title>
          </v-list-item>
        </v-list-group>
      </v-list-item>
      <v-list-item>
        <v-list-group sub-group no-action color="indigo">
          <template v-slot:activator>
            <v-list-item-title>
              {{locale.tabs.tab1.context.fullInfoTable.draft.machine[0]}} ID: {{draft.machine.id}} | {{draft.machine.name}} | {{draft.machine.capacity}}
            </v-list-item-title>
          </template>
          <v-list-item>
            <v-list-item-title>
              {{locale.tabs.tab1.context.fullInfoTable.draft.machine[1]}} {{draft.machine.price}}
            </v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title>
              {{locale.tabs.tab1.context.fullInfoTable.draft.machine[2]}} {{draft.machine.description}}
            </v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title>
              {{locale.tabs.tab1.context.fullInfoTable.draft.machine[3]}} {{draft.machine.status}}
            </v-list-item-title>
          </v-list-item>
        </v-list-group>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.draft.labels[0]}} {{draft.price}} UAH</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.draft.labels[1]}} {{draft.volume}} кг</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>
          {{locale.tabs.tab1.context.fullInfoTable.draft.labels[2]}} {{draft.paymentType}}
        </v-list-item-title>
        <v-list-item-subtitle v-if="draft.paymentType === locale.tabs.tab1.context.fullInfoTable.draft.labels[3]">
          {{locale.tabs.tab1.context.fullInfoTable.draft.labels[4]}} {{draft.creditCard}}
        </v-list-item-subtitle>
      </v-list-item>
      <v-list-item>
        <v-list-group sub-group no-action color="indigo">
          <template v-slot:activator>
            <v-list-item-title>
              {{locale.tabs.tab1.context.fullInfoTable.draft.labels[5]}}
            </v-list-item-title>
          </template>
          <v-list-item v-for="(item, i) in draft.additional" :key="i">
            <v-list-item-title>
              {{item}}
            </v-list-item-title>
          </v-list-item>
        </v-list-group>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.draft.labels[6]}} {{draft.lastUpdateRow}}</v-list-item-title>
      </v-list-item>
    </v-list-group>
    <!---------------------------------------------------------------------------------->
  </v-list-group>
</template>

<script>
export default {
  name: "Draft",
  props: ['draft', 'modeEdit', 'modeDel', 'updater', 'locales'],
  methods: {
    showEditForm() {
      this.updater(
          {infoItem:this.draft, showEdit: true, showDel: false}
      )
    },
    showDelForm() {
      this.updater(
          {infoItem:this.draft, showEdit: false, showDel: true}
      )
    }
  }
}
</script>

<style scoped>

</style>