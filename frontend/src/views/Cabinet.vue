<template>
  <v-app>
    <v-card v-if="authSuccess" style="margin: 8% 15% 0 15%">
      <v-tabs grow color="indigo">
        <v-tab>{{curLocale.tabsNames[0]}}</v-tab>
        <v-tab>{{curLocale.tabsNames[1]}}</v-tab>
        <v-tab>{{curLocale.tabsNames[2]}}</v-tab>
        <v-tab-item>
          <div v-if="profileViewInfo === 'profile'">
            <v-card-title style="justify-content: center; display: flex">{{curLocale.tabItems[0].title}}</v-card-title>
            <v-divider></v-divider>
            <v-container>
              <v-row>
                <v-col>
                  <v-text-field
                      :label="curLocale.tabItems[0].userInfo[0]"
                      :value="info.userInfo.fName"
                      filled
                      rounded
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                      :label="curLocale.tabItems[0].userInfo[1]"
                      :value="info.userInfo.sName"
                      filled
                      rounded
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col>
                  <v-text-field
                      :label="curLocale.tabItems[0].userInfo[2]"
                      :value="info.userInfo.login"
                      filled
                      rounded
                  ></v-text-field>
                </v-col>
                <v-col cols="6">
                  <v-text-field
                      :label="curLocale.tabItems[0].userInfo[3]"
                      :value="info.userInfo.email"
                      filled
                      rounded
                  ></v-text-field>
                </v-col>
                <v-col>
                  <v-text-field
                      :label="curLocale.tabItems[0].userInfo[4]"
                      :value="info.userInfo.phone"
                      filled
                      rounded
                  ></v-text-field>
                </v-col>
              </v-row>
            </v-container>
            <v-divider></v-divider>
            <v-card flat>
              <v-card-title>
                {{curLocale.tabItems[0].curOrders.title}}
                <v-spacer></v-spacer>
                <v-card-subtitle v-if="info.userInfo.machine.length <= 0" style="justify-content: center">
                  <v-icon small style="text-align: center;display: block">warning</v-icon>
                  {{curLocale.tabItems[0].curOrders.ordersIsEmptyTitle}}
                </v-card-subtitle>
              </v-card-title>
              <v-divider></v-divider>
              <v-list v-if="info.userInfo.machine.length > 0">
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
            </v-card>
          </div>
          <div v-if="profileViewInfo === 'balance'">
            <v-card-title style="text-align: center; display: block">
              {{curLocale.tabItems[0].balance.title}}
            </v-card-title>
            <v-divider></v-divider>
            <v-container>
              <v-row>
                <v-col>
                  <v-card-subtitle>
                    {{curLocale.tabItems[0].balance.typeTransTitle}}
                  </v-card-subtitle>
                  <v-divider></v-divider>
                  <v-row>
                    <v-col>
                      <v-row>
                        <v-col cols="12">
                          <v-dialog max-width="420" persistent v-model="payCreditCard">
                            <template v-slot:activator="{on, attrs}">
                              <v-btn outlined color="indigo" block v-on="on" v-bind="attrs">
                                <v-icon>
                                  credit_card
                                </v-icon>
                                {{curLocale.tabItems[0].balance.types[0]}}
                              </v-btn>
                            </template>
                            <v-card>
                              <v-card-title>
                                {{curLocale.tabItems[0].balance.titleCreditCard}}
                                <v-spacer></v-spacer>
                                <v-btn icon @click="payCreditCard = false">
                                  <v-icon>
                                    close
                                  </v-icon>
                                </v-btn>
                              </v-card-title>
                              <v-divider></v-divider>
                              <v-alert type="success" v-model="successAlert">
                                {{curLocale.tabItems[0].balance.alerts[0]}}
                              </v-alert>
                              <v-alert type="error" v-model="errAlert">
                                {{curLocale.tabItems[0].balance.alerts[1]}}
                              </v-alert>
                              <form>
                                <v-container>
                                  <v-row>
                                    <v-col>
                                      <v-text-field
                                          :label="curLocale.tabItems[0].balance.labels[0]"
                                          outlined
                                          type="number"
                                          :rules="balanceForm.numsCreditCard"
                                      ></v-text-field>
                                      <v-row>
                                        <v-col>
                                          <v-text-field
                                              :label="curLocale.tabItems[0].balance.labels[1]"
                                              type="number"
                                              outlined
                                              :rules="balanceForm.cvvRules"
                                          ></v-text-field>
                                        </v-col>
                                        <v-col>
                                          <v-menu
                                              v-model="menuChooseDate"
                                              :close-on-content-click="false"
                                              offset-y
                                              transition='scale-transition'
                                              max-width="100%"
                                          >
                                            <template v-slot:activator="{on, attrs}">
                                              <v-text-field
                                                  v-model="balanceForm.date"
                                                  :label="curLocale.tabItems[0].balance.labels[2]"
                                                  persistent-hint
                                                  prepend-icon="mdi-calendar"
                                                  readonly
                                                  v-bind="attrs"
                                                  v-on="on"
                                              ></v-text-field>
                                            </template>
                                            <v-date-picker
                                                v-model="balanceForm.chosenDate"
                                                no-title
                                                @input="reFormateDate"
                                            ></v-date-picker>
                                          </v-menu>
                                        </v-col>
                                      </v-row>
                                    </v-col>
                                  </v-row>
                                  <v-btn block color="indigo" outlined @click="doPayCreditCard">
                                    {{curLocale.tabItems[0].balance.btnTitle}}
                                  </v-btn>
                                </v-container>
                              </form>
                            </v-card>
                          </v-dialog>
                        </v-col>
                        <v-col cols="12">
                          <v-snackbar
                              transition="fab-transition"
                              timeout="4000"
                              outlined
                              top
                              color="success"
                              v-model="doCashPay"
                          >
                            {{curLocale.tabItems[0].balance.alerts[2]}}
                          </v-snackbar>
                          <v-btn outlined block color="indigo" @click="doCashPay = true">
                            <v-icon>
                              attach_money
                            </v-icon>
                            {{curLocale.tabItems[0].balance.types[1]}}
                          </v-btn>
                        </v-col>
                      </v-row>
                    </v-col>
                    <v-col>
                      <v-card-subtitle>
                        {{curLocale.tabItems[0].histories.title}}
                      </v-card-subtitle>
                      <v-divider></v-divider>
                      <v-list>
                        <v-list-group>
                          <template v-slot:activator>
                            <v-list-item-title>
                              {{curLocale.tabItems[0].histories.subtitle}} {{info.userInfo.wallet.balance}} {{curLocale.tabItems[0].histories.subtitle[1]}}
                            </v-list-item-title>
                          </template>
                          <v-list-group sub-group no-action color="indigo">
                            <template v-slot:activator>
                              <v-list-item-content>
                                <v-list-item-title>
                                  {{curLocale.tabItems[0].histories.lstItems[0]}}
                                </v-list-item-title>
                              </v-list-item-content>
                            </template>
                            <v-list-item>
                              <v-list-group sub-group no-action color="indigo" v-if="info.userInfo.wallet.historyTransactions.length > 0">
                                <template v-slot:activator>
                                  <v-list-item-title>
                                    {{curLocale.tabItems[0].histories.lstItems[1]}}
                                  </v-list-item-title>
                                </template>
                                <div>
                                  <v-list-item v-for="(item, i) in info.userInfo.wallet.historyTransactions" :key="i">
                                    <v-list-item-title>
                                      {{curLocale.tabItems[0].histories.lstItems[2]}} {{item.id}} <br>
                                      {{curLocale.tabItems[0].histories.lstItems[3]}} {{item.sum}} <br>
                                      {{curLocale.tabItems[0].histories.lstItems[4]}} {{item.paymentType}} <br>
                                      <span v-if="item.paymentType === curLocale.tabItems[0].histories.lstItems[5]">
                                        {{curLocale.tabItems[0].histories.lstItems[5]}} {{item.creditCard}}
                                      </span>
                                      {{curLocale.tabItems[0].histories.lstItems[6]}} {{item.date}} <br>
                                    </v-list-item-title>
                                  </v-list-item>
                                </div>
                              </v-list-group>
                              <v-list-item v-else>
                                <v-list-item-title>
                                  {{curLocale.tabItems[0].histories.notFound}}
                                </v-list-item-title>
                              </v-list-item>
                            </v-list-item>
                          </v-list-group>
                        </v-list-group>
                      </v-list>
                    </v-col>
                  </v-row>
                </v-col>
              </v-row>
            </v-container>
          </div>
          <v-bottom-navigation color="indigo" shift>
            <v-btn @click="profileViewInfo = 'profile'">
              {{curLocale.tabItems[0].addsBtns[0]}}
              <v-icon>
                person
              </v-icon>
            </v-btn>
            <v-btn @click="profileViewInfo = 'balance'">
              {{curLocale.tabItems[0].addsBtns[1]}}
              <v-icon>
                account_balance
              </v-icon>
            </v-btn>
          </v-bottom-navigation>
        </v-tab-item>
        <v-tab-item>
          <v-list v-if="info.drafts.length > 0">
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
          <form>
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
                  <v-row>
                    <v-col>
                      <v-btn @click="editSettings" outlined color="indigo" block>{{curLocale.tabItems[2].editForm.btnTitle}}</v-btn>
                    </v-col>
                  </v-row>
                </v-col>
              </v-row>
            </v-container>
          </form>
        </v-tab-item>
      </v-tabs>
    </v-card>
    <v-card v-else style="margin: 8% 15% 0 15%; padding: 5%">
      <v-icon style="justify-content: center; display: flex">warning</v-icon>
      <v-card-title style="text-align: center; display: block">
        {{ curLocale.notFound }}
      </v-card-title>
    </v-card>
  </v-app>
