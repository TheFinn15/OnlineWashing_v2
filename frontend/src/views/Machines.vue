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
          <v-btn color="indigo" bottom right dark block @click="doConfirm">
            {{curLocale.machines.cart.btnTitle}}
          </v-btn>
        </v-card-title>
      </v-card>
    </v-dialog>
    <v-dialog v-model="showConfirmDialog" max-width="650">
      <v-card>
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
                <v-col cols="6">
                  <v-row no-gutters>
                    <v-col>
                      <v-img :src="item.img" width="150" height="150"></v-img>
                    </v-col>
                    <v-col>
                      <v-row no-gutters>
                        <v-col cols="12">
                          <v-text-field
                              :label="curLocale.machines.confirmOrder.machine[0]"
                              v-model="item.name"
                              filled
                              rounded
                              readonly
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12">
                          <v-text-field
                              :label="curLocale.machines.confirmOrder.machine[3]"
                              v-model="item.capacity"
                              filled
                              rounded
                              readonly
                          ></v-text-field>
                        </v-col>
                        <v-col cols="12">
                          <v-text-field
                              :label="curLocale.machines.confirmOrder.machine[2]"
                              v-model="item.price"
                              filled
                              rounded
                              readonly
                          ></v-text-field>
                        </v-col>
                      </v-row>
                    </v-col>
                  </v-row>
                </v-col>
                <v-col>
                  <v-textarea
                      :label="curLocale.machines.confirmOrder.machine[1]"
                      v-model="item.description"
                      filled
                      rounded
                      readonly
                  ></v-textarea>
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
                <v-col cols="6">
                  <v-text-field
                      :label="curLocale.machines.confirmOrder.infoReceipt.receiptLabels[3]"
                      type="number"
                      outlined
                      v-model="receiptInfo.volume"
                  ></v-text-field>
                </v-col>
                <v-col v-if="receiptInfo.paymentType === curLocale.machines.confirmOrder.infoReceipt.receiptLabels[1][1]">
                  <v-text-field
                      :label="curLocale.machines.confirmOrder.infoReceipt.receiptLabels[2]"
                      outlined
                      type="number"
                      v-model="receiptInfo.creditCard"
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
        <v-btn block outlined color="success" @click="doCheckout">
          ЗАВЕРШИТЬ
        </v-btn>
      </v-card>
    </v-dialog>
    <v-dialog v-model="showReceiptDialog" max-width="650" persistent>
      <v-card>
        <v-tabs>
          <v-tab v-for="(item, i) in allReceipt" :key="i">
            {{curLocale.machines.receipt.subtitle}} №{{item.id}}
          </v-tab>
          <v-tab>
            {{curLocale.machines.receipt.about[0]}}
          </v-tab>
          <v-tab-item v-for="(item, i) in allReceipt" :key="i">
            <v-container>
              <v-row>
                <v-col cols="6">
                  <v-text-field
                      :label="curLocale.machines.receipt.labels[0]"
                      filled
                      rounded
                      readonly
                      v-model="item.machine.name"
                  ></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field
                      :label="curLocale.machines.receipt.labels[1]"
                      filled
                      rounded
                      readonly
                      v-model="item.machine.capacity"
                  ></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field
                      :label="curLocale.machines.receipt.labels[2]"
                      filled
                      rounded
                      readonly
                      v-model="item.machine.price"
                  ></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-select
                      :label="curLocale.machines.receipt.labels[3]"
                      filled
                      rounded
                      readonly
                      v-model="item.additional"
                  ></v-select>
                </v-col>
              </v-row>
            </v-container>
          </v-tab-item>
          <v-tab-item>
            <v-container>
              <v-row>
                <v-col cols="6">
                  <v-text-field
                      :label="curLocale.machines.receipt.about[1]"
                      filled
                      rounded
                      readonly
                      v-model="allReceipt[0].price"
                  ></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field
                      :label="curLocale.machines.receipt.about[2]"
                      filled
                      rounded
                      readonly
                      v-model="allReceipt[0].volume"
                  ></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field
                      :label="curLocale.machines.receipt.about[3]"
                      filled
                      rounded
                      readonly
                      v-model="allReceipt[0].paymentType"
                  ></v-text-field>
                </v-col>
                <v-col cols="6" v-if="allReceipt[0].creditCard === curLocale.machines.receipt.about[4]">
                  <v-text-field
                      :label="curLocale.machines.receipt.about[4]"
                      filled
                      rounded
                      readonly
                      v-model="allReceipt[0].creditCard"
                  ></v-text-field>
                </v-col>
              </v-row>
            </v-container>
          </v-tab-item>
        </v-tabs>
        <v-container>
          <v-btn block @click="showReceiptDialog = false" color="indigo" dark>
            {{curLocale.machines.receipt.btnTitle}}
          </v-btn>
        </v-container>
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
              },
              btnTitle: 'FINISH'
            },
            receipt: {
              subtitle: 'Draft ',
              about: [
                  'About draft',
                  'Total price',
                  'Capacity',
                  'Type of pay',
                  'Credit card'
              ],
              labels: [
                'Name:',
                'Capacity:',
                'Price:',
                'Additional:'
              ],
              btnTitle: 'READY'
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
              },
              btnTitle: 'Завершить'
            },
            receipt: {
              subtitle: 'Чек ',
              about: [
                'О чеке',
                'Всего к оплате',
                'Объем вещей',
                'Вид оплаты',
                'Кредитной картой'
              ],
              labels: [
                'Название:',
                'Вместимость:',
                'Цена:',
                'Добавки:'
              ],
              btnTitle: 'ГОТОВО'
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
              },
              btnTitle: 'Завершити'
            },
            receipt: {
              subtitle: 'Чек ',
              about: [
                'Про чек',
                'Усього до сплати',
                'Об`єм',
                'Вид сплати',
                'Кредитной картою'
              ],
              labels: [
                'Назва:',
                'Місткість:',
                'Ціна:',
                'Додатки:'
              ],
              btnTitle: 'ГОТОВО'
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
      showConfirmDialog: false,
      showReceiptDialog: false,
      recomends: false,
      recomendsInfo: {
        machines: [],
        additional: []
      },
      cartItems: [],
      allReceipt: [
        {
          "id": 66,
          "person": {
            "id": 40,
            "machine": [
              {
                "id": 42,
                "stock": {
                  "id": 25,
                  "name": "Joja",
                  "sponsor": "Jinja",
                  "lastTerm": "04.11.2020, 02:00:00",
                  "discount": 23,
                  "lastUpdateRow": "30-10-2020-21:06:19"
                },
                "name": "SAMSA",
                "capacity": 45,
                "percentReady": 0,
                "description": "Kopec",
                "status": "Рабочая",
                "price": 10,
                "img": null,
                "lastUpdateRow": "27-11-2020-12:54:17"
              },
              {
                "id": 41,
                "stock": null,
                "name": "LG",
                "capacity": 30,
                "percentReady": 0,
                "description": "Kruta",
                "status": "Рабочая",
                "price": 15,
                "img": "data:image/jpeg;base64,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",
                "lastUpdateRow": "24-11-2020-12:54:17"
              }
            ],
            "wallet": {
              "id": 39,
              "balance": 0,
              "historyTransactions": [],
              "lastUpdateRow": "24-11-2020-11:35:16"
            },
            "fName": "Topik",
            "sName": "Tpor",
            "pwd": "$2a$10$9ebKSVjxhyc5zR9cKLU14ON4KwBHERH3u27IYahUrOwYNPx.FqnGu",
            "login": "TheFinn",
            "email": "sda@ma.com",
            "phone": "0394494949",
            "lastUpdateRow": "24-11-2020-11:35:16",
            "avatar": "data:image/jpeg;base64,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                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         ",
            "authorities": [
              {
                "name": "ROLE_USER"
              }
            ]
          },
          "machine": {
            "id": 41,
            "stock": null,
            "name": "LG",
            "capacity": 30,
            "percentReady": 0,
            "description": "Kruta",
            "status": "Рабочая",
            "price": 15,
            "img": "data:image/jpeg;base64,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",
            "lastUpdateRow": "24-11-2020-12:54:17"
          },
          "price": 15,
          "volume": 1,
          "additional": [],
          "paymentType": "Готівкою",
          "creditCard": "оплачено без карты",
          "date": "10-12-2020-23:18:06",
          "lastUpdateRow": "10-12-2020-23:18:06"
        }
      ],
      receiptInfo: {
        person: {
          fName: '',
          sName: ''
        },
        machine: {},
        price: this.getTotalPrice,
        volume: 1,
        additional: [],
        paymentType: '',
        creditCard: '',
        date: new Date().toISOString().split('T')[0]
      }
    }
  },
  computed: {
    getTotalPrice() {
      let total = 0
      for (let item of this.cartItems) {
        total += (item.price*this.receiptInfo.volume)
      }
      return total
    }
  },
  methods: {
    doConfirm() {
      this.showCartDialog = false;
      this.showConfirmDialog = true;

      axios({
        method: 'GET',
        url: `http://${ip}:${port}/api/persons`,
        headers: {
          Authorization: 'Bearer ' + localStorage['uid']
        }
      }).then(user => (this.receiptInfo.person = user.data))
    },
    doCheckout() {
      if (this.receiptInfo.paymentType !== '' && this.receiptInfo.volume !== 0) {
        this.showConfirmDialog = false;
        this.showReceiptDialog = true;

        let res = {};
        res['person'] = {id: this.receiptInfo.person.id}
        for (let item of this.cartItems) {
          res['machine'] = {id: item.id}
          res['price'] = item.price
          res['volume'] = parseInt(this.receiptInfo.volume)
          res['additional'] = this.receiptInfo.additional
          res['paymentType'] = this.receiptInfo.paymentType
          res['creditCard'] = this.receiptInfo.creditCard
          axios({
            method: 'POST',
            url: `http://${ip}:${port}/api/drafts`,
            data: res,
            headers: {
              Authorization: 'Bearer ' + localStorage['uid']
            }
          }).then(resp => {
            axios({
              method: 'GET',
              url: `http://${ip}:${port}/api/drafts/`+resp.data.id,
              headers: {
                Authorization: 'Bearer ' + localStorage['uid']
              }
            }).then(drafts => (this.allReceipt = drafts.data))
            this.allReceipt['price'] = this.getTotalPrice
          }).catch(err => {
            console.error(err)
          })
          this.cartItems = []
        }
      }
    },
    updateAdditional(info) {
      this.receiptInfo.additional = info.items
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
        for (let item of resp.data.machine) {
          if (item.percent_ready === null || item.percent_ready === 100) {
            this.cartItems.push(item)
          }
        }
      })
    }
  }
}
</script>

<style scoped>

</style>