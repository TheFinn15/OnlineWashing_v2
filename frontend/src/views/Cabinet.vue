<template>
  <v-app>
    <v-card style="margin: 8% 15% 0 15%">
      <v-tabs grow color="indigo">
        <v-tab>{{curLocale.tabsNames[0]}}</v-tab>
        <v-tab>{{curLocale.tabsNames[1]}}</v-tab>
        <v-tab>{{curLocale.tabsNames[2]}}</v-tab>
        <v-tab-item>
          <v-card-title>{{curLocale.tabItems[0].title}} {{info.userInfo.fName}} {{info.userInfo.sName}}</v-card-title>
          <v-divider></v-divider>
          <v-container>
            <v-row>
              <v-col>
                <v-img width="240" height="240" :src="info.userInfo.avatar"></v-img>
              </v-col>
              <v-col>
                <v-card-text>
                  <b>{{curLocale.tabItems[0].userInfo[0]}}</b> {{info.userInfo.fName}} <br/>
                  <b>{{curLocale.tabItems[0].userInfo[1]}}</b> {{info.userInfo.sName}} <br/>
                  <b>{{curLocale.tabItems[0].userInfo[2]}}</b> {{info.userInfo.login}} <br/>
                  <b>{{curLocale.tabItems[0].userInfo[3]}}</b> {{info.userInfo.email}} <br/>
                  <b>{{curLocale.tabItems[0].userInfo[4]}}</b> {{info.userInfo.phone}} <br/>
<!--                  <b>{{curLocale.tabItems[0].userInfo[5]}}</b> {{info.userInfo.balance}}-->
                </v-card-text>
              </v-col>
            </v-row>
          </v-container>
          <v-divider></v-divider>
          <v-card flat>
            <v-card-title>{{curLocale.tabItems[0].curOrders.title}}</v-card-title>
            <v-divider></v-divider>
            <v-list v-if="info.userInfo.machine !== null">
              <v-list-group v-for="(item, i) in info.userInfo.machine" :key="i">
                <template v-slot:activator>
                    <v-list-item-title>
                      <v-badge :color="item.status" :title="curLocale.tabItems[0].curOrders.tipForMachine">
                        {{item.name}}
                      </v-badge>
                    </v-list-item-title>
                  <v-item-group>
                    <v-list-item-title>{{curLocale.tabItems[0].curOrders.machine.titleReady}} {{item.percentReady}}%</v-list-item-title>
                  </v-item-group>
                </template>
                <v-list-item>
                  <v-container>
                    <v-row>
                      <v-col>
                        <v-img width="140" height="140" :src="item.img"></v-img>
                      </v-col>
                      <v-col>
                        <v-list-item-title>
                          {{curLocale.tabItems[0].curOrders.machine.listInfo[0]}} {{item.price}} {{curLocale.tabItems[0].curOrders.machine.listInfo[1]}} <br/>
                          {{curLocale.tabItems[0].curOrders.machine.listInfo[2]}} {{item.description}} <br/>
                          {{curLocale.tabItems[0].curOrders.machine.listInfo[3]}} {{item.capacity}}{{curLocale.tabItems[0].curOrders.machine.listInfo[4]}}
                        </v-list-item-title>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-list-item>
              </v-list-group>
            </v-list>
            <div v-else style="margin: 10%">
              <v-icon style="text-align: center;display: block">warning</v-icon>
              <v-card-title style="justify-content: center">{{curLocale.tabItems[0].curOrders.ordersIsEmptyTitle}}</v-card-title>
            </div>
          </v-card>
        </v-tab-item>
        <v-tab-item>
          <v-list v-if="info.drafts !== null">
            <v-card-title>{{curLocale.tabItems[1].title}}</v-card-title>
            <v-divider></v-divider>
            <v-list-group v-for="(item, i) in info.drafts" :key="i">
              <template v-slot:activator>
                <v-list-item-title>
                  {{item.machine.name}}
                </v-list-item-title>
                <v-item-group>
                  <v-list-item-title>{{item.date}}</v-list-item-title>
                </v-item-group>
              </template>
              <v-container>
                <v-row>
                  <v-col>
                    <v-text-field
                        :label="curLocale.tabItems[1].listOrders.paymentType"
                        readonly
                        :value="item.paymentType === 'credit'?curLocale.tabItems[1].listOrders.askForCondition[0]:curLocale.tabItems[1].listOrders.askForCondition[1]"
                    ></v-text-field>
                    <v-text-field
                        v-if="item.paymentType === 'credit'"
                        :label="curLocale.tabItems[1].listOrders.numsCreditCard"
                        readonly
                        :value="item.creditCard"
                    ></v-text-field>
                  </v-col>
                  <v-col>
                    <v-badge :color="item.machine.status" overlap :title="curLocale.tabItems[1].listOrders.tipsForMachine">
                      <v-card rounded style="padding: 5%">
                        <v-row>
                          <v-col>
                            <v-text-field
                                :label="curLocale.tabItems[1].listOrders.nameMachine"
                                readonly
                                :value="item.machine.name"
                            ></v-text-field>
                            <v-text-field
                                :label="curLocale.tabItems[1].listOrders.capacityMachine"
                                readonly
                                :value="item.machine.capacity"
                            ></v-text-field>
                          </v-col>
                          <v-col>
                            <v-text-field
                                :label="curLocale.tabItems[1].listOrders.priceMachine"
                                readonly
                                :value="item.machine.price+curLocale.tabItems[1].listOrders.additiveInfo"
                            ></v-text-field>
                            <v-text-field
                                :label="curLocale.tabItems[1].listOrders.stockMachine"
                                readonly
                                :value="item.machine.stock"
                            ></v-text-field>
                          </v-col>
                        </v-row>
                        <v-select :items="item.additional" rounded :label="curLocale.tabItems[1].listOrders.additionalMachine"></v-select>
                      </v-card>
                    </v-badge>
                  </v-col>
                </v-row>
              </v-container>
            </v-list-group>
          </v-list>
          <div v-else style="margin: 10%">
            <v-icon style="text-align: center;display: block">warning</v-icon>
            <v-card-title style="justify-content: center">{{curLocale.tabItems[1].listIsEmptyTitle}}</v-card-title>
          </div>
        </v-tab-item>
        <v-tab-item>
          <v-card-title>{{ curLocale.tabItems[2].title }}</v-card-title>
          <v-divider></v-divider>
          <v-container>
            <v-row style="margin: 0 2% 0 2%">
              <v-col>
                <v-img :src="info.userInfo.avatar" width="420" height="420"></v-img>
                <v-file-input
                    accept="image/png, image/jpeg, image/jpg"
                    :label="curLocale.tabItems[2].editForm.avatar"
                    prepend-icon="mdi-camera"
                ></v-file-input>
              </v-col>
              <v-col>
                <v-row>
                  <v-col>
                    <v-text-field
                        :label="curLocale.tabItems[2].editForm.fName"
                        :placeholder="info.userInfo.fName"
                        outlined
                    ></v-text-field>
                    <v-text-field
                        :label="curLocale.tabItems[2].editForm.sName"
                        :placeholder="info.userInfo.sName"
                        outlined
                    ></v-text-field>
                    <v-text-field
                        :label="curLocale.tabItems[2].editForm.phone"
                        :rules="phoneRules"
                        :placeholder="info.userInfo.phone"
                        outlined
                    ></v-text-field>
                  </v-col>
                  <v-col>
                    <v-text-field
                        :label="curLocale.tabItems[2].editForm.login"
                        :placeholder="info.userInfo.login"
                        outlined
                    ></v-text-field>
                    <v-text-field
                        :label="curLocale.tabItems[2].editForm.pwd"
                        type="password"
                        :placeholder="info.userInfo.pwd"
                        outlined
                    ></v-text-field>
                    <v-text-field
                        :label="curLocale.tabItems[2].editForm.email"
                        type="email"
                        :rules="emailRules"
                        :placeholder="info.userInfo.email"
                        outlined
                    ></v-text-field>
                  </v-col>
                </v-row>
              </v-col>
            </v-row>
          </v-container>
          <v-btn @click="editSettings" outlined color="indigo" width="100%">{{curLocale.tabItems[2].editForm.btnTitle}}</v-btn>
        </v-tab-item>
      </v-tabs>
    </v-card>
  </v-app>
