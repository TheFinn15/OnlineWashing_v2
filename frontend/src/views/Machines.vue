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
      <v-card-title v-else @click="showReceiptDialog = true">{{curLocale.machines.recomendTitle}}</v-card-title>
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
            :title="curLocale.machines.cart.title"
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
            {{curLocale.machines.cart.title}}
          </v-badge>
          <v-spacer></v-spacer>
          <v-btn icon :title="curLocale.machines.cart.removeTip" @click="clearCart">
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
            :add-additional="updateAdditional"
        />
      </v-card>
      <v-card>
        <v-card-title>
          {{ curLocale.machines.cart.totalPrice}}
          <v-spacer></v-spacer>
          {{getTotalPrice}} {{ curLocale.machines.cart.cartLabels[3]}}
          <v-btn color="indigo" bottom right dark block @click="doCheckout">
            {{curLocale.machines.cart.btnTitle}}
          </v-btn>
        </v-card-title>
      </v-card>
    </v-dialog>
    <v-dialog v-model="showReceiptDialog" max-width="650">
      <v-card>
<!--        TODO: Циклично отображать данные для каждой машини в корзине(tabs)-->
        <v-card-title style="justify-content: center; display: flex">
          {{curLocale.machines.confirmOrder.title}}
        </v-card-title>
        <v-tabs>
          <v-tab v-for="(item, i) in this.cartItems" :key="i">
           {{i+1}}# - {{item.name}}
          </v-tab>
          <v-tab>
            {{curLocale.machines.confirmOrder.tabName}}
          </v-tab>
          <v-tab-item v-for="(item, i) in this.cartItems" :key="i">
            <v-container>
              <v-row>
                <v-col cols="4">
                  <v-img :src="item.image" width="50" height="50"></v-img>
                </v-col>
                <v-col cols="8">
                  <v-text-field
                      :label="curLocale.machines.confirmOrder.machine[0]"
                      v-model="item.name"
                      filled
                      rounded
                  ></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field
                      :label="curLocale.machines.confirmOrder.machine[1]"
                      v-model="item.description"
                      filled
                      rounded
                  ></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field
                      :label="curLocale.machines.confirmOrder.machine[2]"
                      v-model="item.price"
                      filled
                      rounded
                  ></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field
                      :label="curLocale.machines.confirmOrder.machine[3]"
                      v-model="item.capacity"
                      filled
                      rounded
                  ></v-text-field>
                </v-col>
              </v-row>
            </v-container>
          </v-tab-item>
          <v-tab-item>
            <v-container>
              <v-row>
                <v-col cols="6">
                  <v-select
                      :label="curLocale.machines.confirmOrder.infoReceipt.receiptLabels[0]"
                      :items="curLocale.machines.confirmOrder.infoReceipt.receiptLabels[1]"
                      outlined
                      v-model="receiptInfo.paymentType"
                  ></v-select>
                </v-col>
                <v-col cols="6" v-if="receiptInfo.paymentType === curLocale.machines.confirmOrder.infoReceipt.receiptLabels[2]">
                  <v-text-field
                      :label="curLocale.machines.confirmOrder.infoReceipt.receiptLabels[3]"
                      outlined
                      v-model="receiptInfo.creditCard"
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                      :label="curLocale.machines.confirmOrder.infoReceipt.receiptLabels[4]"
                      type="number"
                      outlined
                      v-model="receiptInfo.volume"
                  ></v-text-field>
                </v-col>
              </v-row>
            </v-container>
          </v-tab-item>
        </v-tabs>
        <v-card flat>
          <v-card-title>
            {{curLocale.machines.confirmOrder.infoReceipt.totalPrice}}
            <v-spacer></v-spacer>
            {{getTotalPrice}} UAH
          </v-card-title>
        </v-card>
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
            confirmOrder: {
              title: 'Checkout',
              tabName: 'Data to pay',
              machine: [
                  'Name:',
                  'Description:',
                  'Price:',
                  'Capacity:'
              ],
              infoReceipt: {
                receiptLabels: [
                    'Type of pay:',
                    ['Cash', 'Credit card'],
                    'Number credit card',
                    'Volume of things:'
                ],
                totalPrice: 'Total to pay'
              }
            },
            labels: [
                'Machine is successfully ordered',
                'Error of order',
                'You already ordered this machine!'
            ],
            btnTitle: 'To order',
            cart: {
              title: 'Your cart',
              removeTip: 'Clear all cart',
              cartLabels: [
                  'Description:',
                  'Status:',
                  'L.',
                  'UAH',
                'Choose additional:'
              ],
              totalPrice: 'Total to price:',
              btnTitle: 'CHECKOUT'
            }
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
            confirmOrder: {
              title: 'Оформить заказ',
              tabName: 'Данные к оплате',
              machine: [
                'Название:',
                'Описание:',
                'Цена:',
                'Вместимость:'
              ],
              infoReceipt: {
                receiptLabels: [
                  'Вид оплаты:',
                  ['Наличные', 'Кредитная карта'],
                  'Номер кредитной карты',
                  'Объем вещей:'
                ],
                totalPrice: 'Всего к оплате'
              }
            },
            labels: [
              'Машина успешно заказа',
              'Ошибка заказа',
              'Вы уже заказали эту машину!'
            ],
            btnTitle: 'Заказать',
            cart: {
              title: 'Ваша корзина',
              removeTip: 'Очистить всю корзину',
              cartLabels: [
                'Описание:',
                'Статус:',
                'л.',
                'ГРН',
                'Выберите добавки:'
              ],
              totalPrice: 'Всего к оплате:',
              btnTitle: 'ОФОРМИТЬ ЗАКАЗ'
            }
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
            confirmOrder: {
              title: 'Оформити замовлення',
              tabName: 'Дані для сплати',
              machine: [
                'Назва:',
                'Опис:',
                'Ціна:',
                'Місткість:'
              ],
              infoReceipt: {
                receiptLabels: [
                  'Вид сплати:',
                  ['Готівкою', 'Кредитной картою'],
                  'Номер кредитної карти',
                  'Об`єм речей:'
                ],
                totalPrice: 'Усього до сплати'
              }
            },
            labels: [
              'Машина успішно замовлена',
              'Помилка при замовленні',
              'Ви вже замовили цю машину!'
            ],
            btnTitle: 'Замовити',
            cart: {
              title: 'Ваш кошук',
              removeTip: 'Очистити весь кошик',
              cartLabels: [
                'Опис:',
                'Статус:',
                'л.',
                'ГРН',
                'Виберіть добавки:'
              ],
              totalPrice: 'Усього до сплати:',
              btnTitle: 'ОФОРМИТИ ЗАМОВЛЕННЯ'
            }
          }
        }
      },
      searchVal: '',
      showCartDialog: false,
      showReceiptDialog: false,
      recomends: false,
      recomendsInfo: {
        machines: [],
        additional: []
      },
      cartItems: [],
      receiptInfo: {
        person: {
          fName: '',
          sName: ''
        },
        machine: {},
        price: this.getTotalPrice,
        volume: 0,
        additional: [],
        paymentType: '',
        creditCard: '',
        date: new Date().toISOString().split('T')[0]
      }
    }
  },
  computed: {
    mappedCartItems() {
      console.log(this.cartItems)
      return this.cartItems.map(i => i.id + ' | ' + i.name + ' | ' + i.capacity + ' ' + this.curLocale.machines.cart.cartLabels[2])
    },
    getTotalPrice() {
      let total = 0
      for (let item of this.cartItems) {
        total += item.price
      }
      return total
    }
  },
  methods: {
    doCheckout() {

    },
    updateAdditional(info) {
      this.cartItems['additional'] = info.items
    },
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
    let tokenInfo = JSON.parse(atob(localStorage['uid'].split('.')[1]))
    let nowDate = Math.round(+new Date(new Date().toLocaleString('ua-UA', {timeZone: 'Europe/Kiev'})) / 1000)
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
    if (localStorage['uid'] !== undefined && tokenInfo.exp !== nowDate) {
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