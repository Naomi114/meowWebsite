<template>
  <div>
    <h2>我的購物車</h2>
    <div v-if="cart.length === 0">
      <p>購物車是空的！</p>
    </div>
    <div v-else>
      <div v-for="item in cart" :key="item.id">
        <input 
          type="checkbox" 
          v-model="item.selected" 
          :disabled="item.selected === undefined" 
        />
        <p>{{ item.name }} - {{ item.price }}元 × {{ item.quantity }}</p>
        <button @click="removeItem(item.id)">刪除此商品</button>
      </div>
      <div>
        <p>總金額: {{ totalPrice }}元</p>
        <button @click="clearCart">一鍵清空購物車</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useStore } from 'vuex';

const store = useStore();
const cart = computed(() => store.state.cart);

// 計算已勾選的商品的總金額
const totalPrice = computed(() => {
  return cart.value.filter(item => item.selected).reduce((total, item) => total + item.price * item.quantity, 0);
});

// 移除商品
const removeItem = (id) => {
  store.dispatch('removeFromCart', id);
};

// 清空購物車
const clearCart = () => {
  if (window.confirm("Are you sure you want to clear the cart?")) {
    store.dispatch('clearCart');
  }
};
</script>
