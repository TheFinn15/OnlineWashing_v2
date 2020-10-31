<template>
  <v-app style="z-index: 5">
    <v-card style="margin: 5% 15% 0 15%">
      <v-tabs grow color="indigo">
        <v-tab>Администрирование</v-tab>
        <v-tab>Отчеты</v-tab>
        <v-tab>Статистика</v-tab>
        <v-tab-item>
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
          <v-dialog scrollable max-width="650" persistent v-model="fullInfo" v-if="chosenTable !== null">
            <v-card>
              <v-card-title>
                Управление {{ chosenTable }}
                <v-spacer></v-spacer>
                <v-btn icon @click="doCloseTable">
                  <v-icon>
                    close
                  </v-icon>
                </v-btn>
              </v-card-title>
              <v-text-field
                  style="margin: 2%"
                  full-width
                  v-model="searchTextChosenTable"
                  :label="'Введите запрос к ' + chosenTable"
                  append-icon="search"
                  @input="doSearchInTable"
                  outlined
              ></v-text-field>
              <v-snackbar top timeout="2000" v-model="alertSuccess" color="success">
                {{alertText}}
              </v-snackbar>
              <v-snackbar top timeout="2000" v-model="alertErr" color="error">
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
                  style="overflow-y: scroll; height: 100%"
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
                  style="overflow-y: scroll; height: 100%"
              />
              <StockList
                  :updater="updater"
                  :show-edit="showEditForm"
                  :show-del="showDelForm"
                  :item-info="forms.stocks"
                  :table-info="chosenTableInfo"
                  :mode-del="modeDel"
                  :mode-edit="modeEdit"
                  v-if="chosenTable === 'Stocks'"
                  style="overflow-y: scroll; height: 100%"
              />
              <DraftList
                  :updater="updater"
                  :show-edit="showEditForm"
                  :show-del="showDelForm"
                  :item-info="forms.drafts"
                  :table-info="chosenTableInfo"
                  :mode-del="modeDel"
                  :mode-edit="modeEdit"
                  v-if="chosenTable === 'Drafts'"
                  style="overflow-y: scroll; height: 100%"
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
                  <v-container v-if="chosenTable === 'Persons'">
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
                  <v-container v-if="chosenTable === 'Machines'">
                    <v-row>
                      <v-col>
                        <v-text-field
                            label="Название машины"
                            outlined
                            :rules="rulesText"
                            required
                            v-model="forms.machines.name"
                        ></v-text-field>
                      </v-col>
                      <v-col>
                        <v-text-field
                            label="Описание"
                            outlined
                            :rules="rulesText"
                            required
                            v-model="forms.machines.description"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-text-field
                            label="Вместимость в л."
                            type="number"
                            outlined
                            :rules="rulesNum"
                            required
                            v-model="forms.machines.capacity"
                        ></v-text-field>
                      </v-col>
                      <v-col>
                        <v-text-field
                            label="Цена в грн."
                            type="number"
                            outlined
                            :rules="rulesNum"
                            required
                            v-model="forms.machines.price"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-select
                            label="Статус машины"
                            outlined
                            :items="['Рабочая', 'Не работает']"
                            v-model="forms.machines.status"
                        ></v-select>
                      </v-col>
                    </v-row>
                  </v-container>
                  <v-container v-if="chosenTable === 'Stocks'">
                    <v-row>
                      <v-col>
                        <v-text-field
                            label="Название акции"
                            outlined
                            :rules="rulesText"
                            required
                            v-model="forms.stocks.name"
                        ></v-text-field>
                      </v-col>
                      <v-col>
                        <v-text-field
                            label="Спонсор акции"
                            outlined
                            :rules="rulesText"
                            required
                            v-model="forms.stocks.sponsor"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-text-field
                            label="Скидка в %"
                            type="number"
                            outlined
                            :rules="rulesNum"
                            required
                            v-model="forms.stocks.discount"
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
                                v-model="forms.stocks.lastTerm"
                                label="Дата истечения акции"
                                persistent-hint
                                prepend-icon="mdi-calendar"
                                readonly
                                v-bind="attrs"
                                v-on="on"
                            ></v-text-field>
                          </template>
                          <v-date-picker
                              v-model="chosenDate"
                              no-title
                              @input="reFormateDate"
                          ></v-date-picker>
                        </v-menu>
                      </v-col>
                    </v-row>
                  </v-container>
                  <v-container v-if="chosenTable === 'Drafts'">
                    <v-row>
                      <v-col>
                        <v-text-field
                            type="number"
                            label="Объем вещей в кг"
                            outlined
                            :rules="rulesNum"
                            required
                            v-model="forms.drafts.volume"
                        ></v-text-field>
                      </v-col>
                      <v-col>
                        <v-text-field
                            type="number"
                            label="Цена"
                            outlined
                            :rules="rulesNum"
                            required
                            v-model="forms.drafts.price"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-select
                            label="Вид оплаты:"
                            outlined
                            :items="['Наличкой', 'Картой']"
                            required
                            v-model="forms.drafts.paymentType"
                        ></v-select>
                      </v-col>
                      <v-col v-if="forms.drafts.paymentType === 'Картой'">
                        <v-text-field
                            type="number"
                            label="Номер кредитной карты:"
                            outlined
                            :rules="rulesNum"
                            required
                            v-model="forms.drafts.creditCard"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-select
                            label="Пользователь:"
                            outlined
                            :items="renderUserForChoose"
                            required
                            v-model="forms.drafts.person"
                        ></v-select>
                      </v-col>
                      <v-col>
                        <v-select
                            label="Стиральная машина:"
                            outlined
                            :items="renderMachineForChoose"
                            required
                            v-model="forms.drafts.machine"
                        ></v-select>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-select
                            label="Хим. добавки:"
                            outlined
                            :items="['Кондиционер `Lenor`']"
                            required
                            chips
                            multiple
                            v-model="forms.drafts.additional"
                        ></v-select>
                      </v-col>
                      <v-col>
                        <v-text-field
                            label="Дата оплаты:"
                            outlined
                            readonly
                            v-model="forms.drafts.date"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-tab-item>
                <v-tab-item>
                  <v-container v-if="chosenTable === 'Persons'">
                    <v-row>
                      <v-col>
                        <v-card-subtitle>Доступные машини:</v-card-subtitle>
                        <div v-if="editForm.machines.length > 0 ">
                          <v-checkbox v-for="(item, i) in editForm.machines" :label="item.name + ' ' + item.capacity" :key="i"></v-checkbox>
                        </div>
                        <v-checkbox v-else readonly label="Машини отсутствуют"></v-checkbox>
                      </v-col>
                    </v-row>
                  </v-container>
                  <v-container v-if="chosenTable === 'Machines'">
                    <v-row>
                      <v-card-subtitle>Доступные акции сервиса:</v-card-subtitle>
                      <div v-if="editForm.stocks.length > 0 ">
                        <v-checkbox v-for="(item, i) in editForm.stocks" :label="item.name + ' ' + item.sponsor + ' ' + item.lastTerm" :key="i"></v-checkbox>
                      </div>
                      <v-checkbox v-else readonly label="Акции отсутствуют"></v-checkbox>
                    </v-row>
                  </v-container>
                  <v-container v-if="chosenTable === 'Stocks'" style="padding: 4%; overflow: hidden">
                    <v-row>
                      <v-col>
                        <div>
                          <v-icon large style="text-align: center; display: block">
                            warning
                          </v-icon>
                          <v-card-title style="text-align: center; display: block">
                            Отсутствуют
                          </v-card-title>
                        </div>
                      </v-col>
                    </v-row>
                  </v-container>
                  <v-container v-if="chosenTable === 'Drafts'" style="padding: 4%; overflow: hidden">
                    <v-row>
                      <v-col>
                        <div>
                          <v-icon large style="text-align: center; display: block">
                            warning
                          </v-icon>
                          <v-card-title style="text-align: center; display: block">
                            Отсутствуют
                          </v-card-title>
                        </div>
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
                  <v-container v-if="chosenTable === 'Persons'">
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
                  <v-container v-if="chosenTable === 'Machines'">
                    <v-row>
                      <v-col title="Картинка машины" cols="5">
                        <v-row no-gutters align="end" justify="end">
                          <v-col>
                            <v-img v-model="forms.machines.img" :src="forms.machines.img" width="150" height="150" style="border: 1px solid black"></v-img>
                          </v-col>
                          <v-col>
                            <v-file-input
                                hide-input
                                @change="loadImg"
                            ></v-file-input>
                          </v-col>
                        </v-row>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-text-field
                            label="Название машины"
                            outlined
                            :rules="rulesText"
                            required
                            :placeholder="editForm.machines.name"
                            v-model="forms.machines.name"
                        ></v-text-field>
                      </v-col>
                      <v-col>
                        <v-text-field
                            label="Описание"
                            outlined
                            :rules="rulesText"
                            required
                            :placeholder="editForm.machines.description"
                            v-model="forms.machines.description"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-text-field
                            label="Вместимость в л."
                            type="number"
                            outlined
                            :rules="rulesNum"
                            required
                            :placeholder="editForm.machines.capacity"
                            v-model="forms.machines.capacity"
                        ></v-text-field>
                      </v-col>
                      <v-col>
                        <v-text-field
                            label="Цена в грн."
                            type="number"
                            outlined
                            :rules="rulesNum"
                            required
                            :placeholder="editForm.machines.price"
                            v-model="forms.machines.price"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-select
                            label="Статус машины"
                            outlined
                            :items="['Рабочая', 'Не работает']"
                            :placeholder="editForm.machines.status"
                            v-model="forms.machines.status"
                        ></v-select>
                      </v-col>
                    </v-row>
                  </v-container>
                  <v-container v-if="chosenTable === 'Stocks'">
                    <v-row>
                      <v-col>
                        <v-text-field
                            label="Название акции"
                            outlined
                            :rules="rulesText"
                            :placeholder="editForm.stocks.name"
                            required
                            v-model="forms.stocks.name"
                        ></v-text-field>
                      </v-col>
                      <v-col>
                        <v-text-field
                            label="Спонсор акции"
                            outlined
                            :rules="rulesText"
                            :placeholder="editForm.stocks.sponsor"
                            required
                            v-model="forms.stocks.sponsor"
                        ></v-text-field>
                      </v-col>
                    </v-row>
                    <v-row>
                      <v-col>
                        <v-text-field
                            label="Скидка в %"
                            type="number"
                            outlined
                            :rules="rulesNum"
                            :placeholder="editForm.stocks.discount"
                            required
                            v-model="forms.stocks.discount"
                        ></v-text-field>
                      </v-col>
                      <v-col>
                        <v-date-picker
                            elevation="15"
                            color="indigo"
                            v-model="forms.stocks.lastTerm"
                            full-width
                            landscape
                        >
                          Дата истечения
                        </v-date-picker>
                      </v-col>
                    </v-row>
                  </v-container>
                  <v-container v-if="chosenTable === 'Drafts'">
                    <v-row>
                      <v-col>
                        <div>
                          <v-icon large style="text-align: center; display: block">
                            error
                          </v-icon>
                          <v-card-title style="text-align: center; display: block">
                            Редактирование невозможно
                          </v-card-title>
                        </div>
                      </v-col>
                    </v-row>
                  </v-container>
                </v-tab-item>
                <v-tab-item>
                  <v-container v-if="chosenTable === 'Persons'">
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
                  <v-container v-if="chosenTable === 'Machines'">
                    <v-row>
                      <v-card-subtitle>Акции текущей машины:</v-card-subtitle>
                      <div v-if="forms.machines.stock !== null">
                        <v-checkbox v-for="(item, i) in forms.machines.stock" :label="item.name + ' ' + item.sponsor + ' ' + item.lastTerm" :key="i"></v-checkbox>
                      </div>
                      <v-checkbox v-else readonly label="Акции отсутствуют"></v-checkbox>
                    </v-row>
                  </v-container>
                  <v-container v-if="chosenTable === 'Stocks'">
                    <v-row>
                      <v-col>
                        <div>
                          <v-icon large style="text-align: center; display: block">
                            warning
                          </v-icon>
                          <v-card-title style="text-align: center; display: block">
                            Отсутствуют
                          </v-card-title>
                        </div>
                      </v-col>
                    </v-row>
                  </v-container>
                  <v-container v-if="chosenTable === 'Drafts'" style="padding: 4%; overflow: hidden">
                    <v-row>
                      <v-col>
                        <div>
                          <v-icon large style="text-align: center; display: block">
                            warning
                          </v-icon>
                          <v-card-title style="text-align: center; display: block">
                            Отсутствуют
                          </v-card-title>
                        </div>
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
import StockList from "@/components/tables/StockList";
import DraftList from "@/components/tables/DraftList";
const ip = "192.168.0.153"
const port = '9000'
const axios = require('axios')

