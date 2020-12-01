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
      <MachineList :locales="curLocale" :machines="info.machines" v-if="!recomends" :update-cart="updateCartBuyItem"/>
      <MachineList :locales="curLocale" :machines="recomendsInfo.machines" v-else :update-cart="updateCartBuyItem"/>
    </v-card>
    <v-dialog v-model="showCartDialog" max-width="650" v-if="cartItems.length > 0">
      <template v-slot:activator="{on, attrs}">
        <v-btn
            icon
            x-large
            color="success"
            bottom right absolute
            v-on="on"
            v-bind="attrs"
            outlined
            rounded
            title="Ваша корзина"
            elevation="10"
        >
          <v-icon>
            shopping_cart
          </v-icon>
        </v-btn>
      </template>
      <v-card style="overflow-y: auto; max-height: 340px">
        <v-card-title style="justify-content: center; display: flex">
          <v-badge color="indigo" :content="cartItems.length">
            Ваша корзина
          </v-badge>
          <v-spacer></v-spacer>
          <v-btn icon title="Очистить всю корзину" @click="clearCart">
            <v-icon>
              remove_shopping_cart
            </v-icon>
          </v-btn>
        </v-card-title>
        <v-divider></v-divider>
        <CartItemList
            :items="cartItems"
            :locales="curLocale"
            :all-cart="cartItems"
            :update-cart-remove-item="updateCartRemoveItem"
        />
      </v-card>
    </v-dialog>
  </v-app>
</template>

<script>
import MachineList from "@/components/MachineList";
import CartItemList from "@/components/Cart/CartItemList";
const axios = require('axios')
const ip =  "localhost"
const port = "25016"

export default {
  name: "Home",
  components: {
    CartItemList,
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
      showCartDialog: false,
      recomends: false,
      recomendsInfo: {
        machines: [],
        additional: []
      },
      cartItems: []
    }
  },
  methods: {
    updateCartRemoveItem(info) {
      this.cartItems = info.items
    },
    updateCartBuyItem(info) {
      for (let item of info.items) {
        this.cartItems.push(item)
      }
      const filteredArr = this.cartItems.reduce((acc, current) => {
        const x = acc.find(item => item.id === current.id);
        if (!x) {
          return acc.concat([current]);
        } else {
          return acc;
        }
      }, []);
      this.cartItems = filteredArr
    },
    clearCart() {
      axios({
        method: 'PUT',
        url: `http://${ip}:${port}/api/persons`,
        data: {
          machine: []
        },
        headers: {
          Authorization: 'Bearer ' + localStorage['uid']
        }
      }).then(() => {
        this.cartItems = []
        this.showCartDialog = false
      })
    },
    clearSearch() {
      this.searchVal = ''
      axios.get(`http://${ip}:${port}/api/machines`)
          .then(resp => (this.info.machines = resp.data))
    },
    searchMachine() {
      if (this.searchVal !== '') {
        if (this.recomends === true) {
          axios.get(`http://${ip}:${port}/api/persons/recommends`, {
            headers: {
              Authorization: 'Bearer ' + localStorage['uid']
            }
          })
              .then(resp => {
                console.log(this.searchVal.toLowerCase())
                let regex = new RegExp(this.searchVal.toLowerCase(), 'i')
                this.recomendsInfo.machines = resp.data.filter(i => regex.test(i.name.toLowerCase()))
              })
        } else {
          axios.get(`http://${ip}:${port}/api/machines`)
              .then(resp => {
                this.info.machines = resp.data.filter(i => {
                  console.log(i.name.toLowerCase())
                  return new RegExp(this.searchVal.toLowerCase(), 'i').test(i.name.toLowerCase())
                })
              })
        }
      } else {
        if (this.recomends === true) {
          axios.get(`http://${ip}:${port}/api/persons/recommends`, {
            headers: {
              Authorization: 'Bearer ' + localStorage['uid']
            }
          })
              .then(resp => {
                this.recomendsInfo.machines = resp.data[1]
                this.recomendsInfo.additional = resp.data[2]
              })
        } else {
          axios.get(`http://${ip}:${port}/api/machines`)
              .then(resp => (this.info.machines = resp.data))
        }
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
    if (localStorage['uid'] !== undefined) {
      axios({
        method: 'GET',
        url: `http://${ip}:${port}/api/persons`,
        headers: {
          Authorization: 'Bearer ' + localStorage['uid']
        }
      }).then(resp => {
        this.cartItems = resp.data.machine
      })
    }
  }
}
</script>

<style scoped>

</style>