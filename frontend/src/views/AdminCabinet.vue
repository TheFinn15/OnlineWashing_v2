<template>
  <v-app style="z-index: 5">
    <v-card style="margin: 5% 15% 0 15%">
      <v-tabs grow color="indigo">
        <v-tab>Администрирование</v-tab>
        <v-tab>Отчеты</v-tab>
        <v-tab>Статистика</v-tab>
        <v-tab-item>
          <v-card-title style="justify-content: center">
            Управление данными
          </v-card-title>
          <v-divider></v-divider>
          <v-container fluid>
            <v-row no-gutters justify="space-between">
              <v-col cols="3" v-for="(item, i) in tablesInfo.tablesName" :key="i">
                <v-card width="280px" style="margin: 2%" :name="item">
                  <v-card-subtitle>{{ item }}</v-card-subtitle>
                  <v-card-text>
                    <b>Кол-во записей:</b> <br/> {{tablesInfo.countsRows[item.toLowerCase()]}} <br/>
                    <b>Дата изменения:</b> <br/> {{tablesInfo.datesEdits[item.toLowerCase()]}}
                  </v-card-text>
                  <v-btn color="indigo" outlined block @click="handleTable">Подробнее</v-btn>
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
              <WalletList
                  :updater="updater"
                  :show-edit="showEditForm"
                  :show-del="showDelForm"
                  :item-info="forms.wallets"
                  :table-info="chosenTableInfo"
                  :mode-del="modeDel"
                  :mode-edit="modeEdit"
                  v-if="chosenTable === 'Wallets'"
                  style="overflow-y: scroll; height: 100%"
              />
              <HistoryList
                  :updater="updater"
                  :show-edit="showEditForm"
                  :show-del="showDelForm"
                  :item-info="forms.histories"
                  :table-info="chosenTableInfo"
                  :mode-del="modeDel"
                  :mode-edit="modeEdit"
                  v-if="chosenTable === 'Histories'"
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
                  <v-container v-if="chosenTable === 'Wallets'">
                    <v-row>
                      <v-col>
                        <v-text-field
                            type="number"
                            label="Цена"
                            outlined
                            :rules="rulesNum"
                            required
                            v-model="forms.wallets.balance"
                        ></v-text-field>
                      </v-col>
                      <v-col>
                        <v-select
                            label="История транзакций недоступна"
                            readonly
                            outlined
                        ></v-select>
                      </v-col>
                    </v-row>
                  </v-container>
                  <v-container v-if="chosenTable === 'Histories'">
                    <v-row>
                      <v-col>
                        <v-text-field
                            type="number"
                            label="Сумма пополнения"
                            outlined
                            :rules="rulesNum"
                            required
                            v-model="forms.histories.sum"
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
                                v-model="forms.histories.date"
                                label="Дата пополнения"
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
                    <v-row>
                      <v-col>
                        <v-select
                            label="Выберите вид оплаты:"
                            :items="histories.paymentsType"
                            outlined
                            v-model="forms.histories.paymentType"
                        ></v-select>
                      </v-col>
                      <v-col v-if="forms.histories.paymentType === 'Кредитной картой'">
                        <v-text-field
                            label="Номер карты:"
                            :rules="rulesNum"
                            outlined
                            v-model="forms.histories.creditCard"
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
                  <v-container v-if="chosenTable === 'Wallets'" style="padding: 4%; overflow: hidden">
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
                  <v-container v-if="chosenTable === 'Histories'" style="padding: 4%; overflow: hidden">
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
                  <v-container v-if="chosenTable === 'Wallets'">
                    <v-row>
                      <v-col>
                        <v-text-field
                            type="number"
                            label="Цена"
                            outlined
                            :rules="rulesNum"
                            :placeholder="editForm.wallets.balance"
                            required
                            v-model="forms.wallets.balance"
                        ></v-text-field>
                      </v-col>
                      <v-col>
                        <v-select
                            label="Историю транзакций недоступна"
                            readonly
                            outlined
                        ></v-select>
                      </v-col>
                    </v-row>
                  </v-container>
                  <v-container v-if="chosenTable === 'Histories'">
                    <v-row>
                      <v-col>
                        <v-text-field
                            type="number"
                            label="Сумма пополнения"
                            outlined
                            :rules="rulesNum"
                            :placeholder="editForm.histories.sum"
                            required
                            v-model="forms.histories.sum"
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
                                v-model="forms.histories.date"
                                :placeholder="editForm.histories.date"
                                label="Дата пополнения"
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
                    <v-row>
                      <v-col>
                        <v-select
                            label="Выберите вид оплаты:"
                            :items="histories.paymentsType"
                            outlined
                            v-model="forms.histories.paymentType"
                            @input="doClearCreditNums"
                            :placeholder="forms.histories.paymentType"
                        ></v-select>
                      </v-col>
                      <v-col v-if="forms.histories.paymentType === 'Кредитной картой'">
                        <v-text-field
                            label="Кредитная карта:"
                            :rules="rulesNum"
                            outlined
                            :placeholder="editForm.histories.creditCard"
                            v-model="forms.histories.creditCard"
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
                  <v-container v-if="chosenTable === 'Wallets'" style="padding: 4%; overflow: hidden">
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
                  <v-container v-if="chosenTable === 'Histories'" style="padding: 4%; overflow: hidden">
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
        <v-tab-item>
          <v-list>
            <v-list-group v-if="reports.monthlyDrafts.length > 0">
              <template v-slot:activator>
                <v-list-item-title>
                  Чеки за этот месяц
                </v-list-item-title>
              </template>
              <v-list-item v-for="(item, i) in reports.monthlyDrafts " :key="i">
                <v-list-group sub-group no-action color="indigo">
                  <template v-slot:activator>
                    <v-list-item-title>
                      ID: {{item.id}} | {{item.price}} UAH | {{item.date}}
                    </v-list-item-title>
                  </template>
                  <v-list-group sub-group no-action color="indigo">
                    <template v-slot:activator>
                      <v-list-item-content>
                        <v-list-item-title>Информация о чеке</v-list-item-title>
                      </v-list-item-content>
                    </template>
                    <v-list-item>
                      <v-list-group sub-group no-action color="indigo">
                        <template v-slot:activator>
                          <v-list-item-title>
                            Пользователь: ID: {{item.person.id}} | {{item.person.fName}} {{item.person.sName}}
                          </v-list-item-title>
                        </template>
                        <v-list-item>
                          <v-list-item-title>
                            Текущий баланс: {{item.person.wallet.balance}} UAH
                          </v-list-item-title>
                        </v-list-item>
                        <v-list-item>
                          <v-list-item-title>
                            Логин: {{item.person.login}}
                          </v-list-item-title>
                        </v-list-item>
                        <v-list-item>
                          <v-list-item-title>
                            E-mail: {{item.person.email}}
                          </v-list-item-title>
                        </v-list-item>
                        <v-list-item>
                          <v-list-item-title>
                            Телефон: {{item.person.phone}}
                          </v-list-item-title>
                        </v-list-item>
                      </v-list-group>
                    </v-list-item>
                    <v-list-item>
                      <v-list-group sub-group no-action color="indigo">
                        <template v-slot:activator>
                          <v-list-item-title>
                            Стиральная машина: ID: {{item.machine.id}} | {{item.machine.name}} | {{item.machine.capacity}}
                          </v-list-item-title>
                        </template>
                        <v-list-item>
                          <v-list-item-title>
                            Цена за 1 кг: {{item.machine.price}}
                          </v-list-item-title>
                        </v-list-item>
                        <v-list-item>
                          <v-list-item-title>
                            Описание: {{item.machine.description}}
                          </v-list-item-title>
                        </v-list-item>
                        <v-list-item>
                          <v-list-item-title>
                            Текущий статус: {{item.machine.status}}
                          </v-list-item-title>
                        </v-list-item>
                      </v-list-group>
                    </v-list-item>
                    <v-list-item>
                      <v-list-item-title>Уплаченная цена: {{item.price}} UAH</v-list-item-title>
                    </v-list-item>
                    <v-list-item>
                      <v-list-item-title>Объем вещей: {{item.volume}} кг</v-list-item-title>
                    </v-list-item>
                    <v-list-item>
                      <v-list-item-title>
                        Вид оплаты: {{item.paymentType}}
                      </v-list-item-title>
                      <v-list-item-subtitle v-if="item.paymentType === 'Картой'">
                        Кредитная карта: {{item.creditCard}}
                      </v-list-item-subtitle>
                    </v-list-item>
                    <v-list-item>
                      <v-list-group sub-group no-action color="indigo">
                        <template v-slot:activator>
                          <v-list-item-title>
                            Хим. добавки к стирке
                          </v-list-item-title>
                        </template>
                        <v-list-item v-for="(item, i) in item.additional" :key="i">
                          <v-list-item-title>
                            {{item}}
                          </v-list-item-title>
                        </v-list-item>
                      </v-list-group>
                    </v-list-item>
                    <v-list-item>
                      <v-list-item-title>Дата обновления записи: {{item.lastUpdateRow}}</v-list-item-title>
                    </v-list-item>
                  </v-list-group>
                </v-list-group>
              </v-list-item>
            </v-list-group>
            <v-list-group append-icon="warning" no-action disabled v-else>
              <template v-slot:activator>
                <v-list-item-title>
                  Не рабочие машины
                  <v-card-subtitle>
                    Отсутствуют
                  </v-card-subtitle>
                </v-list-item-title>
              </template>
            </v-list-group>
            <v-list-group v-if="reports.babMachines.length > 0">
              <template v-slot:activator>
                <v-list-item-title>
                  Не рабочие машины
                </v-list-item-title>
              </template>
              <v-list-item v-for="(item, i) in reports.babMachines" :key="i">
                <v-list-group sub-group no-action color="indigo">
                  <template slot="activator">
                    <v-list-item-title>
                      ID: {{item.id}} | {{item.name}} {{item.capacity}}
                    </v-list-item-title>
                  </template>
                  <v-list-item>
                    <v-list-item-title>Описание: {{item.description}}</v-list-item-title>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-title>Статус машини: {{item.status}}</v-list-item-title>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-title>Цена за 1 кг: {{item.price}}</v-list-item-title>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-title>Дата обновления записи: {{item.lastUpdateRow}}</v-list-item-title>
                  </v-list-item>
                </v-list-group>
              </v-list-item>
            </v-list-group>
            <v-list-group append-icon="warning" no-action disabled v-else>
              <template v-slot:activator>
                <v-list-item-title>
                  Не рабочие машины
                  <v-card-subtitle>
                    Отсутствуют
                  </v-card-subtitle>
                </v-list-item-title>
              </template>
            </v-list-group>
          </v-list>
        </v-tab-item>
        <v-tab-item>
          <v-card flat>
            <v-container>
              <v-row no-gutters justify="center">
                <v-col cols="6">
                  <v-select
                      label="Статистика по:"
                      append-icon="trending_up"
                      v-model="statistic.type"
                      :items="statistic.types"
                      outlined
                  ></v-select>
                </v-col>
              </v-row>
            </v-container>
            <v-divider></v-divider>
            <div v-if="statistic.type === 'Заказам'">
              <v-menu offset-x>
                <template v-slot:activator="{on, attrs}">
                  <v-btn icon v-bind="attrs" v-on="on">
                    <v-icon>
                      info
                    </v-icon>
                  </v-btn>
                </template>
                <v-card>
                  <v-card-text>
                    <span class="red--text">
                      Красный
                    </span> - очень слабое кол-во заказов <br>
                      <span class="primary--text">
                      Синий
                    </span> - слабое кол-во заказов <br>
                      <span class="indigo--text">
                      Темно-синий
                    </span> - среднее кол-во заказов <br>
                      <span class="success--text">
                      Зеленный
                    </span> - хорошее кол-во заказов
                  </v-card-text>
                </v-card>
              </v-menu>
              <v-card v-for="(item, i) in calcPercent" :key="i" :color="item.color" style="margin: 5%">
                <v-card-title>
                  {{item.name}}
                  <v-menu offset-x transition="scale-transition">
                    <template v-slot:activator="{on, attrs}">
                      <v-btn icon v-bind="attrs" v-on="on">
                        <v-icon>
                          keyboard_arrow_right
                        </v-icon>
                      </v-btn>
                    </template>
                    <v-card>
                      <v-card-text>
                        <b>Цена:</b> {{item.price}} ГРН.<br>
                        <b>Описание:</b> {{item.description}} <br>
                        <b>Вместимость:</b> {{item.capacity}} л.
                      </v-card-text>
                    </v-card>
                  </v-menu>
                  <v-spacer></v-spacer>
                  <v-card-subtitle>
                    Кол-во заказаов: {{item.count}}
                  </v-card-subtitle>
                </v-card-title>
              </v-card>
            </div>
          </v-card>
        </v-tab-item>
      </v-tabs>
    </v-card>
  </v-app>
