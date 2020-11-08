<template>
  <v-app>
    <v-form>
      <v-container>
        <v-row>
          <v-col cols="12">
            <v-text-field
                :label="curLocale.authForm.labels[0]"
                v-model="form.login"
                :rules="form.textRules"
                prepend-icon="person"
                required
            ></v-text-field>
          </v-col>
          <v-col>
            <v-text-field
                type="password"
                :label="curLocale.authForm.labels[1]"
                v-model="form.pwd"
                :rules="form.textRules"
                prepend-icon="lock"
                required
            ></v-text-field>
          </v-col>
        </v-row>
        <v-btn @click="doAuth" color="success" block>
          {{curLocale.authForm.btnTitle}}
        </v-btn>
      </v-container>
    </v-form>
  </v-app>
</template>

<script>
const ip = "192.168.0.153"
const port = "9000"
const axios = require('axios')
export default {
  name: "AdminAuth",
  data() {
    return {
      form: {
        login: '',
        pwd: '',
        textRules: [
          v => v.length !== 0 || this.curLocale.authForm.rulesText[0]
        ],
      },
      curLocale: null,
      locales: {
        locales: {
          'en-EN': {
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
  methods: {
    doAuth() {
      if (this.form.login && this.form.pwd !== '') {
        axios.get(`http://${ip}:${port}/`)
      }
    }
  }
}
</script>

<style scoped>

</style>