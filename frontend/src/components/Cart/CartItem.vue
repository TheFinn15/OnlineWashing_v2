<template>
  <v-container>
    <v-row>
      <v-col cols="4">
        <v-img width="220" height="220" :src="item.img"></v-img>
      </v-col>
      <v-col cols="6">
        <v-menu open-on-hover offset-y>
          <template v-slot:activator="{on, attrs}">
            <v-card-title v-on="on" v-bind="attrs">
              {{item.name}} {{item.capacity}} {{locales.machines.cart.labels[2]}}
            </v-card-title>
          </template>
          <v-card>
            <v-card-text>
              <b>{{locales.machines.cart.labels[0]}}</b> {{item.description}} <br>
              <b>{{locales.machines.cart.labels[1]}}</b> {{item.status}}
            </v-card-text>
          </v-card>
        </v-menu>
      </v-col>
      <v-col>
        <v-row>
          <v-col cols="12">
            <v-btn icon @click="delCartItem">
              <v-icon>
                close
              </v-icon>
            </v-btn>
          </v-col>
          <v-col cols="12">
            {{item.price}} {{locales.machines.cart.labels[3]}}
          </v-col>
        </v-row>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
const [ip, port, axios] = ['localhost', '25016', require('axios')]

export default {
  name: "CartItem",
  props: ['item', 'locales', 'allCart', 'updateCartRemoveItem'],
  methods: {
    delCartItem() {
      let newItems = this.allCart.filter(i => i.id !== this.item.id)
      axios({
        url: `http://${ip}:${port}/api/persons`,
        method: 'PUT',
        data: {
          machine: newItems
        },
        headers: {
          Authorization: 'Bearer ' + localStorage['uid']
        }
      }).then(resp => {
          this.updateCartRemoveItem({
            items: resp.data.machine
          })
      })
    }
  }
}
</script>

<style scoped>

</style>