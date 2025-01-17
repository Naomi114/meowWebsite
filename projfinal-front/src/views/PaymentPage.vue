<template>
    <div>
      <h2>付款頁面</h2>
      <p>總金額: ${{ paymentRequest.amount }}</p>
      <button @click="handlePayment">處理支付</button>
    </div>
  </template>
  
  <script setup>
  import { ref } from 'vue';
  
  const paymentRequest = ref({
    amount: 0,
    description: '購物網站商品支付',
    itemName: '測試商品',
  });
  
  const handlePayment = async () => {
    // 從購物車中讀取總金額等資訊
    const cart = JSON.parse(localStorage.getItem('cart') || '[]');
    const totalAmount = cart.reduce((total, item) => total + item.price, 0);
    paymentRequest.value.amount = totalAmount;
  
    try {
      const response = await fetch('http://localhost:8080/payment/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(paymentRequest.value),
      });
      const data = await response.json();
      // 跳轉到綠界支付頁面
      window.location.href = data.paymentUrl;
    } catch (error) {
      console.error('Error:', error);
    }
  };
  </script>
  