</template>

<script>
import PersonsList from "@/components/tables/PersonsList";
import MachineList from "@/components/tables/MachineList";
import StockList from "@/components/tables/StockList";
import DraftList from "@/components/tables/DraftList";
import WalletList from "@/components/tables/WalletList";
import HistoryList from "@/components/tables/HistoryList";
const ip =  "localhost"
const port = '9000'
const axios = require('axios')

export default {
  name: "AdminCabinet",
  components: {HistoryList, WalletList, DraftList, StockList, MachineList, PersonsList},
  data() {
    return {
      chosenDate: new Date().toISOString().substr(0, 10),
      reports: {
        babMachines: [],
        monthlyDrafts: []
      },
      activeBtnTable: 'default',
      prevBtnTable: false,
      nextBtnTable: true,
      searchTextChosenTable: null,
      tablesInfo: {
        tablesName: [
          'Persons', 'Machines', 'Stocks', 'Drafts', 'Wallets', 'Histories'
        ],
        countsRows: {
          persons: '',
          machines: '',
          stocks: '',
          drafts: '',
          wallets: '',
          histories: ''
        },
        datesEdits: {
          persons: '',
          machines: '',
          stocks: '',
          drafts: '',
          wallets: '',
          histories: ''
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
        stocks: [],
        drafts: []
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
      histories: {
        paymentsType: ['Наличкой', 'Кредитной картой'],
      },
      statistic: {
        types: [
          'Заказам'
        ],
        type: 'Заказам'
      },
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
            Histories: '',
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
          paymentType: '',
          creditCard: '',
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
    calcPercent() {
      let res = []
      let unique = []
      let count = 0
      for (let i=0;i<this.editForm.drafts.length;i++) {
        let curItem = this.editForm.drafts[i].machine
        if (unique.indexOf(curItem.name) < 0) {
          for (let j=0;j<this.editForm.drafts.length;j++) {
            let item = this.editForm.drafts[i].machine
            if (curItem.name === item.name) {
              count++;
            }
          }
          res.push({
            name: curItem.name,
            count: count,
            price: curItem.price,
            capacity: curItem.capacity,
            description: curItem.description,
            color: count >= 25?'success': count >= 15?'indigo':count >= 10?'primary':'red'
          })
          unique.push(curItem.name)
          count = 0
        }
      }
      return res;
    },
    renderMachineForChoose() {
      return this.editForm.machines.map(i => 'ID: ' + i.id + ' | ' + i.name + ' | ' + i.capacity)
    },
    renderUserForChoose() {
      return this.editForm.persons.map(i => 'ID: ' + i.id + ' | ' + i.fName + ' ' + i.sName)
    }
  },
  methods: {
    doClearCreditNums() {
      this.forms.histories.creditCard = '';
    },
    reFormateDate() {
      const [year, month, day] = this.chosenDate.split('-')
      if (this.chosenTable === 'Stocks') this.forms.stocks.lastTerm = `${month}/${day}/${year}`;
      if (this.chosenTable === 'Histories') this.forms.histories.date = `${month}/${day}/${year}`
      this.menuChooseDate = false;
    },
    doCloseTable() {
      this.fullInfo = false
      this.chosenTable = null
      this.chosenTableInfo = null
    },
    doSearchInTable() {
      if (this.chosenTable === 'Persons') {
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/persons`
        }).then(resp => {
          this.chosenTableInfo = resp.data.filter(i => i.id.toString().includes(this.searchTextChosenTable) || (i.fName + ' ' + i.sName).includes(this.searchTextChosenTable) || i.fName.includes(this.searchTextChosenTable) || i.sName.includes(this.searchTextChosenTable))
        })
      }
      else if (this.chosenTable === 'Machines') {
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/machines`
        }).then(resp => {
          this.chosenTableInfo = resp.data.filter(i => i.id.toString().includes(this.searchTextChosenTable) || i.name.includes(this.searchTextChosenTable) || i.status.includes(this.searchTextChosenTable) || i.capacity.toString().includes(this.searchTextChosenTable) || i.price.toString().includes(this.searchTextChosenTable))
        })
      }
      else if (this.chosenTable === 'Drafts') {
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/drafts`
        }).then(resp => {
          this.chosenTableInfo = resp.data.filter(i => i.id.toString().includes(this.searchTextChosenTable) || i.paymentType.includes(this.searchTextChosenTable) || i.machine.name.includes(this.searchTextChosenTable) || i.person.fName.includes(this.searchTextChosenTable) || i.person.sName.includes(this.searchTextChosenTable) || i.person.fName.includes(this.searchTextChosenTable) + ' ' + i.person.sName.includes(this.searchTextChosenTable))
        })
      }
      else if (this.chosenTable === 'Wallets') {
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/wallets`
        }).then(resp => {
          this.chosenTableInfo = resp.data.filter(i => i.id.toString().includes(this.searchTextChosenTable) || i.balance.includes(this.searchTextChosenTable))
        })
      }
      else if (this.chosenTable === 'Histories') {
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/histories`
        }).then(resp => {
          this.chosenTableInfo = resp.data.filter(i => i.id.toString().includes(this.searchTextChosenTable) || i.sum.toString().includes(this.searchTextChosenTable) || i.date.includes(this.searchTextChosenTable) || i.paymentType.includes(this.searchTextChosenTable))
        })
      }
      else if (this.chosenTable === 'Stocks') {
        axios({
          method: 'GET',
          url: `http://${ip}:${port}/api/stocks`
        }).then(resp => {
          this.chosenTableInfo = resp.data.filter(i => i.id.toString().includes(this.searchTextChosenTable) || i.name.includes(this.searchTextChosenTable) || i.sponsor.includes(this.searchTextChosenTable) || i.discount.toString().includes(this.searchTextChosenTable))
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
          this.chosenTableInfo = this.chosenTableInfo.filter(i => {
            if (i.id !== resp.data.id && i.id !== '') {
              return i;
            }
          })
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
            this.editForm.drafts = resp.data
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
            this.tablesInfo.datesEdits.histories = resp.data[resp.data.length-1].lastUpdateRow
          } else {
            this.tablesInfo.countsRows.histories = 0
            this.tablesInfo.datesEdits.histories = 'Отсутствует'
          }
        })
    axios.get(`http://${ip}:${port}/api/drafts/reports/monthly-checks`)
        .then(resp => {
          this.reports.monthlyDrafts = resp.data
        })
    axios.get(`http://${ip}:${port}/api/machines/reports/bad-status-machines`)
        .then(resp => {
          this.reports.babMachines = resp.data
        })
  }
}
</script>

<style scoped>

</style>