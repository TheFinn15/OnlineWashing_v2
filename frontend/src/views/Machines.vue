<template>
  <v-app>
    <v-card style="margin: 8% 15% 0 15%">
      <v-text-field
          @input="searchMachine"
          v-model="searchVal"
          :label="curLocale.searchLabel"
          rounded
      >
        <v-icon slot="append">search</v-icon>
      </v-text-field>
    </v-card>
    <v-card style="margin: 2% 5% 0 5%" flat>
      <v-card-title>{{curLocale.machines.cardTitle}}</v-card-title>
      <MachineList :locales="curLocale" :machines="info.machines"/>
    </v-card>
  </v-app>
</template>

<script>
import MachineList from "@/components/MachineList";
const axios = require('axios')
const ip = "192.168.0.112"
const port = "9000"

export default {
  name: "Home",
  components: {
    MachineList
  },
  data() {
    return {
      info: {
        machines: null
      },
      curLocale: null,
      locales: {
        'en-EN': {
          searchLabel: 'Input name of washing machine',
          machines: {
            cardTitle: 'Available machines:',
            capacity: 'Capacity',
            litres: 'L.',
            currency: 'uah.',
            description: 'Description',
            price: 'Price of 1 kg',
            btnTitle: 'To order'
          }
        },
        'ru-RU': {
          searchLabel: 'Введите название стиральной машини',
          machines: {
            cardTitle: 'Доступные машини:',
            capacity: 'Вместимость',
            litres: 'л.',
            currency: 'грн.',
            description: 'Описание',
            price: 'Цена за 1 кг',
            btnTitle: 'Заказать'
          }
        },
        'ua-UA': {
          searchLabel: 'Введіть назву пральної машини',
          machines: {
            cardTitle: 'Доступні машини:',
            capacity: 'Місткість',
            litres: 'л.',
            currency: 'грн.',
            description: 'Опис',
            price: 'Ціна за 1 кг',
            btnTitle: 'Замовити'
          }
        }
      },
      searchVal: '',
      searchItems: null
    }
  },
  methods: {
    searchMachine() {
      if (this.searchVal !== '') {
        axios.get(`http://${ip}:${port}/api/machines`)
            .then(resp => (this.info.machines = resp.data.filter(i => new RegExp(this.searchVal.toLowerCase()).test(i.name.toLowerCase()))))
      } else {
        axios.get(`http://${ip}:${port}/api/machines`)
            .then(resp => (this.info.machines = resp.data))
      }
    }
  },
  beforeMount() {
    if (localStorage['lang'] === 'ru-RU') {
      this.curLocale = this.locales["ru-RU"];
    } else if (localStorage['lang'] === 'en-EN') {
      this.curLocale = this.locales["en-EN"];
    } else if (localStorage['lang'] === 'ua-UA') {
      this.curLocale = this.locales["ua-UA"];
    } else {
      localStorage.setItem('lang', 'ua-UA')
      this.curLocale = this.locales["ua-UA"];
    }
  },
  mounted() {
    axios.get(`http://${ip}:${port}/api/machines`)
      .then(resp => (this.info.machines = resp.data))
  }
}
</script>

<style scoped>

</style>