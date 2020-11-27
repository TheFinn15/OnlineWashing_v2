<template>
  <v-app>
    <v-app-bar app color="indigo">
      <v-toolbar-title @click="homePage" style="color: white">{{curLocale.toolbarTitle}}</v-toolbar-title>
      <v-menu v-model="translate" offset-x rounded="b-xl" :close-on-content-click="false" nudge-width="140">
        <template v-slot:activator="{on, attrs}">
          <v-btn v-on="on" v-bind="attrs" color="white" icon :title="curLocale.locales.tip">
            <v-icon>translate</v-icon>
          </v-btn>
        </template>
        <v-card>
          <v-card-subtitle>{{curLocale.locales.langSubTitle}}</v-card-subtitle>
          <v-divider></v-divider>
          <v-list shaped>
            <v-list-item-group color="indigo">
              <v-list-item @click="changeLangEN">
                <v-list-item-content>
                  <v-list-item-title>
                    {{curLocale.locales.selects[0]}}
                  </v-list-item-title>
                </v-list-item-content>
                <v-list-item-icon>
                  <v-icon>chevron_right</v-icon>
                </v-list-item-icon>
              </v-list-item>
              <v-list-item @click.prevent="changeLangRU">
                <v-list-item-content>
                  <v-list-item-title>
                    {{curLocale.locales.selects[1]}}
                  </v-list-item-title>
                </v-list-item-content>
                <v-list-item-icon>
                  <v-icon>chevron_right</v-icon>
                </v-list-item-icon>
              </v-list-item>
              <v-list-item @click="changeLangUA">
                <v-list-item-content v-model="langThird">
                  <v-list-item-title>
                    {{curLocale.locales.selects[2]}}
                  </v-list-item-title>
                </v-list-item-content>
                <v-list-item-icon>
                  <v-icon>chevron_right</v-icon>
                </v-list-item-icon>
              </v-list-item>
            </v-list-item-group>
          </v-list>
        </v-card>
      </v-menu>
      <v-spacer></v-spacer>
      <v-spacer></v-spacer>
      <v-menu offset-y v-if="isAuth">
        <template v-slot:activator="{on, attrs}">
          <v-avatar v-on="on" v-bind="attrs">
            <v-img :src="info.userInfo.avatar"></v-img>
          </v-avatar>
        </template>
        <v-card>
          <v-card-subtitle>{{info.userInfo.fName}} {{info.userInfo.sName}}</v-card-subtitle>
          <v-divider></v-divider>
          <v-list flat>
            <v-list-item-group>
              <v-list-item>
                <v-list-item-icon><v-icon>attach_money</v-icon></v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>{{info.userInfo.wallet.balance}} {{curLocale.authedUser.menuItems[0]}}</v-list-item-title>
                </v-list-item-content>
              </v-list-item>
              <v-list-item @click="$router.push('/cabinet')">
                <v-list-item-icon><v-icon>person</v-icon></v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>{{curLocale.authedUser.menuItems[1]}}</v-list-item-title>
                </v-list-item-content>
              </v-list-item>
              <v-list-item @click="goToMain">
                <v-list-item-icon><v-icon>apps</v-icon></v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>{{curLocale.authedUser.menuItems[2]}}</v-list-item-title>
                </v-list-item-content>
              </v-list-item>
              <v-divider></v-divider>
              <v-list-item @click="doLogout">
                <v-list-item-icon><v-icon>exit_to_app</v-icon></v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>{{curLocale.authedUser.menuItems[3]}}</v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list-item-group>
          </v-list>
        </v-card>
      </v-menu>
      <v-menu offset-y v-else-if="!isAuth">
        <template v-slot:activator="{on, attrs}">
          <v-avatar v-on="on" v-bind="attrs" color="primary" title="Anonymous">A</v-avatar>
        </template>
        <v-card>
          <v-list>
            <v-list-item-group>
              <v-list-item @click="$router.push('/')">
                <v-list-item-icon><v-icon>apps</v-icon></v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>{{curLocale.nonAuthedUser.menuItems[1]}}</v-list-item-title>
                </v-list-item-content>
              </v-list-item>
              <v-list-item @click="register = true">
                <v-list-item-icon><v-icon>person_add</v-icon></v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>{{curLocale.nonAuthedUser.menuItems[2]}}</v-list-item-title>
                </v-list-item-content>
              </v-list-item>
              <v-list-item @click="authForm = true">
                <v-list-item-icon><v-icon>login</v-icon></v-list-item-icon>
                <v-list-item-content>
                  <v-list-item-title>{{curLocale.nonAuthedUser.menuItems[3]}}</v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list-item-group>
          </v-list>
        </v-card>
      </v-menu>
    </v-app-bar>
    <v-dialog v-model="authForm" max-width="600px" persistent>
      <v-card>
        <v-card-title>
          {{curLocale.authForm.title}}
          <v-spacer></v-spacer>
          <v-btn icon @click="authForm = false">
            <v-icon>
              close
            </v-icon>
          </v-btn>
        </v-card-title>
        <v-divider></v-divider>
        <v-alert
            v-model="errForm"
            color="red"
            dense
            outlined
            type="error"
        >
          {{errText}}
        </v-alert>
        <v-alert
            v-model="successForm"
            color="success"
            dense
            outlined
            type="success"
        >
          Авторизация успешна
        </v-alert>
        <v-form>
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field
                    :label="curLocale.authForm.labels[0]"
                    v-model="login"
                    :rules="textRules"
                    prepend-icon="person"
                    required
                ></v-text-field>
              </v-col>
              <v-col>
                <v-text-field
                    type="password"
                    :label="curLocale.authForm.labels[1]"
                    v-model="pwd"
                    :rules="textRules"
                    prepend-icon="lock"
                    required
                ></v-text-field>
              </v-col>
            </v-row>
            <v-btn @click="doAuth" color="success" block>{{curLocale.authForm.btnTitle}}</v-btn>
          </v-container>
        </v-form>
      </v-card>
    </v-dialog>
    <v-dialog v-model="register" max-width="600px" persistent>
      <v-card>
        <v-card-title>
          Регистрация
          <v-spacer></v-spacer>
          <v-btn icon @click="register = false">
            <v-icon>
              close
            </v-icon>
          </v-btn>
        </v-card-title>
        <v-divider></v-divider>
        <v-alert
            v-model="errForm"
            color="red"
            dense
            outlined
            type="error"
        >
          {{errText}}
        </v-alert>
        <v-alert
            v-model="successForm"
            color="success"
            dense
            outlined
            type="success"
        >
          {{curLocale.authForm.successText}}
        </v-alert>
        <v-container>
          <v-row>
            <v-col>
              <v-text-field
                  outlined
                  :label="curLocale.registerForm.labels[0]"
                  v-model="form.fName"
                  :rules="textRules"
                  required
              ></v-text-field>
              <v-text-field
                  outlined
                  :label="curLocale.registerForm.labels[1]"
                  v-model="form.sName"
                  :rules="textRules"
                  required
              ></v-text-field>
            </v-col>
            <v-col>
              <v-text-field
                  outlined
                  :label="curLocale.registerForm.labels[2]"
                  v-model="form.login"
                  prepend-inner-icon="person"
                  :rules="textRules"
                  required
              ></v-text-field>
              <v-text-field
                  type="password"
                  outlined
                  :label="curLocale.registerForm.labels[3]"
                  v-model="form.pwd"
                  prepend-inner-icon="lock"
                  :rules="textRules"
                  required
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <v-text-field
                  outlined
                  :label="curLocale.registerForm.labels[4]"
                  v-model="form.phone"
                  prepend-inner-icon="phone"
                  :rules="phoneRules"
                  required
              ></v-text-field>
              <v-text-field
                  outlined
                  :label="curLocale.registerForm.labels[5]"
                  v-model="form.email"
                  prepend-inner-icon="email"
                  :rules="emailRules"
                  required
              ></v-text-field>
            </v-col>
            <v-col :title="curLocale.registerForm.labels[6]">
              <v-row no-gutters align="end">
                <v-col>
                  <v-img style="border: 1px solid lightgray" width="150px" height="142px" :src="form.avatar"></v-img>
                </v-col>
                <v-col>
                  <v-file-input
                      hide-input
                      @change="doLoadAvatar"
                  ></v-file-input>
                </v-col>
              </v-row>
            </v-col>
          </v-row>
          <v-btn @click="doRegister" color="indigo" outlined block class="pa-2">
            {{curLocale.registerForm.btnTitle}}
          </v-btn>
        </v-container>
      </v-card>
    </v-dialog>
    <router-view></router-view>
  </v-app>
