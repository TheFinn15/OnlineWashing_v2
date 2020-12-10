<template>
  <v-app style="z-index: 5">
    <v-card flat>
      <v-toolbar style="justify-content: center; display: flex">
        <v-menu v-model="translate" offset-y>
          <template v-slot:activator="{on, attrs}">
            <v-btn v-on="on" v-bind="attrs" icon :title="curLocale.locales.tip">
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
                  <v-list-item-content>
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
        <v-toolbar-title>ADMIN PANEL</v-toolbar-title>
        <v-btn icon @click="logout">
          <v-icon>
            logout
          </v-icon>
        </v-btn>
      </v-toolbar>
      <v-card style="margin: 5% 15% 0 15%">
        <v-tabs grow color="indigo">
          <v-tab>
            {{curLocale.tabs.tab1.name}}
          </v-tab>
          <v-tab>
            {{curLocale.tabs.tab2.name}}
          </v-tab>
          <v-tab>
            {{curLocale.tabs.tab3.name}}
          </v-tab>
          <v-tab-item>
            <v-card-title style="justify-content: center">
              {{curLocale.tabs.tab1.context.title}}
            </v-card-title>
            <v-divider></v-divider>
            <v-container fluid>
              <v-row no-gutters justify="space-between">
                <v-col cols="3" v-for="(item, i) in tablesInfo.tablesName" :key="i">
                  <v-card width="280px" style="margin: 2%" :name="item">
                    <v-card-subtitle>{{ item }}</v-card-subtitle>
                    <v-card-text>
                      <b>{{curLocale.tabs.tab1.context.table.tableInfo[0]}}</b> <br/> {{tablesInfo.countsRows[item.toLowerCase()]}} <br/>
                      <b>{{curLocale.tabs.tab1.context.table.tableInfo[1]}}</b> <br/> {{tablesInfo.datesEdits[item.toLowerCase()]}}
                    </v-card-text>
                    <v-btn color="indigo" outlined block @click="handleTable">
                      {{curLocale.tabs.tab1.context.table.btnTitle}}
                    </v-btn>
                  </v-card>
                </v-col>
              </v-row>
            </v-container>
            <v-dialog scrollable max-width="650" persistent v-model="fullInfo" v-if="chosenTable !== null">
              <v-card>
                <v-card-title>
                  {{curLocale.tabs.tab1.context.fullInfoTable.title}} {{ chosenTable }}
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
                    :label="curLocale.tabs.tab1.context.fullInfoTable.title + ' ' + chosenTable"
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
                      <span v-if="hover">
                        {{curLocale.tabs.tab1.context.fullInfoTable.crudBtns[0]}}
                      </span>
                      <v-icon>
                        add
                      </v-icon>
                    </v-btn>
                  </v-hover>
                  <v-hover v-slot:default="{hover}">
                    <v-btn outlined elevation="3" color="indigo" @click="modeEdit = !modeEdit">
                      <span v-if="hover">
                        {{curLocale.tabs.tab1.context.fullInfoTable.crudBtns[1]}}
                      </span>
                      <v-icon>
                        edit
                      </v-icon>
                    </v-btn>
                  </v-hover>
                  <v-hover v-slot:default="{hover}">
                    <v-btn outlined elevation="3" color="indigo" @click="modeDel = !modeDel">
                      <span v-if="hover">
                        {{curLocale.tabs.tab1.context.fullInfoTable.crudBtns[2]}}
                      </span>
                      <v-icon>
                        delete
                      </v-icon>
                    </v-btn>
                  </v-hover>
                </v-card>
                <v-divider></v-divider>
                <PersonsList
                    :updater="updater"
                    :locales="curLocale"
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
                    :locales="curLocale"
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
                    :locales="curLocale"
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
                    :locales="curLocale"
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
                    :locales="curLocale"
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
                    :locales="curLocale"
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
                  {{curLocale.tabs.tab1.context.fullInfoTable.addForm.title}} {{chosenTable}}
                  <v-spacer></v-spacer>
                  <v-btn icon @click="showAddForm = false">
                    <v-icon>
                      close
                    </v-icon>
                  </v-btn>
                </v-card-title>
                <v-divider></v-divider>
                <v-tabs grow color="indigo">
                  <v-tab>
                    {{curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.name}}
                  </v-tab>
                  <v-tab>
                    {{curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab2.name}}
                  </v-tab>
                  <v-tab-item>
                    <v-container v-if="chosenTable === 'Persons'">
                      <v-row>
                        <v-col>
                          <v-row>
                            <v-col>
                              <v-text-field
                                  :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.person.labels[0]"
                                  outlined
                                  :rules="rulesText"
                                  required
                                  v-model="forms.persons.fName"
                              ></v-text-field>
                              <v-text-field
                                  :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.person.labels[1]"
                                  outlined
                                  :rules="rulesText"
                                  required
                                  v-model="forms.persons.sName"
                              ></v-text-field>
                              <v-text-field
                                  :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.person.labels[2]"
                                  outlined
                                  :rules="rulesText"
                                  required
                                  v-model="forms.persons.login"
                              ></v-text-field>
                            </v-col>
                            <v-col>
                              <v-text-field
                                  type="password"
                                  :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.person.labels[3]"
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
                                  :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.person.labels[5]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.machine.labels[0]"
                              outlined
                              :rules="rulesText"
                              required
                              v-model="forms.machines.name"
                          ></v-text-field>
                        </v-col>
                        <v-col>
                          <v-text-field
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.machine.labels[1]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.machine.labels[2]"
                              type="number"
                              outlined
                              :rules="rulesNum"
                              required
                              v-model="forms.machines.capacity"
                          ></v-text-field>
                        </v-col>
                        <v-col>
                          <v-text-field
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.machine.labels[3]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.machine.labels[4]"
                              outlined
                              :items="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.machine.labels[5]"
                              v-model="forms.machines.status"
                          ></v-select>
                        </v-col>
                      </v-row>
                    </v-container>
                    <v-container v-if="chosenTable === 'Stocks'">
                      <v-row>
                        <v-col>
                          <v-text-field
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.stock.labels[0]"
                              outlined
                              :rules="rulesText"
                              required
                              v-model="forms.stocks.name"
                          ></v-text-field>
                        </v-col>
                        <v-col>
                          <v-text-field
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.stock.labels[1]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.stock.labels[2]"
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
                                  :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.stock.labels[3]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.draft.labels[0]"
                              outlined
                              :rules="rulesNum"
                              required
                              v-model="forms.drafts.volume"
                          ></v-text-field>
                        </v-col>
                        <v-col>
                          <v-text-field
                              type="number"
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.draft.labels[1]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.draft.labels[2]"
                              outlined
                              :items="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.draft.labels[3]"
                              required
                              v-model="forms.drafts.paymentType"
                          ></v-select>
                        </v-col>
                        <v-col v-if="forms.drafts.paymentType === curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.draft.labels[3][1]">
                          <v-text-field
                              type="number"
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.draft.labels[3][1]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.draft.labels[4]"
                              outlined
                              :items="renderUserForChoose"
                              required
                              v-model="forms.drafts.person"
                          ></v-select>
                        </v-col>
                        <v-col>
                          <v-select
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.draft.labels[5]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.draft.labels[6]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.draft.labels[7]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.wallet.labels[0]"
                              outlined
                              :rules="rulesNum"
                              required
                              v-model="forms.wallets.balance"
                          ></v-text-field>
                        </v-col>
                        <v-col>
                          <v-select
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.wallet.labels[1]"
                              :items="getHistories"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.histories.labels[0]"
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
                                  :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.histories.labels[1]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.histories.labels[2]"
                              :items="histories.paymentsType"
                              outlined
                              v-model="forms.histories.paymentType"
                          ></v-select>
                        </v-col>
                        <v-col v-if="forms.histories.paymentType === curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.histories.labels[3][1]">
                          <v-text-field
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.histories.labels[3]"
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
                          <v-card-subtitle>
                            {{curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab2.context.person.labels[0]}}
                          </v-card-subtitle>
                          <div v-if="editForm.machines.length > 0 ">
                            <v-checkbox v-for="(item, i) in editForm.machines" :label="item.name + ' ' + item.capacity" :key="i"></v-checkbox>
                          </div>
                          <v-checkbox v-else readonly :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab2.context.person.labels[1]"></v-checkbox>
                        </v-col>
                      </v-row>
                    </v-container>
                    <v-container v-if="chosenTable === 'Machines'">
                      <v-row>
                        <v-card-subtitle>
                          {{curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab2.context.machine.labels[0]}}
                        </v-card-subtitle>
                        <div v-if="editForm.stocks.length > 0 ">
                          <v-checkbox v-for="(item, i) in editForm.stocks" :label="item.name + ' ' + item.sponsor + ' ' + item.lastTerm" :key="i"></v-checkbox>
                        </div>
                        <v-checkbox v-else readonly :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab2.context.machine.labels[1]"></v-checkbox>
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
                              {{curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab2.context.stock.labels[0]}}
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
                              {{curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab2.context.histories.labels[0]}}
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
                              {{curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab2.context.wallet.labels[0]}}
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
                              {{curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab2.context.histories.labels[0]}}
                            </v-card-title>
                          </div>
                        </v-col>
                      </v-row>
                    </v-container>
                  </v-tab-item>
                </v-tabs>
                <v-divider></v-divider>
                <v-btn @click="doAdd" outlined color="indigo" width="100%">
                  {{curLocale.tabs.tab1.context.fullInfoTable.addForm.btnTitle}}
                </v-btn>
              </v-card>
            </v-dialog>
            <v-dialog max-width="1000" persistent v-model="showEditForm">
              <v-card>
                <v-card-title>
                  {{curLocale.tabs.tab1.context.fullInfoTable.editForm.title}} {{chosenTable}}
                  <v-spacer></v-spacer>
                  <v-btn icon @click="showEditForm = false">
                    <v-icon>
                      close
                    </v-icon>
                  </v-btn>
                </v-card-title>
                <v-divider></v-divider>
                <v-tabs grow color="indigo">
                  <v-tab>
                    {{curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.name}}
                  </v-tab>
                  <v-tab>
                    {{curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab2.name}}
                  </v-tab>
                  <v-tab-item>
                    <v-container v-if="chosenTable === 'Persons'">
                      <v-row>
                        <v-col>
                          <v-img width="200" height="185" :src="forms.persons.avatar"></v-img>
                          <v-file-input
                              full-width
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
                                  :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.person.labels[0]"
                                  :placeholder="editForm.persons.fName"
                                  outlined
                                  :rules="rulesText"
                                  v-model="forms.persons.fName"
                              ></v-text-field>
                              <v-text-field
                                  :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.person.labels[1]"
                                  :placeholder="editForm.persons.sName"
                                  outlined
                                  :rules="rulesText"
                                  v-model="forms.persons.sName"
                              ></v-text-field>
                              <v-text-field
                                  :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.person.labels[2]"
                                  :placeholder="editForm.persons.login"
                                  outlined
                                  :rules="rulesText"
                                  v-model="forms.persons.login"
                              ></v-text-field>
                            </v-col>
                            <v-col>
                              <v-text-field
                                  type="password"
                                  :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.person.labels[3]"
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
                                  :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.person.labels[5]"
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
                        <v-col cols="5">
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.machine.labels[0]"
                              outlined
                              :rules="rulesText"
                              required
                              :placeholder="editForm.machines.name"
                              v-model="forms.machines.name"
                          ></v-text-field>
                        </v-col>
                        <v-col>
                          <v-text-field
                              :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.machine.labels[1]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.machine.labels[2]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.machine.labels[3]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.machine.labels[4]"
                              outlined
                              :items="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.machine.labels[5]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.stock.labels[0]"
                              outlined
                              :rules="rulesText"
                              :placeholder="editForm.stocks.name"
                              required
                              v-model="forms.stocks.name"
                          ></v-text-field>
                        </v-col>
                        <v-col>
                          <v-text-field
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.stock.labels[1]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.addForm.tabs.tab1.context.stock.labels[2]"
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
                            {{curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.stock.labels[3]}}
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
                              {{curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.draft.labels[0]}}
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.wallet.labels[0]"
                              outlined
                              :rules="rulesNum"
                              :placeholder="editForm.wallets.balance"
                              required
                              v-model="forms.wallets.balance"
                          ></v-text-field>
                        </v-col>
                        <v-col>
                          <v-select
                              :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.wallet.labels[1]"
                              :items="getHistories"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.histories.labels[0]"
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
                                  :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.histories.labels[1]"
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
                              :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.histories.labels[2]"
                              :items="histories.paymentsType"
                              outlined
                              v-model="forms.histories.paymentType"
                              @input="doClearCreditNums"
                              :placeholder="forms.histories.paymentType"
                          ></v-select>
                        </v-col>
                        <v-col v-if="forms.histories.paymentType === curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.histories.labels[3][1]">
                          <v-text-field
                              :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab1.context.histories.labels[3]"
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
                          <v-card-subtitle>
                            {{curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab2.context.person.labels[0]}}
                          </v-card-subtitle>
                          <div v-if="forms.persons.machine.length > 0 ">
                            <v-checkbox v-for="(item, i) in forms.persons.machine" :label="item.name + ' ' + item.capacity" :key="i"></v-checkbox>
                          </div>
                          <v-checkbox readonly :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab2.context.person.labels[1]"></v-checkbox>
                        </v-col>
                      </v-row>
                    </v-container>
                    <v-container v-if="chosenTable === 'Machines'">
                      <v-row>
                        <v-card-subtitle>
                          {{curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab2.context.machine.labels[0]}}
                        </v-card-subtitle>
                        <div v-if="forms.machines.stock !== null">
                          <v-checkbox v-for="(item, i) in forms.machines.stock" :label="item.name + ' ' + item.sponsor + ' ' + item.lastTerm" :key="i"></v-checkbox>
                        </div>
                        <v-checkbox v-else readonly :label="curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab2.context.machine.labels[1]"></v-checkbox>
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
                              {{curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab2.context.stock.labels[0]}}
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
                              {{curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab2.context.draft.labels[0]}}
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
                              {{curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab2.context.wallet.labels[0]}}
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
                              {{curLocale.tabs.tab1.context.fullInfoTable.editForm.tabs.tab2.context.histories.labels[0]}}
                            </v-card-title>
                          </div>
                        </v-col>
                      </v-row>
                    </v-container>
                  </v-tab-item>
                </v-tabs>
                <v-divider></v-divider>
                <v-btn @click="doEdit" outlined color="indigo" width="100%">
                  {{curLocale.tabs.tab1.context.fullInfoTable.editForm.btnTitle}}
                </v-btn>
              </v-card>
            </v-dialog>
            <v-dialog max-width="400" persistent v-model="showDelForm">
              <v-card>
                <v-card-title>
                  {{curLocale.tabs.tab1.context.fullInfoTable.delForm.title[0]}} {{chosenTable}}. <br/> {{curLocale.tabs.tab1.context.fullInfoTable.delForm.title[1]}}
                </v-card-title>
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn @click="showDelForm = false" text color="green">
                    {{curLocale.tabs.tab1.context.fullInfoTable.delForm.btns[0]}}
                  </v-btn>
                  <v-btn @click="doRemove" text color="red">
                    {{curLocale.tabs.tab1.context.fullInfoTable.delForm.btns[1]}}
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-tab-item>
          <v-tab-item>
            <v-list>
              <v-list-group v-if="reports.monthlyDrafts.length > 0">
                <template v-slot:activator>
                  <v-list-item-title>
                    {{curLocale.tabs.tab2.context.monthlyDrafts.title}}
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
                          <v-list-item-title>
                            {{curLocale.tabs.tab2.context.monthlyDrafts.subtitle}}
                          </v-list-item-title>
                        </v-list-item-content>
                      </template>
                      <v-list-item>
                        <v-list-group sub-group no-action color="indigo">
                          <template v-slot:activator>
                            <v-list-item-title>
                              {{curLocale.tabs.tab2.context.monthlyDrafts.person[0]}} ID: {{item.person.id}} | {{item.person.fName}} {{item.person.sName}}
                            </v-list-item-title>
                          </template>
                          <v-list-item>
                            <v-list-item-title>
                              {{curLocale.tabs.tab2.context.monthlyDrafts.person[1]}} {{item.person.wallet.balance}} UAH
                            </v-list-item-title>
                          </v-list-item>
                          <v-list-item>
                            <v-list-item-title>
                              {{curLocale.tabs.tab2.context.monthlyDrafts.person[2]}} {{item.person.login}}
                            </v-list-item-title>
                          </v-list-item>
                          <v-list-item>
                            <v-list-item-title>
                              E-mail: {{item.person.email}}
                            </v-list-item-title>
                          </v-list-item>
                          <v-list-item>
                            <v-list-item-title>
                              {{curLocale.tabs.tab2.context.monthlyDrafts.person[4]}} {{item.person.phone}}
                            </v-list-item-title>
                          </v-list-item>
                        </v-list-group>
                      </v-list-item>
                      <v-list-item>
                        <v-list-group sub-group no-action color="indigo">
                          <template v-slot:activator>
                            <v-list-item-title>
                              {{curLocale.tabs.tab2.context.monthlyDrafts.machine[0]}} ID: {{item.machine.id}} | {{item.machine.name}} | {{item.machine.capacity}}
                            </v-list-item-title>
                          </template>
                          <v-list-item>
                            <v-list-item-title>
                              {{curLocale.tabs.tab2.context.monthlyDrafts.machine[1]}} {{item.machine.price}}
                            </v-list-item-title>
                          </v-list-item>
                          <v-list-item>
                            <v-list-item-title>
                              {{curLocale.tabs.tab2.context.monthlyDrafts.machine[2]}} {{item.machine.description}}
                            </v-list-item-title>
                          </v-list-item>
                          <v-list-item>
                            <v-list-item-title>
                              {{curLocale.tabs.tab2.context.monthlyDrafts.machine[3]}} {{item.machine.status}}
                            </v-list-item-title>
                          </v-list-item>
                        </v-list-group>
                      </v-list-item>
                      <v-list-item>
                        <v-list-item-title>{{curLocale.tabs.tab2.context.monthlyDrafts.labels[0]}} {{item.price}} UAH</v-list-item-title>
                      </v-list-item>
                      <v-list-item>
                        <v-list-item-title>{{curLocale.tabs.tab2.context.monthlyDrafts.labels[1]}} {{item.volume}} кг</v-list-item-title>
                      </v-list-item>
                      <v-list-item>
                        <v-list-item-title>
                          {{curLocale.tabs.tab2.context.monthlyDrafts.labels[2]}} {{item.paymentType}}
                        </v-list-item-title>
                        <v-list-item-subtitle v-if="item.paymentType === curLocale.tabs.tab2.context.monthlyDrafts.labels[3]">
                          {{curLocale.tabs.tab2.context.monthlyDrafts.labels[3]}} {{item.creditCard}}
                        </v-list-item-subtitle>
                      </v-list-item>
                      <v-list-item>
                        <v-list-group sub-group no-action color="indigo">
                          <template v-slot:activator>
                            <v-list-item-title>
                              {{curLocale.tabs.tab2.context.monthlyDrafts.labels[4]}}
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
                        <v-list-item-title>{{curLocale.tabs.tab2.context.monthlyDrafts.labels[5]}} {{item.lastUpdateRow}}</v-list-item-title>
                      </v-list-item>
                    </v-list-group>
                  </v-list-group>
                </v-list-item>
              </v-list-group>
              <v-list-group append-icon="warning" no-action disabled v-else>
                <template v-slot:activator>
                  <v-list-item-title>
                    {{curLocale.tabs.tab2.context.monthlyDrafts.title}}
                    <v-card-subtitle>
                      {{curLocale.tabs.tab2.context.monthlyDrafts.notFound}}
                    </v-card-subtitle>
                  </v-list-item-title>
                </template>
              </v-list-group>
              <v-list-group v-if="reports.babMachines.length > 0">
                <template v-slot:activator>
                  <v-list-item-title>
                    {{curLocale.tabs.tab2.context.babMachines.title}}
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
                      <v-list-item-title>{{curLocale.tabs.tab2.context.babMachines.labels[0]}} {{item.description}}</v-list-item-title>
                    </v-list-item>
                    <v-list-item>
                      <v-list-item-title>{{curLocale.tabs.tab2.context.babMachines.labels[1]}} {{item.status}}</v-list-item-title>
                    </v-list-item>
                    <v-list-item>
                      <v-list-item-title>{{curLocale.tabs.tab2.context.babMachines.labels[2]}} {{item.price}}</v-list-item-title>
                    </v-list-item>
                    <v-list-item>
                      <v-list-item-title>{{curLocale.tabs.tab2.context.babMachines.labels[3]}} {{item.lastUpdateRow}}</v-list-item-title>
                    </v-list-item>
                  </v-list-group>
                </v-list-item>
              </v-list-group>
              <v-list-group append-icon="warning" no-action disabled v-else>
                <template v-slot:activator>
                  <v-list-item-title>
                    {{curLocale.tabs.tab2.context.babMachines.title}}
                    <v-card-subtitle>
                      {{curLocale.tabs.tab2.context.babMachines.notFound}}
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
                        :label="curLocale.tabs.tab3.context.select"
                        append-icon="trending_up"
                        v-model="statistic.type"
                        :items="statistic.types"
                        outlined
                    ></v-select>
                  </v-col>
                </v-row>
              </v-container>
              <v-divider></v-divider>
              <div v-if="statistic.type === curLocale.tabs.tab3.context.stats[0]">
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
                      {{curLocale.tabs.tab3.context.description.color[0]}}
                    </span> - {{curLocale.tabs.tab3.context.description.text[0]}} <br>
                      <span class="primary--text">
                      {{curLocale.tabs.tab3.context.description.color[1]}}
                    </span> - {{curLocale.tabs.tab3.context.description.text[1]}} <br>
                      <span class="indigo--text">
                      {{curLocale.tabs.tab3.context.description.color[2]}}
                    </span> - {{curLocale.tabs.tab3.context.description.text[2]}} <br>
                      <span class="success--text">
                      {{curLocale.tabs.tab3.context.description.color[3]}}
                    </span> - {{curLocale.tabs.tab3.context.description.text[3]}}
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
                          <b>{{curLocale.tabs.tab3.context.labels[0]}}</b> {{item.price}} UAH.<br>
                          <b>{{curLocale.tabs.tab3.context.labels[1]}}</b> {{item.description}} <br>
                          <b>{{curLocale.tabs.tab3.context.labels[2]}}</b> {{item.capacity}} {{curLocale.tabs.tab3.context.labels[3]}}
                        </v-card-text>
                      </v-card>
                    </v-menu>
                    <v-spacer></v-spacer>
                    <v-card-subtitle>
                      {{curLocale.tabs.tab3.context.labels[4]}} {{item.count}}
                    </v-card-subtitle>
                  </v-card-title>
                </v-card>
              </div>
            </v-card>
          </v-tab-item>
        </v-tabs>
      </v-card>
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
const port = '25016'
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
      translate: false,
      locales: {
        'en-EN': {
          locales: {
            tip: 'Choose language of interface',
            langSubTitle: 'Language of interface:',
            selects: [
                'English',
                'Russian',
                'Ukrainian'
            ]
          },
          tabs: {
            tab1: {
              name: 'Administration',
              context: {
                title: 'Manage data',
                table: {
                  tableInfo: [
                    'Count of records:',
                    'Date of change:'
                  ],
                  btnTitle: 'MORE'
                },
                fullInfoTable: {
                  title: 'Manage of ',
                  search: 'Input a query for ',
                  alerts: [
                      'Record successfully added',
                      'Record successfully updated',
                      'Record successfully deleted',
                      'Server error:'
                  ],
                  crudBtns: [
                      'Add',
                      'Change',
                      'Remove'
                  ],
                  person: {
                    user: {
                      title: 'Information about user',
                      labels: [
                          'Login:',
                          'Password:',
                          'E-mail:',
                          'Phone:',
                          'Date update record:'
                      ]
                    },
                    wallet: {
                      title: 'Wallet',
                      history: 'History transactions',
                      labels: [
                          'Sum of transaction:',
                          'Date of transaction:',
                          'Date update record:'
                      ],
                      notFound: 'Transactions not found'
                    },
                    machine: {
                      title: 'Information of washing machines',
                      labels: [
                          'Capacity:',
                          'Description:',
                          'Price:',
                          'Date update record:'
                      ],
                      notFound: 'Washing machines not in use'
                    }
                  },
                  machine: {
                    machine: {
                      title: 'Information about washing machine',
                      labels: [
                          'Process washing:',
                          'Description:',
                          'Status:',
                          'Price:',
                          'Date update record:'
                      ]
                    },
                    stock: {
                      title: 'Information about stocks in machines',
                      labels: [
                          'Sponsor:',
                          'Sum of stock:',
                          'Date update record:'
                      ],
                      notFound: 'Machine haven`t stock'
                    }
                  },
                  stock: {
                    title: 'Information about stock',
                    labels:[
                        'Name of stock:',
                        'Sponsor:',
                        'Discount:',
                        'Expired date:',
                        'Date update record:'
                    ]
                  },
                  draft: {
                    title: 'Information about draft',
                    person: [
                        'User:',
                        'Current balance:',
                        'Login:',
                        'E-mail:',
                        'Phone:'
                    ],
                    machine: [
                        'Machine: ',
                        'Price:',
                        'Description:',
                        'Status:'
                    ],
                    labels: [
                        'Payed sum:',
                        'Capacity of things:',
                        'Type of pay:',
                        'Credit card:',
                        'Chemical additions:',
                        'Date update record:'
                    ]
                  },
                  wallet: {
                    title: 'Information about wallet',
                    labels: [
                        'History of transactions',
                        'Sum of transaction:',
                        'Date of transaction:',
                        'Date update record:'
                    ],
                    notFound: 'Transactions not found'
                  },
                  histories: {
                    title: 'Information about transaction',
                    labels: [
                        'Sum of transaction:',
                        'Type transaction:',
                        'Credit card:',
                        'Date of transaction:',
                        'Date update record:'
                    ]
                  },
                  addForm: {
                    title: 'New record in table ',
                    tabs: {
                      tab1: {
                        name: 'Required fields',
                        context: {
                          person: {
                            labels: [
                                'First Name',
                                'Second Name',
                                'Username',
                                'Password',
                                'E-mail',
                                'Phone'
                            ]
                          },
                          machine: {
                            labels: [
                                'Name of machine',
                                'Description',
                                'Capacity, litres',
                                'Price',
                                'Status',
                                ['Working', 'Not working']
                            ]
                          },
                          stock: {
                            labels: [
                                'Name of stock',
                                'Sponsor',
                                'Discount',
                                'Expired stock date'
                            ]
                          },
                          draft: {
                            labels: [
                                'Capacity',
                                'Price',
                                'Type of pay',
                                ['Cash', 'Credit card'],
                                'Number of credit card',
                                'User',
                                'Machine',
                                'Сhemical additives',
                                'Date of pay'
                            ]
                          },
                          wallet: {
                            labels: [
                                'Balance',
                                'Histories Transactions'
                            ]
                          },
                          histories: {
                            labels: [
                                'Sum of transaction',
                                'Date transaction',
                                'Type of pay',
                                ['Cash', 'Credit card'],
                                'Credit card'
                            ]
                          }
                        }
                      },
                      tab2: {
                        name: 'Non-required fields',
                        context: {
                          person: {
                            labels: [
                              'Available machines:',
                              'Machines not found'
                            ]
                          },
                          machine: {
                            labels: [
                              'Available stocks:',
                              'Stocks not found'
                            ]
                          },
                          stock: {
                            labels: [
                              'Not found'
                            ]
                          },
                          draft: {
                            labels: [
                              'Not found'
                            ]
                          },
                          wallet: {
                            labels: [
                              'Not found'
                            ]
                          },
                          histories: {
                            labels: [
                              'Not found'
                            ]
                          }
                        }
                      }
                    },
                    btnTitle: 'CREATE'
                  },
                  editForm: {
                    title: 'Change record in table ',
                    tabs: {
                      tab1: {
                        name: 'Required fields',
                        context: {
                          person: {
                            labels: [
                              'First Name',
                              'Second Name',
                              'Username',
                              'Password',
                              'E-mail',
                              'Phone'
                            ]
                          },
                          machine: {
                            labels: [
                              'Name of machine',
                              'Description',
                              'Capacity, litres',
                              'Price',
                              'Status'
                            ]
                          },
                          stock: {
                            labels: [
                              'Name of stock',
                              'Sponsor',
                              'Discount',
                              'Expired stock date'
                            ]
                          },
                          draft: {
                            labels: [
                              'Edit impossible'
                            ]
                          },
                          wallet: {
                            labels: [
                              'Balance',
                              'Histories Transactions'
                            ]
                          },
                          histories: {
                            labels: [
                              'Sum of transaction',
                              'Date transaction',
                              'Type of pay',
                              ['Cash', 'Credit card'],
                              'Credit card'
                            ]
                          }
                        }
                      },
                      tab2: {
                        name: 'Non-required fields',
                        context: {
                          person: {
                            labels: [
                              'Available machines:',
                              'Machines not found'
                            ]
                          },
                          machine: {
                            labels: [
                              'Available stocks:',
                              'Stocks not found'
                            ]
                          },
                          stock: {
                            labels: [
                              'Not found'
                            ]
                          },
                          draft: {
                            labels: [
                              'Not found'
                            ]
                          },
                          wallet: {
                            labels: [
                              'Not found'
                            ]
                          },
                          histories: {
                            labels: [
                              'Not found'
                            ]
                          }
                        }
                      }
                    },
                    btnTitle: 'Change'
                  },
                  delForm: {
                    title: [
                      'Record will be deleted from ',
                        'Continue?'
                    ],
                    btns: [
                        'No, cancel',
                        'Yes, delete'
                    ]
                  }
                }
              }
            },
            tab2: {
              name: 'Reports',
              context: {
                monthlyDrafts: {
                  title: 'Purchases for the current month',
                  subtitle: 'Information about draft',
                  person: [
                    'User:',
                    'Current balance:',
                    'Login:',
                    'E-mail:',
                    'Phone:'
                  ],
                  machine: [
                    'Machine: ',
                    'Price:',
                    'Description:',
                    'Status:'
                  ],
                  labels: [
                    'Payed sum:',
                    'Capacity of things:',
                    'Type of pay:',
                    'Credit card:',
                    'Chemical additions:',
                    'Date update record:'
                  ],
                  notFound: 'Purchases not found'
                },
                babMachines: {
                  title: 'Broken machines',
                  labels: [
                      'Description:',
                      'Status:',
                      'Price:',
                      'Date update record:'
                  ],
                  notFound: 'Broken machines not exists'
                }
              }
            },
            tab3: {
              name: 'Statistic',
              context: {
                select: 'Statistic for:',
                stats: [
                    'Ordered'
                ],
                description: {
                  color: [
                      'Red',
                      'Blue',
                      'Dark-blue',
                      'Green'
                  ],
                  text: [
                      'very weak count of orders',
                      'weak count of orders',
                      'average count of orders',
                      'better count of orders'
                  ]
                },
                labels: [
                    'Price:',
                    'Description:',
                    'Capacity:',
                    'l.',
                    'Count of orders:'
                ]
              }
            }
          }
        },
        'ru-RU': {
          locales: {
            tip: 'Выбрать язык интерфейса',
            langSubTitle: 'Язык интерфейса:',
            selects: [
              'Английский',
              'Русский',
              'Украинский'
            ]
          },
          tabs: {
            tab1: {
              name: 'Адмистрирование',
              context: {
                title: 'Управление данными',
                table: {
                  tableInfo: [
                    'Кол-во записей:',
                    'Дата изменения:'
                  ],
                  btnTitle: 'ПОДРОБНОСТИ'
                },
                fullInfoTable: {
                  title: 'Управление ',
                  search: 'Введите запрос к ',
                  alerts: [
                    'Запись успешно добавлена',
                    'Запись успешно обновленна',
                    'Запись успешно удаленна',
                    'Ошибка сервера'
                  ],
                  crudBtns: [
                    'Добавить',
                    'Изменить',
                    'Удалить'
                  ],
                  person: {
                    user: {
                      title: 'Информация о пользователе',
                      labels: [
                        'Логин:',
                        'Пароль:',
                        'E-mail:',
                        'Телефон:',
                        'Дата изменения записи:'
                      ]
                    },
                    wallet: {
                      title: 'Кошелек',
                      history: 'История пополнений',
                      labels: [
                        'Сумма пополнения:',
                        'Дата пополнения:',
                        'Дата изменения записи:'
                      ],
                      notFound: 'Пополнений не найдено'
                    },
                    machine: {
                      title: 'Информация о машинах',
                      labels: [
                        'Вместимость:',
                        'Описание:',
                        'Цена:',
                        'Дата изменения записи:'
                      ],
                      notFound: 'Машина не используется'
                    }
                  },
                  machine: {
                    machine: {
                      title: 'Информация о стиральных машинах',
                      labels: [
                        'Процесс стирки:',
                        'Описание:',
                        'Статус:',
                        'Цена:',
                        'Дата изменения записи:'
                      ]
                    },
                    stock: {
                      title: 'Информация о скидке на машину',
                      labels: [
                        'Спонсор:',
                        'Сумма скидки:',
                        'Дата изменения записи:'
                      ],
                      notFound: 'Машина не имеет скидок'
                    }
                  },
                  stock: {
                    title: 'Информация о скидках',
                    labels:[
                      'Название скидки:',
                      'Спонсор:',
                      'Сумма скидки:',
                      'Дата истечения:',
                      'Дата изменения записи:'
                    ]
                  },
                  draft: {
                    title: 'Информация о чеках',
                    person: [
                      'Пользователь:',
                      'Текущий баланс:',
                      'Логин:',
                      'E-mail:',
                      'Телефон:'
                    ],
                    machine: [
                      'Машина: ',
                      'Цена:',
                      'Описание:',
                      'Статус:'
                    ],
                    labels: [
                      'Сплаченная сумма:',
                      'Объем вещей:',
                      'Вид оплаты:',
                      'Кредитная карта:',
                      'Хим. добавки:',
                      'Дата изменения записи:'
                    ]
                  },
                  wallet: {
                    title: 'Информация о кошельках',
                    labels: [
                      'История пополнений',
                      'Сумма пополнения:',
                      'Дата пополнения:',
                      'Дата изменения записи:'
                    ],
                    notFound: 'Пополнений не найдено'
                  },
                  histories: {
                    title: 'Информация о пополнениях',
                    labels: [
                      'Сумма пополнения:',
                      'Вид пополнения:',
                      'Кредитная карта:',
                      'Дата пополнения:',
                      'Дата изменения записи:'
                    ]
                  },
                  addForm: {
                    title: 'Добавить запись в таблицу ',
                    tabs: {
                      tab1: {
                        name: 'Обязательные поля',
                        context: {
                          person: {
                            labels: [
                              'Имя',
                              'Фамилия',
                              'Логин',
                              'Пароль',
                              'E-mail',
                              'Телефон'
                            ]
                          },
                          machine: {
                            labels: [
                              'Название машини',
                              'Описание',
                              'Вместимость, л',
                              'Цена',
                              'Статус',
                              ['Рабочая', 'Не рабочая']
                            ]
                          },
                          stock: {
                            labels: [
                              'Имя акции',
                              'Спонсор',
                              'Скидка',
                              'Время истечения'
                            ]
                          },
                          draft: {
                            labels: [
                              'Вместимость',
                              'Цена',
                              'Вид оплаты',
                              ['Наличный', 'Кредитная карта'],
                              'Номер кредитной карты',
                              'Пользователь',
                              'Машина',
                              'Хим. добавки',
                              'Дата оплаты'
                            ]
                          },
                          wallet: {
                            labels: [
                              'Баланс',
                              'История пополнений'
                            ]
                          },
                          histories: {
                            labels: [
                              'Сумма пополнения',
                              'Дата пополнения',
                              'Вид оплаты',
                              ['Наличные', 'Кредитная карта'],
                              'Кредитная карта'
                            ]
                          }
                        }
                      },
                      tab2: {
                        name: 'Не обязательные поля',
                        context: {
                          person: {
                            labels: [
                              'Доступные поля:',
                              'Машини не найдено'
                            ]
                          },
                          machine: {
                            labels: [
                              'Доступные акции:',
                              'Акций не найдено'
                            ]
                          },
                          stock: {
                            labels: [
                              'Не найдено'
                            ]
                          },
                          draft: {
                            labels: [
                              'Не найдено'
                            ]
                          },
                          wallet: {
                            labels: [
                              'Не найдено'
                            ]
                          },
                          histories: {
                            labels: [
                              'Не найдено'
                            ]
                          }
                        }
                      }
                    },
                    btnTitle: 'СОЗДАТЬ'
                  },
                  editForm: {
                    title: 'Изменить запись в таблице ',
                    tabs: {
                      tab1: {
                        name: 'Обязательные поля',
                        context: {
                          person: {
                            labels: [
                              'Имя',
                              'Фамилия',
                              'Логин',
                              'Пароль',
                              'E-mail',
                              'Телефон'
                            ]
                          },
                          machine: {
                            labels: [
                              'Название машини',
                              'Описание',
                              'Вместимость, л',
                              'Цена',
                              'Статус',
                              ['Рабочая', 'Не рабочая']
                            ]
                          },
                          stock: {
                            labels: [
                              'Имя акции',
                              'Спонсор',
                              'Скидка',
                              'Время истечения'
                            ]
                          },
                          draft: {
                            labels: [
                              'Редактирование невозможно'
                            ]
                          },
                          wallet: {
                            labels: [
                              'Баланс',
                              'История пополнений'
                            ]
                          },
                          histories: {
                            labels: [
                              'Сумма пополнения',
                              'Дата пополнения',
                              'Вид оплаты',
                              ['Наличные', 'Кредитная карта'],
                              'Кредитная карта'
                            ]
                          }
                        }
                      },
                      tab2: {
                        name: 'Не обязательные поля',
                        context: {
                          person: {
                            labels: [
                              'Доступные поля:',
                              'Машини не найдено'
                            ]
                          },
                          machine: {
                            labels: [
                              'Доступные акции:',
                              'Акций не найдено'
                            ]
                          },
                          stock: {
                            labels: [
                              'Не найдено'
                            ]
                          },
                          draft: {
                            labels: [
                              'Не найдено'
                            ]
                          },
                          wallet: {
                            labels: [
                              'Не найдено'
                            ]
                          },
                          histories: {
                            labels: [
                              'Не найдено'
                            ]
                          }
                        }
                      }
                    },
                    btnTitle: 'Изменить'
                  },
                  delForm: {
                    title: [
                      'Запись будет удаленна из ',
                      'Продолжить?'
                    ],
                    btns: [
                      'Нет, отмена',
                      'Да, удалить'
                    ]
                  }
                }
              }
            },
            tab2: {
              name: 'Отчеты',
              context: {
                monthlyDrafts: {
                  title: 'Покупки за этот месяц',
                  subtitle: 'Информация о чеках',
                  person: [
                    'Пользователь:',
                    'Текущий баланс:',
                    'Логин:',
                    'E-mail:',
                    'Телефон:'
                  ],
                  machine: [
                    'Машина: ',
                    'Цена:',
                    'Описание:',
                    'Статус:'
                  ],
                  labels: [
                    'Сплаченная сумма:',
                    'Объем вещей:',
                    'Вид оплаты:',
                    'Кредитная карта:',
                    'Хим. добавки:',
                    'Дата изменения записи:'
                  ],
                  notFound: 'Покупок не найдено'
                },
                babMachines: {
                  title: 'Поломанные машини',
                  labels: [
                    'Описание:',
                    'Статус:',
                    'Цена:',
                    'Дата изменения записи:'
                  ],
                  notFound: 'Сломанных машин не найдено'
                }
              }
            },
            tab3: {
              name: 'Статистика',
              context: {
                select: 'Статистика по:',
                stats: [
                  'Заказанные'
                ],
                description: {
                  color: [
                    'Красные',
                    'Голубой',
                    'Темно-голубой',
                    'Зеленный'
                  ],
                  text: [
                    'очень низкое кол-во продаж',
                    'низкое кол-во продаж',
                    'среднее кол-во продаж',
                    'хорошее кол-во продаж'
                  ]
                },
                labels: [
                  'Цена:',
                  'Описание:',
                  'Вместимость:',
                  'л.',
                  'Кол-во заказов:'
                ]
              }
            }
          }
        },
        'ua-UA': {
          locales: {
            tip: 'Виберіть мову інтерфейсу',
            langSubTitle: 'Мова інтерфейсу:',
            selects: [
              'Англійська',
              'Російська',
              'Українська'
            ]
          },
          tabs: {
            tab1: {
              name: 'Адміністрування',
              context: {
                title: 'Управління данними',
                table: {
                  tableInfo: [
                    'Кількість записів:',
                    'Дата зміни:'
                  ],
                  btnTitle: 'БІЛЬШЕ'
                },
                fullInfoTable: {
                  title: 'Управління ',
                  search: 'Напишіть запрос до ',
                  alerts: [
                    'Запис успішно додано',
                    'Запис успішно оновлено',
                    'Запис успішно видалено',
                    'Помилку серверу'
                  ],
                  crudBtns: [
                    'Додати',
                    'Змінити',
                    'Видалити'
                  ],
                  person: {
                    user: {
                      title: 'Інформація о користувачі',
                      labels: [
                        'Логін:',
                        'Пароль:',
                        'E-mail:',
                        'Телефон:',
                        'Дата зміни запису:'
                      ]
                    },
                    wallet: {
                      title: 'Гаманець',
                      history: 'Історія поповнень',
                      labels: [
                        'Сума поповнень:',
                        'Дата поповнення:',
                        'Дата зміни запису:'
                      ],
                      notFound: 'Поповнення не знайдено'
                    },
                    machine: {
                      title: 'Інформація о машинах',
                      labels: [
                        'Місткість:',
                        'Опис:',
                        'Ціна:',
                        'Дата зміни запису:'
                      ],
                      notFound: 'Машиною не користуются'
                    }
                  },
                  machine: {
                    machine: {
                      title: 'Інформація о пральній машині',
                      labels: [
                        'Процес прання:',
                        'Опис:',
                        'Статус:',
                        'Ціна:',
                        'Дата зміни запису:'
                      ]
                    },
                    stock: {
                      title: 'Інформація о знижке на машину',
                      labels: [
                        'Спонсор:',
                        'Сума знижки:',
                        'Дата зміни запису:'
                      ],
                      notFound: 'Машина не має знижок'
                    }
                  },
                  stock: {
                    title: 'Інформація о знижках',
                    labels:[
                      'Названня знижки:',
                      'Спонсор:',
                      'Сума знижки:',
                      'Дата витікання:',
                      'Дата зміни запису:'
                    ]
                  },
                  draft: {
                    title: 'Інформація о чеках',
                    person: [
                      'Користувач:',
                      'Теперешній баланс:',
                      'Логін:',
                      'E-mail:',
                      'Телефон:'
                    ],
                    machine: [
                      'Машина: ',
                      'Ціна:',
                      'Опис:',
                      'Статус:'
                    ],
                    labels: [
                      'Сплаченна сума:',
                      'Об`єм речей:',
                      'Вид сплаты:',
                      'Кредитна карта:',
                      'Хім. добавки:',
                      'Дата зміни запису:'
                    ]
                  },
                  wallet: {
                    title: 'Інформація о гаманцях',
                    labels: [
                      'Історія поповнень',
                      'Сума поповнень:',
                      'Дата поповнень:',
                      'Дата зміни запису:'
                    ],
                    notFound: 'Поповнень не знайдено'
                  },
                  histories: {
                    title: 'Інформація о поповненнях',
                    labels: [
                      'Сума поповнення:',
                      'Вид поповнення:',
                      'Кредитна карта:',
                      'Дата поповнення:',
                      'Дата зміни запису:'
                    ]
                  },
                  addForm: {
                    title: 'Добавить запись в таблицу ',
                    tabs: {
                      tab1: {
                        name: 'Обов`язкові поля',
                        context: {
                          person: {
                            labels: [
                              'Им`я',
                              'Прізвище',
                              'Логін',
                              'Пароль',
                              'E-mail',
                              'Телефон'
                            ]
                          },
                          machine: {
                            labels: [
                              'Назва машини',
                              'Опис',
                              'Місткість, л',
                              'Ціна',
                              'Статус',
                              ['Робоча', 'Не робоча']
                            ]
                          },
                          stock: {
                            labels: [
                              'Им`я акції',
                              'Спонсор',
                              'Знижки',
                              'Час витікання'
                            ]
                          },
                          draft: {
                            labels: [
                              'Місткість',
                              'Ціна',
                              'Вид сплаты',
                              ['Готівка', 'Кредитна карта'],
                              'Номер кредитної карти',
                              'Користувач',
                              'Машина',
                              'Хім. добавки',
                              'Дата сплаті'
                            ]
                          },
                          wallet: {
                            labels: [
                              'Баланс',
                              'Історія поповнень'
                            ]
                          },
                          histories: {
                            labels: [
                              'Сума поповнень',
                              'Дата поповнень',
                              'Вид сплаті',
                              ['Готівка', 'Кредитна карта'],
                              'Кредитна карта'
                            ]
                          }
                        }
                      },
                      tab2: {
                        name: 'Не обов`язкові поля',
                        context: {
                          person: {
                            labels: [
                              'Доступні поля:',
                              'Машин не знайдено'
                            ]
                          },
                          machine: {
                            labels: [
                              'Доступні акції:',
                              'Акцій не знайдено'
                            ]
                          },
                          stock: {
                            labels: [
                              'Не знайдено'
                            ]
                          },
                          draft: {
                            labels: [
                              'Не знайдено'
                            ]
                          },
                          wallet: {
                            labels: [
                              'Не знайдено'
                            ]
                          },
                          histories: {
                            labels: [
                              'Не знайдено'
                            ]
                          }
                        }
                      }
                    },
                    btnTitle: 'СТВОРИТИ'
                  },
                  editForm: {
                    title: 'Змінити запис у таблиці ',
                    tabs: {
                      tab1: {
                        name: 'Обов`язкові поля',
                        context: {
                          person: {
                            labels: [
                              'Им`я',
                              'Прізвище',
                              'Логін',
                              'Пароль',
                              'E-mail',
                              'Телефон'
                            ]
                          },
                          machine: {
                            labels: [
                              'Назва машини',
                              'Опис',
                              'Місткість, л',
                              'Ціна',
                              'Статус',
                              ['Робоча', 'Не робоча']
                            ]
                          },
                          stock: {
                            labels: [
                              'Им`я акції',
                              'Спонсор',
                              'Знижки',
                              'Час витікання'
                            ]
                          },
                          draft: {
                            labels: [
                              'Редагування неможливо'
                            ]
                          },
                          wallet: {
                            labels: [
                              'Баланс',
                              'Історія поповнень'
                            ]
                          },
                          histories: {
                            labels: [
                              'Сума поповнень',
                              'Дата поповнень',
                              'Вид сплаті',
                              ['Готівка', 'Кредитна карта'],
                              'Кредитна карта'
                            ]
                          }
                        }
                      },
                      tab2: {
                        name: 'Не обов`язкові поля',
                        context: {
                          person: {
                            labels: [
                              'Доступні поля:',
                              'Машин не знайдено'
                            ]
                          },
                          machine: {
                            labels: [
                              'Доступні акції:',
                              'Акцій не знайдено'
                            ]
                          },
                          stock: {
                            labels: [
                              'Не знайдено'
                            ]
                          },
                          draft: {
                            labels: [
                              'Не знайдено'
                            ]
                          },
                          wallet: {
                            labels: [
                              'Не знайдено'
                            ]
                          },
                          histories: {
                            labels: [
                              'Не знайдено'
                            ]
                          }
                        }
                      }
                    },
                    btnTitle: 'ЗМІНИТИ'
                  },
                  delForm: {
                    title: [
                      'Запис буде видалено з ',
                      'Продовжити?'
                    ],
                    btns: [
                      'Ні, відміна',
                      'Так, видалити'
                    ]
                  }
                }
              }
            },
            tab2: {
              name: 'Звіт',
              context: {
                monthlyDrafts: {
                  title: 'Купівлі за цей місяць',
                  subtitle: 'Інформація о чеках',
                  person: [
                    'Користувач:',
                    'Теперешній баланс:',
                    'Логін:',
                    'E-mail:',
                    'Телефон:'
                  ],
                  machine: [
                    'Машина: ',
                    'Ціна:',
                    'Опис:',
                    'Статус:'
                  ],
                  labels: [
                    'Сплаченна сума:',
                    'Об`єм речей:',
                    'Вид сплаты:',
                    'Кредитна карта:',
                    'Хім. добавки:',
                    'Дата зміни запису:'
                  ],
                  notFound: 'Купівлі відсутні'
                },
                babMachines: {
                  title: 'Зламані машини',
                  labels: [
                    'Опис:',
                    'Статус:',
                    'Ціна:',
                    'Дата зміни запису:'
                  ],
                  notFound: 'Зламанних машин не знайдено'
                }
              }
            },
            tab3: {
              name: 'Статистика',
              context: {
                select: 'Статистика по:',
                stats: [
                  'Замовленні'
                ],
                description: {
                  color: [
                    'Червоний',
                    'Голубий',
                    'Темно-голубий',
                    'Зеленний'
                  ],
                  text: [
                    'дуже слабкі продажі',
                    'слабкі продажи',
                    'середні продажи',
                    'хорошы продажи'
                  ]
                },
                labels: [
                  'Ціна:',
                  'Опис:',
                  'Місткість:',
                  'л.',
                  'Кількість продаж:'
                ]
              }
            }
          }
        }
      },
      curLocale: {},
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
    logout() {

    },
    changeLangEN() {
      localStorage['lang'] = 'en-EN'
      this.curLocale = this.locales['en-EN']
    },
    changeLangRU() {
      localStorage['lang'] = 'ru-RU'
      this.curLocale = this.locales['ru-RU']
    },
    changeLangUA() {
      localStorage['lang'] = 'ua-UA'
      this.curLocale = this.locales['ua-UA']
    },
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
          data: this.forms[table.toLowerCase()],
          headers: {
            Authorization: 'Bearer ' + localStorage['uid']
          }
        }).then(resp => {
          console.log(resp)

          this.$nextTick(() => {
            this.showAddForm = false;
            this.alertSuccess = true;
            this.alertText = this.curLocale.tabs.tab1.context.fullInfoTable.alerts[0];
            this.chosenTableInfo.push(resp.data)
          })

          for (let item of Object.keys(this.forms[table.toLowerCase()])) {
            this.forms[table.toLowerCase()][item] = '';
          }
        }).catch(err => {
          console.log(err)
          this.$nextTick(() => {
            this.alertErr = true;
            this.alertText = this.curLocale.tabs.tab1.context.fullInfoTable.alerts[3];
          })
        })
      } catch (e) {
        console.log('err', e)
        this.$nextTick(() => {
          this.alertErr = true;
          this.alertText = this.curLocale.tabs.tab1.context.fullInfoTable.alerts[3];
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
          data: this.forms[table.toLowerCase()],
          headers: {
            Authorization: 'Bearer ' + localStorage['uid']
          }
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
          this.alertText = this.curLocale.tabs.tab1.context.fullInfoTable.alerts[1]
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
          url: `http://${ip}:${port}/api/${table.toLowerCase()}/${curItem}`,
          headers: {
            Authorization: 'Bearer ' + localStorage['uid']
          }
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
          this.alertText = this.curLocale.tabs.tab1.context.fullInfoTable.alerts[2]
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
    axios.get(`http://${ip}:${port}/api/persons`, {
      headers: {
        Authorization: 'Bearer ' + localStorage['uid']
      }
    })
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
    axios.get(`http://${ip}:${port}/api/machines`, {
      headers: {
        Authorization: 'Bearer ' + localStorage['uid']
      }
    })
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
    axios.get(`http://${ip}:${port}/api/drafts`, {
      headers: {
        Authorization: 'Bearer ' + localStorage['uid']
      }
    })
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
    axios.get(`http://${ip}:${port}/api/stocks`, {
      headers: {
        Authorization: 'Bearer ' + localStorage['uid']
      }
    })
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
    axios.get(`http://${ip}:${port}/api/wallets`, {
      headers: {
        Authorization: 'Bearer ' + localStorage['uid']
      }
    })
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
    axios.get(`http://${ip}:${port}/api/histories`, {
      headers: {
        Authorization: 'Bearer ' + localStorage['uid']
      }
    })
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
    axios.get(`http://${ip}:${port}/api/drafts/reports/monthly-checks`, {
      headers: {
        Authorization: 'Bearer ' + localStorage['uid']
      }
    })
      .then(resp => {
        this.reports.monthlyDrafts = resp.data
      })
    axios.get(`http://${ip}:${port}/api/machines/reports/bad-status-machines`, {
      headers: {
        Authorization: 'Bearer ' + localStorage['uid']
      }
    })
      .then(resp => {
        this.reports.babMachines = resp.data
      })
  }
}
</script>

<style scoped>

</style>