</template>

<script>
const ip = "192.168.0.153"
const port = "9000"
const axios = require('axios')
export default {
  name: "Cabinet",
  data() {
    return {
      info: {
        userInfo: {
          fName: '',
          sName: '',
          login: '',
          email: '',
          phone: '',
          machine: [],
          wallet: {
            balance: '',
            historyTransactions: []
          }
        },
        drafts: [],
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
              title: 'Personal Area',
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
              },
              addsBtns: [
                  'I am',
                  'Balance'
              ],
              balance: {
                title: 'My transaction',
                typeTransTitle: 'Type of transactions:',
                titleCreditCard: 'Payment for credit card',
                types: [
                    'Credit card',
                    'Cash'
                ],
                alerts: [
                    'Redirect to site with pay...',
                    'Error in input data',
                    'Ask the service, giving your full name to the employee'
                ],
                labels: [
                    'Number of credit card:',
                    'CVV2 code:',
                    'Expiration date:'
                ],
                btnTitle: 'DONATE'
              },
              histories: {
                title: 'History of transactions',
                subtitle: [
                    'Summa', 'UAH'
                ],
                lstItems: [
                    'Information of wallet',
                    'History of replenishment',
                    'ID:',
                    'Summa replenishment:',
                    'Type of pay:',
                    'CreditCard',
                    'Date of replenishment:'
                ],
                notFound: 'Replenishment not found'
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
          ],
          notFound: 'Denied to access'
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
              },
              addsBtns: [
                'Я',
                'Счет'
              ],
              balance: {
                title: 'Мои пополнения',
                typeTransTitle: 'Виды пополнений:',
                titleCreditCard: 'Пополнение кредитной картой',
                types: [
                  'Кредитная карта',
                  'Наличные'
                ],
                alerts: [
                  'Переадресация на сайт с оплатой...',
                  'Ошибка в введеных данных',
                  'Попросить персонал помочь вам, также сказав им своё ФИО.'
                ],
                labels: [
                  'Номер кредитной карты:',
                  'CVV2 код:',
                  'Дата истечения карты:'
                ],
                btnTitle: 'ПОПОЛНИТЬ',
                numCreditRules: 'Введите коректный номер карты!',
                cvvRules: 'Введите верный CVV2/CVC код!'
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
          ],
          notFound: 'Отказано в доступе'
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
              },
              addsBtns: [
                'Я',
                'Рахунок'
              ],
              balance: {
                title: 'Мої поповнення',
                typeTransTitle: 'Види поповнень:',
                titleCreditCard: 'Поповнення для кредитної карти',
                types: [
                  'Кредитна карта',
                  'Готівка'
                ],
                alerts: [
                  'Переадресація до сайту платнику...',
                  'Помилки у написаних даних',
                  'Зверніться до служби, вказавши своє повне ім’я працівнику'
                ],
                labels: [
                  'Номера кредитної карти:',
                  'CVV2 код:',
                  'Дата закінчення карти:'
                ],
                btnTitle: 'DONATE',
                numCreditRules: 'Введіть коректний номер карти!',
                cvvRules: 'Введіть вірний CVV2 / CVC код!'
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
          ],
          notFound: 'Відмовленно у доступі'
        }
      },
      authSuccess: false,
      profileViewInfo: 'profile',
      menuChooseDate: false,
      payCreditCard: false,
      doCashPay: false,
      balanceForm: {
        chosenDate: '',
        date: '00/00',
        numsCreditCard: [
            v => v.length === 16 || this.curLocale.tabItems[0].balance.numCreditRules
        ],
        cvvRules: [
            v => v.length === 3 || this.curLocale.tabItems[0].balance.cvvRules
        ]
      },
      successAlert: false,
      errAlert: false
    }
  },
  methods: {
    doPayCreditCard() {

    },
    reFormateDate() {
      const dates = this.balanceForm.chosenDate.split('-')
      this.balanceForm.date = `${dates[2]}/${dates[1]}`
    },
    editSettings() {

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
    axios.get(`http://${ip}:${port}/api/persons/`)
      .then(resp => {
        console.info('session', localStorage.getItem('uid'))
        if (localStorage.getItem('uid') !== null ) {
          let info = resp.data.filter(i => i.sessionId === localStorage.getItem('uid'))
          console.info('data', info)
          if (info.length > 0) {
            this.info.userInfo = info[0]
            axios.get(`http://${ip}:${port}/api/drafts`)
                .then(resp1 => {
                  this.info.drafts = resp1.data.filter(i => i.person.id === this.info.userInfo.id);
                })
            this.authSuccess = true
          } else {
            this.authSuccess = false
          }
        } else {
          this.authSuccess = false
        }
      })
  }
}
</script>

<style scoped>

</style>