</template>

<script>
  const ip =  "localhost"
  const port = "25016"
  const axios = require('axios')
  export default {
    name: 'App',
    data() {
      return {
        info: {
          userInfo: {
            fName: '',
            sName: '',
            login: '',
            email: '',
            pwd: '',
            phone: '',
            avatar: '',
            wallet: {
              balance: ''
            }
          }
        },
        form: {
          fName: '',
          sName: '',
          login: '',
          pwd: '',
          phone: '',
          email: '',
          avatar: ''
        },
        imgSelected: false,
        drawer: false,
        isAuth: false,
        authForm: false,
        curLocale: null,
        locales: {
          'en-EN': {
            toolbarTitle: 'Online Washing',
            locales: {
              langSubTitle: 'Language of Interface',
              tip: 'Choice of language',
              selects: [
                'English', 'Russian', 'Ukrainian'
              ]
            },
            authedUser: {
              menuItems: [
                'UAH', 'Personal Area', 'Catalog', 'Logout'
              ]
            },
            nonAuthedUser: {
              menuItems: [
                'UAH', 'Catalog', 'Sign-Up', 'Sign-In'
              ]
            },
            authForm: {
              title: 'Sign-In',
              errText: 'Entered data is invalid',
              successText: 'Successfully authorization',
              labels: [
                'Login*', 'Password*'
              ],
              rulesText: [
                'This field can not be a empty'
              ],
              phoneRules: 'Input correct phone number',
              emailRules: 'Input correct e-mail',
              btnTitle: 'ENTER'
            },
            registerForm: {
              title: 'Registration',
              errText: 'Try later...',
              successText: 'Successfully registration',
              labels: [
                  'First Name:',
                  'Second Name:',
                  'Login:',
                  'Password:',
                  'Phone:',
                  "E-mail:",
                  'Avatar:'
              ],
              btnTitle: 'CREATE ACCOUNT'
            }
          },
          'ru-RU': {
            toolbarTitle: 'Онлайн-прачечная',
            tip: 'Выбор языка',
            locales: {
              langSubTitle: 'Язык интерфейса',
              selects: [
                'English', 'Русский', 'Украинский'
              ]
            },
            authedUser: {
              menuItems: [
                'ГРН', 'Личный кабинет', 'Каталог', 'Выйти'
              ]
            },
            nonAuthedUser: {
              menuItems: [
                'ГРН', 'Каталог', 'Регистрация', 'Авторизация'
              ]
            },
            authForm: {
              title: 'Авторизация',
              errText: 'Попробуйте позже...',
              successText: 'Успешная авторизация',
              labels: [
                'Логин*', 'Пароль*'
              ],
              rulesText: [
                'Это поле не может быть пустым'
              ],
              phoneRules: 'Введите верный телефон',
              emailRules: 'Введите верный e-mail',
              btnTitle: 'ВОЙТИ'
            },
            registerForm: {
              title: 'Регистрация',
              errText: 'Попробуйте позже...',
              successText: 'Успешная регистрация',
              labels: [
                'Имя:',
                'Фамилия:',
                'Логин:',
                'Пароль:',
                'Телефон:',
                "E-mail:",
                'Ваш аватар:'
              ],
              btnTitle: 'СОЗДАТЬ АККАУНТ'
            }
          },
          'ua-UA': {
            toolbarTitle: 'Онлайн-пральня',
            locales: {
              langSubTitle: 'Мова інтерфейсу',
              tip: 'Вибір мови',
              selects: [
                'English', 'Російська', 'Українська'
              ]
            },
            authedUser: {
              menuItems: [
                'ГРН', 'Особистий кабінет', 'Каталог', 'Вийти'
              ]
            },
            nonAuthedUser: {
              menuItems: [
                'ГРН', 'Каталог', 'Реєстрація', 'Авторизація'
              ]
            },
            authForm: {
              title: 'Авторизація',
              errText: 'Спробуйте пізніше...',
              successText: 'Авторизація успішна',
              labels: [
                'Логін*', 'Пароль*'
              ],
              rulesText: [
                'Поле не може бути порожнім'
              ],
              phoneRules: 'Напишіть коректний телефон',
              emailRules: 'Напишіть коректний e-mail',
              btnTitle: 'ВІЙТИ'
            },
            registerForm: {
              title: 'Реєстрація',
              errText: 'Спробуйте пізніше...',
              successText: 'Реэстрація успішна',
              labels: [
                'Ім`я:',
                'Прізвище:',
                'Логін:',
                'Пароль:',
                'Телефон:',
                "E-mail:",
                'Ваш аватар:'
              ],
              btnTitle: 'СТВОРИТИ АККАУНТ'
            }
          }
        },
        register: false,
        successForm: false,
        errForm: false,
        errText: null,
        translate: false,
        langOne: false,
        langSecond: false,
        langThird: false,
        login: '',
        pwd: '',
        textRules: [
          v => v.length !== 0 || this.curLocale.authForm.rulesText[0]
        ],
        phoneRules: [
            v => v.match('[0-9]{10}') !== null || this.curLocale.authForm.phoneRules
        ],
        emailRules: [
            v => v.match('[a-zA-Z]+@[a-zA-Z]+.[a-zA-Z]+') !== null || this.curLocale.authForm.emailRules
        ]
      }
    },
    mounted() {
      if (localStorage.getItem('uid') !== null) {
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/persons`,
          headers: {
            Authorization: 'Bearer ' + localStorage['uid']
          }
        }).then(resp => {
          this.info.userInfo = resp.data
          if (this.info.userInfo.wallet.balance === null || this.info.userInfo.wallet.balance === 0 || this.info.userInfo.wallet.balance === undefined) {
            this.info.userInfo.wallet.balance =  "00, 00"
          } else {
            this.info.userInfo.wallet.balance = this.info.userInfo.wallet.balance.toString() + ", 00";
          }
          this.isAuth = true;
        }).catch(() => {
          this.isAuth = false;
        })
      }
      if (localStorage.getItem('lang') === null) {
        localStorage.setItem('lang', 'ua-UA')
        this.langThird = true;
        this.langOne = false;
        this.langSecond = false;
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
    methods: {
      goToMain() {
        if (this.$route.fullPath !== '/') {
          this.$router.push('/')
        }
      },
      homePage() {
        if (this.$route.path !== '/') this.$router.push('/')
      },
      doLoadAvatar(ev) {
        let reader = new FileReader();
        reader.onload = (e) => {
          this.form.avatar = e.target.result
        }
        reader.readAsDataURL(ev);
        this.imgSelected = true;
      },
      doLogout() {
        localStorage.removeItem('uid')
        this.isAuth = false;
        // window.history.go();
      },
      changeLangEN() {
        localStorage.setItem('lang', 'en-EN')
        this.curLocale = this.locales["en-EN"];
        this.$router.go(this.$router.currentRoute.path);
      },
      changeLangRU() {
        localStorage.setItem('lang', 'ru-RU')
        this.curLocale = this.locales["ru-RU"];
        this.$router.go(this.$router.currentRoute.path);
      },
      changeLangUA() {
        localStorage.setItem('lang', 'ua-UA')
        this.curLocale = this.locales["ua-UA"];
        this.$router.go(this.$router.currentRoute.path);
      },
      doAuth() {
        axios.post(`http://${ip}:${port}/api/auth`, {
          login: this.login,
          pwd: this.pwd
        })
            .then(resp => {
              localStorage.setItem('uid', resp.data['id_token'])
              this.successForm = true;
              let req = new XMLHttpRequest();
              req.open('GET', `http://${ip}:${port}/api/user`)
              req.setRequestHeader('Authorization', localStorage.getItem('uid'))
              req.responseType = 'json'
              req.send()
              req.onload = () => {
                setTimeout(() => {
                  this.login = ''
                  this.pwd = ''
                  this.authForm = false
                  this.info.userInfo = req.response
                  window.location.reload()
                }, 1200)
              }
            })
            .catch(() => {
              this.errForm = true
              this.errText = this.curLocale.authForm.errText
            })
      },
      doRegister() {
        if (this.form.fName && this.form.sName && this.form.login && this.form.pwd) {
          if (this.form.email.match('[a-zA-Z]+@[a-zA-Z]+.[a-zA-Z]+') !== null &&
          this.form.phone.match('[0-9]{10}') !== null) {
            let data = {
              fName: this.form.fName,
              sName: this.form.sName,
              login: this.form.login,
              pwd: this.form.pwd,
              phone: this.form.phone,
              email: this.form.email,
              avatar: this.form.avatar
            }
            axios({
              url: `http://${ip}:${port}/api/register`,
              method: 'POST',
              data: data
            })
                .then(() => {
                  axios({
                    url: `http://${ip}:${port}/api/auth`,
                    method: 'POST',
                    data: {
                      login: this.form.login,
                      pwd: this.form.pwd
                    }
                  }).then(resp => {
                    localStorage.setItem('uid', resp.data['id_token'])
                    this.info.userInfo = data
                    this.successForm = true;
                    setTimeout(() => {
                      for (let item of Object.keys(this.form)) {
                        this.form[item] = '';
                      }
                      this.register = false
                      window.location.reload()
                    }, 1500)
                  }).catch(() => {
                    this.errForm = true
                    this.errText = this.curLocale.registerForm.errText
                  })
                })
                .catch(() => {
                  this.errForm = true
                  this.errText = this.curLocale.registerForm.errText
                })
          }
        }
      }
    }
  }
</script>

<style lang="scss">
</style>
