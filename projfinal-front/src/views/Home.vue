<template>
  <div>
    <h2>商城商品</h2>
    <div class="products">
      <ProductCard
        v-for="product in products"
        :key="product.id"
        :product="product"
      />
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import ProductCard from '../components/ProductCard.vue';

const products = ref([
  { id: 1, name: 'iSpan測試商品 1', price: 1000, description: '這是1個iSpan測試用交易' },
  { id: 2, name: 'iSpan測試商品 2', price: 1200, description: '這是2個iSpan測試用交易' },
  { id: 3, name: 'iSpan測試商品 3', price: 1500, description: '這是3個iSpan測試用交易' },
]);
</script>

<style scoped>
.products {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;
}
</style>