</template>

<script>
const ip = "192.168.0.113"
const port = "9000"
const axios = require('axios')
export default {
  name: "Cabinet",
  data() {
    return {
      info: {
        userInfo: null,
        drafts: null,
      },
      phoneRules: [
        v => v.length === 10 || this.curLocale.tabItems[2].editForm.rulePhoneText
      ],
      emailRules: [
        v => v.match("[a-zA-Z]+@[a-zA-Z]+[.][a-zA-Z]+") || this.curLocale.tabItems[2].editForm.ruleEmailText
      ],
      curLocale: null,
      locales: {
        'en-EN': {
          tabsNames: [
            'Dashboard', 'My Orders', 'Settings'
          ],
          tabItems: [
            {
              title: 'Personal Area of',
              userInfo: [
                'First Name:', 'Second Name:', 'Login:', 'E-mail:', 'Phone:', 'Balance:'
              ],
              curOrders: {
                title: 'Current washings:',
                tipForMachine: 'Status of washing machine',
                machine: {
                  titleReady: 'Ready of',
                  listInfo: [
                    'Price:', 'UAH.', 'Description:', 'Capacity:', 'L.'
                  ]
                },
                ordersIsEmptyTitle: 'Active orders not found'
              }
            },
            {
              title: 'My Orders',
              listOrders: {
                paymentType: 'Payment Type',
                askForCondition: [
                  'Credit Card', 'Cash'
                ],
                numsCreditCard: 'Number of credit card',
                tipsForMachine: 'Status of washing machine in the present',
                nameMachine: 'Washing machine',
                capacityMachine: 'Capacity',
                priceMachine: 'Price',
                additiveInfo: ' UAH/L.',
                stockMachine: 'Stock',
                additionalMachine: 'Used additives'
              },
              listIsEmptyTitle: 'Orders not found'
            },
            {
              title: 'Settings of account',
              editForm: {
                avatar: 'Your Picture',
                fName: 'First name',
                sName: 'Second name',
                phone: 'Phone',
                login: 'Login',
                pwd: 'Password',
                email: 'E-mail',
                btnTitle: 'EDIT',
                rulePhoneText: 'Input correct phone number',
                ruleEmailText: 'Input correct e-mail'
              }
            }
          ]
        },
        'ru-RU': {
          tabsNames: [
              'Информация', 'Мои заказы', 'Настройки'
          ],
          tabItems: [
            {
              title: 'Личный кабинет',
              userInfo: [
                  'Имя:', 'Фамилия:', 'Логин:', 'E-mail:', 'Телефон:', 'Баланс:'
              ],
              curOrders: {
                title: 'Текущие стирки:',
                tipForMachine: 'Статус стиральной машини',
                machine: {
                  titleReady: 'Готово на',
                  listInfo: [
                      'Цена:', 'ГРН.', 'Описание:', 'Вместимость:', 'л.'
                  ]
                },
                ordersIsEmptyTitle: 'Активных заказов нет'
              }
            },
            {
              title: 'Мои заказы',
              listOrders: {
                paymentType: 'Способ оплаты',
                askForCondition: [
                    'Кредитная карта', 'Наличные'
                ],
                numsCreditCard: 'Номер кредитной карты',
                tipsForMachine: 'Статус стиральной машини на данный момент',
                nameMachine: 'Стиральная машина',
                capacityMachine: 'Вместимость',
                priceMachine: 'Цена',
                additiveInfo: ' ГРН/л.',
                stockMachine: 'Скидка',
                additionalMachine: 'Используемые добавки'
              },
              listIsEmptyTitle: 'Заказов не найдено'
            },
            {
              title: 'Настройки аккаунта',
              editForm: {
                avatar: 'Ваша аватарка',
                fName: 'Имя',
                sName: 'Фамилия',
                phone: 'Телефон',
                login: 'Логин',
                pwd: 'Пароль',
                email: 'E-mail',
                btnTitle: 'ИЗМЕНИТЬ',
                rulePhoneText: 'Введите коретный номер телефона',
                ruleEmailText: 'Введите верный e-mail'
              }
            }
          ]
        },
        'ua-UA': {
          tabsNames: [
            'Інформація', 'Мої замовлення', 'Налаштування'
          ],
          tabItems: [
            {
              title: 'Особистий кабінет',
              userInfo: [
                "Ім`я:", 'Прізвище:', 'Логін:', 'E-mail:', 'Телефон:', 'Баланс:'
              ],
              curOrders: {
                title: 'Поточні прання:',
                tipForMachine: 'Статус пральної машини',
                machine: {
                  titleReady: 'Готово на',
                  listInfo: [
                    'Ціна:', 'ГРН.', 'Опис:', 'Місткість:', 'л.'
                  ]
                },
                ordersIsEmptyTitle: 'Активных замовлень немає'
              }
            },
            {
              title: 'Мої замовлення',
              listOrders: {
                paymentType: 'Спосіб сплати',
                askForCondition: [
                  'Кредитна карта', 'Готівка'
                ],
                numsCreditCard: 'Номер кредитної карти',
                tipsForMachine: 'Статус пральної машини на даний момент',
                nameMachine: 'Пральна машина',
                capacityMachine: 'Місткість',
                priceMachine: 'Ціна',
                additiveInfo: ' ГРН/л.',
                stockMachine: 'Знижка',
                additionalMachine: 'Використовані добавки'
              },
              listIsEmptyTitle: 'Замовлень не знайдено'
            },
            {
              title: 'Налаштування аккаунта',
              editForm: {
                avatar: 'Ваша аватарка',
                fName: 'Им`я',
                sName: 'Прізвище',
                phone: 'Телефон',
                login: 'Логін',
                pwd: 'Пароль',
                email: 'E-mail',
                btnTitle: 'ЗМІНИТИ',
                rulePhoneText: 'Введіть коректний номер телефону',
                ruleEmailText: 'Введіть коректний e-mail'
              }
            }
          ]
        }
      },
    }
  },
  methods: {
    editSettings() {

    }
  },
  beforeMount() {
    if (localStorage['lang'] === 'ru-RU') {
      this.curLocale = this.locales["ru-RU"];
      console.log(this.curLocale);
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
    axios.get(`http://${ip}:${port}/api/persons/1`)
      .then(resp => {
        this.info.userInfo = resp.data
        axios.get(`http://${ip}:${port}/api/drafts`)
            .then(resp1 => {
              this.info.drafts = resp1.data.filter(i => i.person.id === resp.data.id);
            })
      })
  }
}
</script>

<style scoped>

</style>