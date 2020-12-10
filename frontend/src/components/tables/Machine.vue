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
          <v-list-item-title>
            {{locale.tabs.tab1.context.fullInfoTable.machine.machine.title}}
          </v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.machine.machine.labels[0]}} {{machine.percentReady}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.machine.machine.labels[1]}} {{machine.description}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.machine.machine.labels[2]}} {{machine.status}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.machine.machine.labels[3]}} {{machine.price}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.machine.machine.labels[4]}} {{machine.lastUpdateRow}}</v-list-item-title>
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
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.machine.stock.labels[0]}} {{machine.stock.sponsor}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.machine.stock.labels[1]}} {{machine.stock.discount}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.machine.stock.labels[2]}} {{person.machine.lastUpdateRow}}</v-list-item-title>
      </v-list-item>
    </v-list-group>
    <v-list-group sub-group no-action v-else>
      <template v-slot:activator>
        <v-list-item-content>
          <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.machine.stock.title}}</v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-list-item-title>{{locale.tabs.tab1.context.fullInfoTable.machine.stock.notFound}}</v-list-item-title>
      </v-list-item>
    </v-list-group>
  </v-list-group>
</template>

<script>
export default {
  name: "Machine",
  props: ['machine', 'modeEdit', 'modeDel', 'updater', 'locales'],
  methods: {
    showEditForm() {
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