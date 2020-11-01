<template>
  <v-list-group>

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