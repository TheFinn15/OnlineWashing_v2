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
          <v-list-item-title>
            {{locale.tabs.tab1.context.fullInfoTable.histories.title}}
          </v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-card-text>
          {{locale.tabs.tab1.context.fullInfoTable.histories.labels[0]}} {{history.sum}} UAH <br>
          {{locale.tabs.tab1.context.fullInfoTable.histories.labels[1]}} {{history.paymentType}} <br>
          <span v-if="history.paymentType === locale.tabs.tab1.context.fullInfoTable.histories.labels[2]">
            {{locale.tabs.tab1.context.fullInfoTable.histories.labels[2]}} {{history.creditCard}} <br>
          </span>
          {{locale.tabs.tab1.context.fullInfoTable.histories.labels[3]}} {{history.date}}
        </v-card-text>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.histories.labels[4]}} {{history.lastUpdateRow}}</v-list-item-title>
      </v-list-item>
    </v-list-group>
  </v-list-group>
</template>

<script>
export default {
  name: "History",
  props: ['history', 'modeEdit', 'modeDel', 'updater', 'locales'],
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