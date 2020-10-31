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
          <v-list-item-title>Информация о чеке</v-list-item-title>
        </v-list-item-content>
      </template>
      <v-list-item>
        <v-list-group sub-group no-action color="indigo">
          <template v-slot:activator>
            <v-list-item-title>
              Пользователь: ID: {{draft.person.id}} | {{draft.person.fName}} {{draft.person.sName}}
            </v-list-item-title>
          </template>
          <v-list-item>
            <v-list-item-title>
              Текущий баланс: {{draft.person.wallet.balance}} UAH
            </v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title>
              Логин: {{draft.person.login}}
            </v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title>
              E-mail: {{draft.person.email}}
            </v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title>
              Телефон: {{draft.person.phone}}
            </v-list-item-title>
          </v-list-item>
        </v-list-group>
      </v-list-item>
      <v-list-item>
        <v-list-group sub-group no-action color="indigo">
          <template v-slot:activator>
            <v-list-item-title>
              Стиральная машина: ID: {{draft.machine.id}} | {{draft.machine.name}} | {{draft.machine.capacity}}
            </v-list-item-title>
          </template>
          <v-list-item>
            <v-list-item-title>
              Цена за 1 кг: {{draft.machine.price}}
            </v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title>
              Описание: {{draft.machine.description}}
            </v-list-item-title>
          </v-list-item>
          <v-list-item>
            <v-list-item-title>
              Текущий статус: {{draft.machine.status}}
            </v-list-item-title>
          </v-list-item>
        </v-list-group>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Уплаченная цена: {{draft.price}}</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>Объем вещей: {{draft.volume}} кг</v-list-item-title>
      </v-list-item>
      <v-list-item>
        <v-list-item-title>
          Вид оплаты: {{draft.paymentType}}
        </v-list-item-title>
        <v-list-item-subtitle v-if="draft.paymentType === 'Картой'">
          Кредитная карта: {{draft.creditCard}}
        </v-list-item-subtitle>
      </v-list-item>
      <v-list-item>
        <v-list-group sub-group no-action color="indigo">
          <template v-slot:activator>
            <v-list-item-title>
              Хим. добавки к стирке
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
        <v-list-item-title>Дата обновления записи: {{draft.lastUpdateRow}}</v-list-item-title>
      </v-list-item>
    </v-list-group>
    <!---------------------------------------------------------------------------------->
  </v-list-group>
</template>

<script>
export default {
  name: "Draft",
  props: ['draft', 'modeEdit', 'modeDel', 'updater'],
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