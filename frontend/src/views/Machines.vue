<template>
  <v-app>
    <v-card style="margin: 6% 15% 0 15%">
      <v-text-field
          @input="searchMachine"
          v-model="searchVal"
          :label="curLocale.searchLabel"
          prepend-inner-icon="search"
          rounded
      >
        <v-icon @click="clearSearch" slot="append">
          close
        </v-icon>
      </v-text-field>
    </v-card>
    <v-card style="margin: 2% 5% 0 5%" flat>
      <v-card-title v-if="!recomends">{{curLocale.machines.cardTitle}}</v-card-title>
      <v-card-title v-else>{{curLocale.machines.recomendTitle}}</v-card-title>
      <MachineList :locales="curLocale" :machines="info.machines" v-if="!recomends"/>
      <MachineList :locales="curLocale" :machines="recomendsInfo.machines" v-else/>
    </v-card>
  </v-app>
</template>

<script>
import MachineList from "@/components/MachineList";
const axios = require('axios')
const ip =  "localhost"
const port = "25016"

export default {
  name: "Home",
  components: {
    MachineList
  },
  data() {
    return {
      info: {
        machines: []
      },
      curLocale: null,
      locales: {
        'en-EN': {
          searchLabel: 'Input name of washing machine',
          machines: {
            cardTitle: 'Available machines:',
            recomendTitle: 'Recommendations:',
            status: 'Status machine:',
            capacity: 'Capacity:',
            litres: 'L.',
            currency: 'uah.',
            description: 'Description:',
            price: 'Price of 1 kg:',
            labels: [
                'Machine is successfully ordered',
                'Error of order',
                'You already ordered this machine!'
            ],
            btnTitle: 'To order'
          }
        },
        'ru-RU': {
          searchLabel: 'Введите название стиральной машини',
          machines: {
            cardTitle: 'Доступные машини:',
            recomendTitle: 'Рекомендации:',
            status: 'Статус машини:',
            capacity: 'Вместимость:',
            litres: 'л.',
            currency: 'грн.',
            description: 'Описание:',
            price: 'Цена за 1 кг:',
            labels: [
              'Машина успешно заказа',
              'Ошибка заказа',
              'Вы уже заказали эту машину!'
            ],
            btnTitle: 'Заказать'
          }
        },
        'ua-UA': {
          searchLabel: 'Введіть назву пральної машини',
          machines: {
            cardTitle: 'Доступні машини:',
            recomendTitle: 'Рекомендації:',
            status: 'Статус машини:',
            capacity: 'Місткість:',
            litres: 'л.',
            currency: 'грн.',
            description: 'Опис:',
            price: 'Ціна за 1 кг:',
            labels: [
              'Машина успішно замовлена',
              'Помилка при замовленні',
              'Ви вже замовили цю машину!'
            ],
            btnTitle: 'Замовити'
          }
        }
      },
      searchVal: '',
      searchItems: null,
      recomends: false,
      recomendsInfo: {
        machines: [],
        additional: []
      }
    }
  },
  methods: {
    clearSearch() {
      this.searchVal = ''
      axios.get(`http://${ip}:${port}/api/machines`)
          .then(resp => (this.info.machines = resp.data))
    },
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
    axios({
      method: 'GET',
      url: `http://${ip}:${port}/api/machines`
    })
      .then(resp => {
        axios.get(`http://${ip}:${port}/api/persons/recommends`, {
          headers: {
            Authorization: 'Bearer ' + localStorage['uid']
          }
        }).then(recomend => {
          this.recomendsInfo.machines = recomend.data[1]
          this.recomendsInfo.additional = recomend.data[2]
          this.recomends = true
        }).catch(() => {
          this.info.machines = resp.data
        })
      })
  }
}
</script>

<style scoped>

</style>