<template>
  <v-app style="z-index: 5">
    <v-card style="margin: 5% 15% 0 15%">
      <v-tabs grow color="indigo">
        <v-tab>Администрирование</v-tab>
        <v-tab>Отчеты</v-tab>
        <v-tab>Статистика</v-tab>
        <v-tab-item>
          <v-text-field
              style="margin: 2%"
              full-width
              label="Введите запрос"
              append-icon="search"
              outlined
          ></v-text-field>
          <v-card-title>Управление данными</v-card-title>
          <v-divider></v-divider>
          <v-container>
            <v-row>
              <v-col cols="4" v-for="(item, i) in tablesInfo.tablesName" :key="i">
                <v-card max-width="200" style="margin: 2%" :name="item">
                  <v-card-subtitle>{{ item }}</v-card-subtitle>
                  <v-card-text>
                    <b>Кол-во записей:</b> <br/> {{tablesInfo.countsRows[item.toLowerCase()]}} <br/>
                    <b>Дата изменения:</b> <br/> {{tablesInfo.datesEdits[[item.toLowerCase()]]}}
                  </v-card-text>
                  <br>
                  <br>
                  <v-btn color="indigo" outlined width="100%" @click="handleTable">Подробнее</v-btn>
                </v-card>
              </v-col>
            </v-row>
          </v-container>
          <v-dialog max-width="650" persistent v-model="fullInfo" v-if="chosenTable !== null">
            <v-card>
              <v-card-title>
                Управление {{ chosenTable }}
                <v-spacer></v-spacer>
                <v-btn icon @click="fullInfo = false">
                  <v-icon>
                    close
                  </v-icon>
                </v-btn>
              </v-card-title>
              <v-text-field
                  style="margin: 2%"
                  full-width
                  :label="'Введите запрос к ' + chosenTable"
                  append-icon="search"
                  outlined
              ></v-text-field>
              <v-snackbar top text timeout="2000" v-model="alertSuccess" color="success">
                {{alertText}}
              </v-snackbar>
              <v-divider></v-divider>
              <v-card style="justify-content: center; display: flex; margin: 2%" flat tile>
                <v-hover v-slot:default="{hover}">
                  <v-btn outlined elevation="3" color="indigo" @click="showAddForm = true">
                    <span v-if="hover">Добавить</span>
                    <v-icon>
                      add
                    </v-icon>
                  </v-btn>
                </v-hover>
                <v-hover v-slot:default="{hover}">
                  <v-btn outlined elevation="3" color="indigo" @click="modeEdit = !modeEdit">
                    <span v-if="hover">Изменить</span>
                    <v-icon>
                      edit
                    </v-icon>
                  </v-btn>
                </v-hover>
                <v-hover v-slot:default="{hover}">
                  <v-btn outlined elevation="3" color="indigo" @click="modeDel = !modeDel">
                    <span v-if="hover">Удалить</span>
                    <v-icon>
                      delete
                    </v-icon>
                  </v-btn>
                </v-hover>
              </v-card>
              <v-divider></v-divider>
              <PersonsList
                  :updater="updater"
                  :show-edit="showEditForm"
                  :show-del="showDelForm"
                  :item-info="forms.persons"
                  :table-info="chosenTableInfo"
                  :mode-del="modeDel"
                  :mode-edit="modeEdit"
                  v-if="chosenTable === 'Persons'"
              />
              <MachineList
                  :updater="updater"
                  :show-edit="showEditForm"
                  :show-del="showDelForm"
                  :item-info="forms.machines"
                  :table-info="chosenTableInfo"
                  :mode-del="modeDel"
                  :mode-edit="modeEdit"
                  v-if="chosenTable === 'Machines'"
              />
            </v-card>
          </v-dialog>
          <v-dialog max-width="1000" persistent v-model="showAddForm">
            <v-card>
              <v-card-title>
                Добавление записи в таблицу {{chosenTable}}
                <v-spacer></v-spacer>
                <v-btn icon @click="showAddForm = false">
                  <v-icon>
                    close
                  </v-icon>
                </v-btn>
              </v-card-title>
              <v-divider></v-divider>
              <v-tabs grow color="indigo">
                <v-tab>Обязательные поля</v-tab>
                <v-tab>Необязательные поля</v-tab>
                <v-tab-item>
                  <v-container>
                    <v-row>
                      <v-col>
                        <v-row>
                          <v-col>
                            <v-text-field
                                label="Имя"
                                outlined
                                :rules="rulesText"
                                required
                                v-model="forms.persons.fName"
                            ></v-text-field>
                            <v-text-field
                                label="Фамилия"
                                outlined
                                :rules="rulesText"
                                required
                                v-model="forms.persons.sName"
                            ></v-text-field>
                            <v-text-field
                                label="Логин"
                                outlined
                                :rules="rulesText"
                                required
                                v-model="forms.persons.login"
                            ></v-text-field>
                          </v-col>
                          <v-col>
                            <v-text-field
                                type="password"
                                label="Пароль"
                                outlined
                                :rules="rulesText"
                                required
                                v-model="forms.persons.pwd"
                            ></v-text-field>
                            <v-text-field
                                label="E-mail"
                                outlined
                                :rules="emailRules"
                                required
                                v-model="forms.persons.email"
                            ></v-text-field>
                            <v-text-field
                                label="Телефон"
                                outlined
                                :rules="phoneRules"
                                required
                                v-model="forms.persons.phone"
                            ></v-text-field>
                          </v-col>
                        </v-row>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-tab-item>
                <v-tab-item>
                  <v-container>
                    <v-row>
                      <v-col>
                        <v-card-subtitle>Доступные машини:</v-card-subtitle>
                        <div v-if="editForm.machines.length > 0 ">
                          <v-checkbox v-for="(item, i) in editForm.machines" :label="item.name + ' ' + item.capacity" :key="i"></v-checkbox>
                        </div>
                        <v-checkbox readonly label="Машини отсутствуют"></v-checkbox>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-tab-item>
              </v-tabs>
              <v-divider></v-divider>
              <v-btn @click="doAdd" outlined color="indigo" width="100%">Создать</v-btn>
            </v-card>
          </v-dialog>
          <v-dialog max-width="1000" persistent v-model="showEditForm">
            <v-card>
              <v-card-title>
                Изменение записи в таблице {{chosenTable}}
                <v-spacer></v-spacer>
                <v-btn icon @click="showEditForm = false">
                  <v-icon>
                    close
                  </v-icon>
                </v-btn>
              </v-card-title>
              <v-divider></v-divider>
              <v-tabs grow color="indigo">
                <v-tab>Обязательные поля</v-tab>
                <v-tab>Необязательные поля</v-tab>
                <v-tab-item>
                  <v-container>
                    <v-row>
                      <v-col>
                        <v-img width="200" height="185" :src="forms.persons.avatar"></v-img>
                        <v-file-input
                            full-width
                            label="Аватар пользователя"
                            append-icon="mdi-camera"
                            v-model="forms.persons.avatar"
                            @change="loadImg"
                            outlined
                        ></v-file-input>
                      </v-col>
                      <v-col>
                        <v-row>
                          <v-col>
                            <v-text-field
                                label="Имя"
                                :placeholder="editForm.persons.fName"
                                outlined
                                :rules="rulesText"
                                v-model="forms.persons.fName"
                            ></v-text-field>
                            <v-text-field
                                label="Фамилия"
                                :placeholder="editForm.persons.sName"
                                outlined
                                :rules="rulesText"
                                v-model="forms.persons.sName"
                            ></v-text-field>
                            <v-text-field
                                label="Логин"
                                :placeholder="editForm.persons.login"
                                outlined
                                :rules="rulesText"
                                v-model="forms.persons.login"
                            ></v-text-field>
                          </v-col>
                          <v-col>
                            <v-text-field
                                type="password"
                                label="Пароль"
                                :placeholder="editForm.persons.pwd"
                                outlined
                                :rules="rulesText"
                                v-model="forms.persons.pwd"
                            ></v-text-field>
                            <v-text-field
                                label="E-mail"
                                :placeholder="editForm.persons.email"
                                outlined
                                :rules="emailRules"
                                v-model="forms.persons.email"
                            ></v-text-field>
                            <v-text-field
                                label="Телефон"
                                :placeholder="editForm.persons.phone"
                                outlined
                                :rules="phoneRules"
                                v-model="forms.persons.phone"
                            ></v-text-field>
                          </v-col>
                        </v-row>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-tab-item>
                <v-tab-item>
                  <v-container>
                    <v-row>
                      <v-col>
                        <v-card-subtitle>Доступные машини:</v-card-subtitle>
                        <div v-if="forms.persons.machine.length > 0 ">
                          <v-checkbox v-for="(item, i) in forms.persons.machine" :label="item.name + ' ' + item.capacity" :key="i"></v-checkbox>
                        </div>
                        <v-checkbox readonly label="Машини отсутствуют"></v-checkbox>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-tab-item>
              </v-tabs>
              <v-divider></v-divider>
              <v-btn @click="doEdit" outlined color="indigo" width="100%">Изменить</v-btn>
            </v-card>
          </v-dialog>
          <v-dialog max-width="400" persistent v-model="showDelForm">
            <v-card>
              <v-card-title>
                Сейчас будет удалена запись из {{chosenTable}}. <br/> Продолжить ?
              </v-card-title>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn @click="showDelForm = false" text color="green">Нет, отмена.</v-btn>
                <v-btn @click="doRemove" text color="red">Да, удалить.</v-btn>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-tab-item>
        <v-tab-item></v-tab-item>
        <v-tab-item></v-tab-item>
      </v-tabs>
    </v-card>
  </v-app>
