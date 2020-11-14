<template>
  <v-card max-width="220">
    <v-menu transition="fab-transition" offset-y v-model="activeInfoProduct">
      <template v-slot:activator="{on, attrs}">
        <div style="justify-content: center; display: flex">
          <v-btn
              icon
              x-large
              title="Описание машини"
              v-bind="attrs"
              v-on="on"
              v-if="!activeInfoProduct"
              @click="activeInfoProduct = true"
          >
            <v-icon x-large>
              keyboard_arrow_down
            </v-icon>
          </v-btn>
          <v-btn
              icon
              x-large
              v-bind="attrs"
              v-on="on"
              v-if="activeInfoProduct"
              @click="activeInfoProduct = false"
              @mouseleave="reRenderInfoBtn"
          >
            <v-icon x-large>
              keyboard_arrow_up
            </v-icon>
          </v-btn>
        </div>
        <div>
          <v-img :src="machine.img" width="220" height="220"></v-img>
          <v-card-subtitle>
            {{machine.name}} <br>
            {{locales.machines.price}} {{machine.price}} {{locales.machines.currency}}
          </v-card-subtitle>
        </div>
      </template>
      <v-card>
        <v-card-text>
          <b>{{locales.machines.status}}</b> {{machine.status}} <br />
          <b>{{locales.machines.capacity}}</b> {{machine.capacity}} {{locales.machines.litres}}<br />
          <b>{{locales.machines.description}}</b> {{machine.description}}
        </v-card-text>
      </v-card>
    </v-menu>
    <v-btn block color="primary" @click="orderMachine">{{locales.machines.btnTitle}}</v-btn>
    <v-snackbar
        top
        v-model="alertSuccess"
        outlined
        color="success"
        timeout="1500"
    >
      {{locales.machines.labels[0]}}
      <template v-slot:action="{attrs}">
        <v-btn icon v-bind="attrs" @click="alertSuccess = false">
          <v-icon>
            close
          </v-icon>
        </v-btn>
      </template>
    </v-snackbar>
    <v-snackbar
        top
        color="red"
        v-model="alertErr"
        outlined
        timeout="1500"
    >
      {{locales.machines.labels[1]}}
      <template v-slot:action="{attrs}">
        <v-btn icon v-bind="attrs" @click="alertErr = false">
          <v-icon>
            close
          </v-icon>
        </v-btn>
      </template>
    </v-snackbar>
    <v-snackbar
        top
        color="red"
        v-model="machineisOrdered"
        outlined
        timeout="1500"
    >
      {{locales.machines.labels[2]}}
      <template v-slot:action="{attrs}">
        <v-btn icon v-bind="attrs" @click="machineisOrdered = false">
          <v-icon>
            close
          </v-icon>
        </v-btn>
      </template>
    </v-snackbar>
  </v-card>
</template>

<script>
const [ip, port, axios] = ['localhost', '9000', require('axios')]
export default {
  name: "Machine",
  props: {
    machine: {
      type: Object,
      required: true
    },
    locales: {
      type: Object,
      required: true
    }
  },
  data() {
    return {
      activeInfoProduct: false,
      alertErr: false,
      alertSuccess: false,
      machineisOrdered: false,
    }
  },
  methods: {
    orderMachine() {
      // send PUT
      if (!(this.alertSuccess || this.alertErr || this.machineisOrdered)) {
        axios({
          url: `http://${ip}:${port}/api/persons/current`,
          method: 'GET',
          headers: {
            Authorization: localStorage['uid']
          }
        }).then(resp => {
          let req = new XMLHttpRequest()
          req.responseType = 'json'
          req.open('GET', `http://${ip}:${port}/api/persons/${resp.data.id}`)
          req.send()
          req.onerror = (err) => {
            console.error(err)
          }
          req.onload = () => {
            let machines = []
            let oldMachine = req.response.machine
            if (oldMachine.length > 0) {
              for (let i=0;i<oldMachine.length;i++) {
                if (oldMachine[i].id === this.machine.id) {
                  this.machineisOrdered = true;
                  throw "Machine already ordered.";
                }
                machines.push({id: oldMachine[i].id})
              }
            }
            machines.push({id: this.machine.id})
            axios.put(`http://${ip}:${port}/api/persons/${resp.data.id}`, {
              machine: machines
            })
                .then(resp => {
                  console.info(resp)
                  this.alertSuccess = true
                })
                .catch(() => {
                  this.alertErr = true;
                })
          }
        }).catch(() => {
          this.alertErr = true;
        })
      }
    },
    reRenderInfoBtn() {
      this.activeInfoProduct = false
    }
  }
}
</script>

<style scoped>

</style>