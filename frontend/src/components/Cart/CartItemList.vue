<template>
  <div>
    <v-card flat v-for="(item, i) in items" :key="i">
      <CartItem
          :item="item"
          :locales="locales"
          :all-cart="allCart"
          :update-cart-remove-item="updateCartRemoveItem"
      />
    </v-card>
  </div>
</template>

<script>
import CartItem from "@/components/Cart/CartItem";
export default {
  name: "CartItemList",
  components: {CartItem},
  props: ['items', 'locales', 'allCart', 'updateCartRemoveItem']
}
</script>

<style scoped>

</style>