</template>

<script>
import PersonsList from "@/components/tables/PersonsList";
import MachineList from "@/components/tables/MachineList";
const ip = "192.168.0.153"
const port = '9000'
const axios = require('axios')

export default {
  name: "AdminCabinet",
  components: {MachineList, PersonsList},
  data() {
    return {
      tablesInfo: {
        tablesName: [
          'Persons', 'Machines', 'Stocks', 'Drafts', 'Wallets', 'HistoryTransactions'
        ],
        countsRows: {
          persons: '',
          machines: '',
          stocks: '',
          drafts: '',
          wallets: '',
          historytransactions: ''
        },
        datesEdits: {
          persons: '',
          machines: '',
          stocks: '',
          drafts: '',
          wallets: '',
          historytransactions: ''
        }
      },
      chosenTableInfo: null,
      chosenTable: null,
      selectedItem: null,
      fullInfo: false,
      showAddForm: false,
      showEditForm: false,
      showDelForm: false,
      modeEdit: false,
      modeDel: false,
      editForm: {
        machines: [],
        persons: [],
        wallets: [],
        histories: [],
        stocks: []
      },
      rulesText: [
          v => v.length > 0 || 'Это поле не может быть пустым'
      ],
      phoneRules: [
        v => v.length === 10 || 'Введите коректный номер'
      ],
      emailRules: [
        v => v.match("\\w+@[a-zA-Z]+[.][a-zA-Z]+") !== null || 'Введите коректный e-mail'
      ],
      alertSuccess: false,
      alertErr: false,
      alertText: '',
      forms: {
        persons: {
          fName: '',
          sName: '',
          login: '',
          email: '',
          pwd: '',
          phone: '',
          avatar: '',
          machine: ''
        },
        machines: {
          stock: '',
          name: '',
          capacity: '',
          description: '',
          status: '',
          price: '',
          img: ''
        },
        drafts: {
          person: '',
          machine: '',
          price: '',
          volume: '',
          additional: '',
          paymentType: '',
          creditCard: ''
        },
        wallets: {
          balance: ''
        },
        histories: {
          sum: '',
          date: ''
        },
        stocks: {
          name: '',
          sponsor: '',
          lastTerm: '',
          discount: ''
        }
      }
    }
  },
  methods: {
    updater(info) {
      this.forms[this.chosenTable.toLowerCase()] = info.infoItem
        if (info.showEdit) this.showEditForm = true
        else if (info.showDel) this.showDelForm = true
    },
    loadImg(ev) {
      let reader = new FileReader()
      reader.onload = (e) => {
        this.forms[this.chosenTable.toLowerCase()].avatar = e.target.result
      }
      reader.readAsDataURL(ev);
    },
    doAdd() {
      try {
        let table = this.chosenTable
        let curForms = this.forms[table.toLowerCase()]
        console.log('info', curForms)
        if (curForms.machine === '') curForms.machine = []
        axios({
          method: "POST",
          url: `http://${ip}:${port}/api/${table.toLowerCase()}`,
          data: this.forms[table.toLowerCase()]
        }).then(resp => {
          console.log(resp)
          this.chosenTableInfo.push(resp.data)
        })
        for (let item of Object.keys(this.forms[table.toLowerCase()])) {
          this.forms[table.toLowerCase()][item] = '';
        }
        this.$nextTick(() => {
          this.showAddForm = false;
          this.alertSuccess = true;
          this.alertText = 'Успешно добавлена запись';
        })
      } catch (e) {
        console.log('err', e)
        this.$nextTick(() => {
          this.alertErr = true;
          this.alertText = 'Ошибка сервера';
        })
      }
    },
    doEdit() {
      try {
        let table = this.chosenTable
        let curItem = this.forms[table.toLowerCase()].id
        axios({
          method: "PUT",
          url: `http://${ip}:${port}/api/${table.toLowerCase()}/${curItem}`,
          data: this.forms[table.toLowerCase()]
        }).then(resp => {
          console.log(resp)
          this.chosenTableInfo = this.chosenTableInfo.filter(i => i.id !== resp.data.id)
          this.chosenTableInfo.push(resp.data)
        })
        for (let item of Object.keys(this.forms[table.toLowerCase()])) {
          this.forms[table.toLowerCase()][item] = '';
        }
        this.$nextTick(() => {
          this.showEditForm = false;
          this.alertSuccess = true;
          this.alertText = 'Запись успешно обновлена'
        })
      } catch (e) {
        console.log('err', e)
        this.$nextTick(() => {
          this.alertErr = true;
        })
      }
    },
    doRemove() {
      try {
        let table = this.chosenTable
        let curItem = this.forms[table.toLowerCase()].id
        axios({
          method: "DELETE",
          url: `http://${ip}:${port}/api/${table.toLowerCase()}/${curItem}`
        }).then(resp => console.log(resp))
        this.fullInfo = false
        for (let item of Object.keys(this.forms[table.toLowerCase()])) {
          this.forms[table.toLowerCase()][item] = '';
        }
        this.$nextTick(() => {
          this.chosenTableInfo = this.chosenTableInfo.filter(i => i.id !== curItem)
          this.showDelForm = false;
          this.alertSuccess = true;
          this.alertText = 'Запись успешно удалена'
        })
        this.mounted();
      } catch (e) {
        console.log('err', e)
        this.$nextTick(() => {
          this.alertErr = true;
        })
      }
    },
    handleTable(ev) {
      this.fullInfo = true;
      this.chosenTable = ev.path[0].offsetParent.offsetParent.attributes.getNamedItem('name').value || ev.path[0].offsetParent.attributes.getNamedItem('name').value
      axios({
        method: 'GET',
        url: `http://${ip}:${port}/api/${this.chosenTable.toLowerCase()}`,
      }).then(resp => (this.chosenTableInfo = resp.data))
    }
  },
  mounted() {
    axios.get(`http://${ip}:${port}/api/persons`)
      .then(resp => {
        if (resp.data.length > 0) {
          this.tablesInfo.countsRows.persons = resp.data.length
          this.editForm.persons = resp.data
          this.tablesInfo.datesEdits.persons = resp.data[resp.data.length-1].lastUpdateRow
        } else {
          this.tablesInfo.countsRows.persons = 0
          this.tablesInfo.datesEdits.persons = 'Отсутствует'
        }
      })
    axios.get(`http://${ip}:${port}/api/machines`)
        .then(resp => {
          if (resp.data.length > 0) {
            this.tablesInfo.countsRows.machines = resp.data.length
            this.editForm.machines = resp.data
            this.tablesInfo.datesEdits.machines = resp.data[resp.data.length-1].lastUpdateRow
          } else {
            this.tablesInfo.countsRows.machines = 0
            this.tablesInfo.datesEdits.machines = 'Отсутствует'
          }
        })
    axios.get(`http://${ip}:${port}/api/drafts`)
        .then(resp => {
          if (resp.data.length > 0) {
            this.tablesInfo.countsRows.drafts = resp.data.length
            this.tablesInfo.datesEdits.drafts = resp.data[resp.data.length-1].lastUpdateRow
          } else {
            this.tablesInfo.countsRows.drafts = 0
            this.tablesInfo.datesEdits.drafts = 'Отсутствует'
          }
        })
    axios.get(`http://${ip}:${port}/api/stocks`)
        .then(resp => {
          if (resp.data.length > 0) {
            this.tablesInfo.countsRows.stocks = resp.data.length
            this.editForm.stocks = resp.data
            this.tablesInfo.datesEdits.stocks = resp.data[resp.data.length-1].lastUpdateRow
          } else {
            this.tablesInfo.countsRows.stocks = 0
            this.tablesInfo.datesEdits.stocks = 'Отсутствует'
          }
        })
    axios.get(`http://${ip}:${port}/api/wallets`)
        .then(resp => {
          if (resp.data.length > 0) {
            this.tablesInfo.countsRows.wallets = resp.data.length
            this.editForm.wallets = resp.data
            this.tablesInfo.datesEdits.wallets = resp.data[resp.data.length-1].lastUpdateRow
          } else {
            this.tablesInfo.countsRows.wallets = 0
            this.tablesInfo.datesEdits.wallets = 'Отсутствует'
          }
        })
    axios.get(`http://${ip}:${port}/api/histories`)
        .then(resp => {
          if (resp.data.length > 0) {
            this.tablesInfo.countsRows.histories = resp.data.length
            this.editForm.histories = resp.data
            this.tablesInfo.datesEdits.historytransactions = resp.data[resp.data.length-1].lastUpdateRow
          } else {
            this.tablesInfo.countsRows.histories = 0
            this.tablesInfo.datesEdits.historytransactions = 'Отсутствует'
          }
        })
  }
}
</script>

<style scoped>

</style>