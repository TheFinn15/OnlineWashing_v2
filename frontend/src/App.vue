<template>
  <v-app>
    <v-app-bar app color="indigo">
      <v-toolbar-title style="color: white">{{curLocale.toolbarTitle}}</v-toolbar-title>
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
              <v-list-item @click="$router.push('/balance')">
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
              <v-list-item @click="$router.push('/')">
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
              <v-col>
                <v-text-field
                    :label="curLocale.authForm.labels[0]"
                    v-model="login"
                    :rules="textRules"
                    required
                ></v-text-field>
              </v-col>
            </v-row>
            <v-row>
              <v-col>
                <v-text-field
                    type="password"
                    :label="curLocale.authForm.labels[1]"
                    v-model="pwd"
                    :rules="textRules"
                    required
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
          <v-btn @click="doAuth" color="success" width="100%">{{curLocale.authForm.btnTitle}}</v-btn>
        </v-form>
      </v-card>
    </v-dialog>
    <v-dialog v-model="register" max-width="600px" persistent>
      <v-card>
        <v-card-title>Регистрация</v-card-title>
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
          Регистрация успешна
        </v-alert>
        <v-container>
          <v-row>
            <v-col>
              <v-text-field
                  outlined
                  label="Имя:"
                  v-model="form.fName"
                  :rules="textRules"
                  required
              ></v-text-field>
              <v-text-field
                  outlined
                  label="Фамилия:"
                  v-model="form.sName"
                  :rules="textRules"
                  required
              ></v-text-field>
            </v-col>
            <v-col>
              <v-text-field
                  outlined
                  label="Логин:"
                  v-model="form.login"
                  :rules="textRules"
                  required
              ></v-text-field>
              <v-text-field
                  type="password"
                  outlined
                  label="Пароль:"
                  v-model="form.pwd"
                  :rules="textRules"
                  required
              ></v-text-field>
            </v-col>
          </v-row>
          <v-row>
            <v-col>
              <v-text-field
                  outlined
                  label="Телефон:"
                  v-model="form.phone"
                  :rules="phoneRules"
                  required
              ></v-text-field>
              <v-text-field
                  outlined
                  label="E-mail:"
                  v-model="form.email"
                  :rules="emailRules"
                  required
              ></v-text-field>
            </v-col>
            <v-col title="Ваш аватар">
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
        </v-container>
        <v-btn @click="doRegister" color="indigo" outlined block class="pa-2">СОЗДАТЬ АККАУНТ</v-btn>
      </v-card>
    </v-dialog>
    <router-view></router-view>
  </v-app>
</template>

