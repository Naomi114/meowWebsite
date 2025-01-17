<template>
  <div class="product-card">
    <h3>{{ product.name }}</h3>
    <p>{{ product.description }}</p>
    <p>價格: {{ product.price }}元</p>
    <button @click="addToCart(product)">加入購物車</button>
  </div>
</template>

<script setup>
import { useStore } from 'vuex';

const props = defineProps({
  product: Object
});

const store = useStore();

const addToCart = (product) => {
  store.dispatch('addToCart', product);
};
</script>

<style scoped>
.product-card {
  border: 1px solid #ddd;
  padding: 20px;
  text-align: center;
  background-color: #fff;
}

button {
  background-color: #007bff;
  color: white;
  padding: 10px;
  border: none;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}
</style>