export default {
  name: "AdminCabinet",
  components: {DraftList, StockList, MachineList, PersonsList},
  data() {
    return {
      chosenDate: new Date().toISOString().substr(0, 10),
      searchTextChosenTable: null,
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
      menuChooseDate: false,
      editForm: {
        machines: [],
        persons: [],
        wallets: [],
        histories: [],
        stocks: []
      },
      rulesNum: [
        v => v > 0 || 'Число должно быть больше 0'
      ],
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
          machine: '',
          wallet: {
            historyTransactions: '',
            balance: ''
          }
        },
        machines: {
          stock: {
            sponsor: '',
            discount: '',
            lastTerm: ''
          },
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
          additional: [],
          paymentType: '',
          date: new Date().toISOString().substr(0, 10),
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
  computed: {
    renderMachineForChoose() {
      return this.editForm.machines.map(i => 'ID: ' + i.id + ' | ' + i.name + ' | ' + i.capacity)
    },
    renderUserForChoose() {
      return this.editForm.persons.map(i => 'ID: ' + i.id + ' | ' + i.fName + ' ' + i.sName)
    }
  },
  methods: {
    reFormateDate() {
      const [year, month, day] = this.chosenDate.split('-')
      this.forms.stocks.lastTerm = `${month}/${day}/${year}`;
      this.menuChooseDate = false;
    },
    doCloseTable() {
      this.fullInfo = false
      this.chosenTable = null
      this.chosenTableInfo = null
    },
    doSearchInTable() {
      let regex = new RegExp(this.searchTextChosenTable, 'i')
      if (this.chosenTable === 'Persons') {
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/persons`
        }).then(resp => {
          this.chosenTableInfo = resp.data.filter(i => regex.test(i.fName + ' ' + i.sName || i.fName || i.sName))
        })
      }
      else if (this.chosenTable === 'Machines') {
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/machines`
        }).then(resp => {
          this.chosenTableInfo = resp.data.filter(i => regex.test(i.name))
        })
      }
      else if (this.chosenTable === 'Drafts') {
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/drafts`
        }).then(resp => {
          this.chosenTableInfo = resp.data.filter(i => regex.test(i.paymentType))
        })
      }
      else if (this.chosenTable === 'Wallets') {
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/wallets`
        }).then(resp => {
          this.chosenTableInfo = resp.data.filter(i => regex.test(i.balance))
        })
      }
      else if (this.chosenTable === 'histories') {
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/persons`
        }).then(resp => {
          this.chosenTableInfo = resp.data.filter(i => regex.test(i.sum || i.date))
        })
      }
      else if (this.chosenTable === 'Stocks') {
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/stocks`
        }).then(resp => {
          this.chosenTableInfo = resp.data.filter(i => regex.test(i.name || i.sponsor || i.discount))
        })
      }
      else {
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/${this.chosenTable.toLowerCase()}`
        }).then(resp => {
          this.chosenTableInfo = resp.data
        })
      }
    },
    updater(info) {
      this.forms[this.chosenTable.toLowerCase()] = info.infoItem
      if (info.showEdit) this.showEditForm = true
      else if (info.showDel) this.showDelForm = true
    },
    loadImg(ev) {
      let reader = new FileReader()
      if (this.chosenTable === 'Persons') {
        reader.onload = (e) => {
          this.forms[this.chosenTable.toLowerCase()].avatar = e.target.result
        }
      } else {
        reader.onload = (e) => {
          this.forms[this.chosenTable.toLowerCase()].img = e.target.result
        }
      }
      reader.readAsDataURL(ev);
    },
    doAdd() {
      try {
        let table = this.chosenTable
        let curForms = this.forms[table.toLowerCase()]
        if (table === 'Drafts') {
          curForms.machine = {id: curForms.machine.split('|')[0].split(': ')[1]}
          curForms.person = {id: curForms.person.split('|')[0].split(': ')[1]}
        }
        if (table === 'Persons' && curForms.machine === '') curForms.machine = null
        if (table === 'Machines' && curForms.stock === '') curForms.stock = null
        console.log('info', curForms)
        axios({
          method: "POST",
          url: `http://${ip}:${port}/api/${table.toLowerCase()}`,
          data: this.forms[table.toLowerCase()]
        }).then(resp => {
          console.log(resp)

          this.$nextTick(() => {
            this.showAddForm = false;
            this.alertSuccess = true;
            this.alertText = 'Успешно добавлена запись';
            this.chosenTableInfo.push(resp.data)
          })

          for (let item of Object.keys(this.forms[table.toLowerCase()])) {
            this.forms[table.toLowerCase()][item] = '';
          }
        }).catch(err => {
          console.log(err)
          this.$nextTick(() => {
            this.alertErr = true;
            this.alertText = 'Ошибка сервера';
          })
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
        }).then(resp => {
          console.log(resp.data.info)
        })

        this.chosenTableInfo = this.chosenTableInfo.filter(i => i.id !== curItem)

        for (let item of Object.keys(this.forms[table.toLowerCase()])) {
          this.forms[table.toLowerCase()][item] = '';
        }

        this.$nextTick(() => {
          this.showDelForm = false;
          this.alertSuccess = true;
          this.alertText = 'Запись успешно удалена'
        })
      } catch (e) {
        console.log('err', e)
        this.$nextTick(() => {
          this.alertErr = true;
        })
      }
    },
    handleTable(ev) {
      try {
        this.fullInfo = true;
        this.chosenTable = ev.path[0].offsetParent.offsetParent.attributes.getNamedItem('name').value || ev.path[0].offsetParent.attributes.getNamedItem('name').value
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/${this.chosenTable.toLowerCase()}`,
        }).then(resp => (this.chosenTableInfo = resp.data))
      }
      catch {
        console.log()
      }
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