<script>
  const ip = "192.168.0.112"
  const port = "9000"
  const axios = require('axios')
  export default {
    name: 'App',
    data() {
      return {
        info: {
          userInfo: null
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
              labels: [
                'Login*', 'Password*'
              ],
              rulesText: [
                'This field can not be a empty'
              ],
              btnTitle: 'ENTER'
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
              labels: [
                  'Логин*', 'Пароль*'
              ],
              rulesText: [
                  'Это поле не может быть пустым'
              ],
              btnTitle: 'ВОЙТИ'
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
              labels: [
                'Логін*', 'Пароль*'
              ],
              rulesText: [
                'Поле не може бути порожнім'
              ],
              btnTitle: 'ВІЙТИ'
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
            v => v.match('[0-9]{10}') !== null || 'Введите верный телефон'
        ],
        emailRules: [
            v => v.match('[a-zA-Z]+@[a-zA-Z]+.[a-zA-Z]+') !== null || 'Введите верный e-mail'
        ]
      }
    },
    mounted() {
      if (localStorage.getItem('uid') !== null) {
        axios.get(`http://${ip}:${port}/api/persons`)
          .then(resp => {
            let uId = resp.data.filter(i => i.sessionId === localStorage.getItem('uid'))
            uId = uId[uId.length-1].id
            let req = new XMLHttpRequest()
            req.open('GET', `http://${ip}:${port}/api/persons/${uId}`)
            req.send()
            req.onload = () => {
              this.info.userInfo = JSON.parse(req.responseText)
              if (this.info.userInfo.wallet.balance === null || this.info.userInfo.wallet.balance === 0 || this.info.userInfo.wallet.balance === undefined) {
                this.info.userInfo.wallet.balance =  "00, 00"
              } else {
                this.info.userInfo.wallet.balance = this.info.userInfo.wallet.balance.toString() + ", 00";
              }
            }
          })
        this.isAuth = true;
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
      doLoadAvatar(ev) {
        let reader = new FileReader();
        reader.onload = (e) => {
          this.form.avatar = e.target.result
        }
        reader.readAsDataURL(ev);
        this.imgSelected = true;
      },
      doLogout() {
        axios.get(`http://${ip}:${port}/api/persons`)
            .then(resp => {
              let uId = resp.data.filter(i => i.sessionId === localStorage.getItem('uid'))
              uId = uId[uId.length-1].id
              axios.put(`http://${ip}:${port}/api/persons/`+uId, {
                sessionId: ''
              })
                  .then(resp1 => {
                    console.log(resp1)
                    localStorage.removeItem('uid')
                  })
                  .catch(err => console.log('catch error', err))
            })
        this.$forceUpdate()
        window.history.go();
      },
      changeLangEN() {
        localStorage.setItem('lang', 'en-EN')
        this.curLocale = this.locales["en-EN"];
        this.$router.go('/cabinet');
      },
      changeLangRU() {
        localStorage.setItem('lang', 'ru-RU')
        this.curLocale = this.locales["ru-RU"];
        this.$router.go('/cabinet');
      },
      changeLangUA() {
        localStorage.setItem('lang', 'ua-UA')
        this.curLocale = this.locales["ua-UA"];
        this.$router.go('/cabinet');
      },
      doAuth() {
        let req = new XMLHttpRequest()
        req.open('GET', `http://${ip}:${port}/api/persons`, false)
        req.send()
        let userId = null
        let info = JSON.parse(req.responseText).filter(i => {
          if (i.login === this.login && i.pwd === this.pwd) {
            userId = i.id;
            return i;
          }
        })
        if (info.length > 0) {
          this.errForm = false
          let alpha = 'abcdefghijklmnopqrstuvwxyz'.split('');
          let sId = ''
          for (let i=0;i<25;i++) {
            let randChoose = Math.floor(Math.random()*2)
            if (randChoose === 0) {
              sId += alpha[i]
            } else {
              sId += Math.floor(Math.random()*10)
            }
          }
          axios.put(`http://${ip}:${port}/api/persons/`+userId, {
            sessionId: sId.toUpperCase()
          })
              .then(resp => {
                console.log(resp)
                localStorage.setItem('uid', sId.toUpperCase())
              })
              .catch(err => console.log('catch error', err))
          this.successForm = true;
          setTimeout(() => {
            this.login = ''
            this.pwd = ''
            this.authForm = false
            this.info.userInfo = info
            window.location.reload()
          }, 1500)
        } else {
          this.errForm = true
          this.errText = 'Введены неверные данные'
        }
      },
      doRegister() {
        if (this.form.fName && this.form.sName && this.form.login && this.form.pwd) {
          if (this.form.email.match('[a-zA-Z]+@[a-zA-Z]+.[a-zA-Z]+') !== null &&
          this.form.phone.match('[0-9]{10}') !== null) {
            if (this.form.avatar === '') this.form.avatar = 'data:image/png;base64,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'
            let alpha = 'abcdefghijklmnopqrstuvwxyz'.split('');
            let sId = ''
            for (let i=0;i<25;i++) {
              let randChoose = Math.floor(Math.random()*2)
              if (randChoose === 0) {
                sId += alpha[i]
              } else {
                sId += Math.floor(Math.random()*10)
              }
            }
            let data = {
              fName: this.form.fName,
              sName: this.form.sName,
              login: this.form.login,
              pwd: this.form.pwd,
              phone: this.form.phone,
              email: this.form.email,
              avatar: this.form.avatar,
              sessionId: sId
            }
            axios({
              url: `http://${ip}:${port}/api/persons`,
              method: 'POST',
              data: data
            })
                .then(resp => {
                  console.log(resp)
                  localStorage.setItem('uid', sId)
                  this.info.userInfo = data
                })
                .catch(err => {
                  this.errForm = true
                  this.errText = 'Ошибка регистрация. Попробуйте позже'
                  console.log(err)
                })
            this.successForm = true;
            setTimeout(() => {
              for (let item of Object.keys(this.form)) {
                this.form[item] = '';
              }
              this.register = false
              window.location.reload()
            }, 1500)
          }
        }
      }
    }
  }
</script>

<style lang="scss">
</style>
