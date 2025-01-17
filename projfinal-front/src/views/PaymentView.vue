<template>
  <div id="app">
    <header>
      <h1>歡迎來到我的購物網站</h1>
      <nav>
        <RouterLink to="/">首頁</RouterLink>
        <RouterLink to="/cart">購物車</RouterLink>
        <RouterLink to="/payment">支付</RouterLink>
      </nav>
    </header>
    <RouterView />
    <!-- 保留原本的支付按鈕 -->
    <form id="paymentForm" @submit.prevent="sendPayment">
      <button type="submit">立即支付</button>
    </form>
  </div>
</template>

<script setup>
import { RouterLink } from 'vue-router';
import axios from 'axios';

// 假設購物車資料
const cart = [
  { id: 1, name: "商品 A", price: 1000, quantity: 1 },
  { id: 2, name: "商品 B", price: 2000, quantity: 2 },
];

// 方法將購物車資料轉換成所需格式
const convertCartToPaymentData = () => {
  // 將購物車資料整合成單個商品名稱、總金額、描述
  const name = cart.map(item => item.name).join(", ");  // 商品名稱
  const total = cart.reduce((sum, item) => sum + (item.price * item.quantity), 0).toString();  // 總金額
  const desc = "這是測試用交易，包含 " + cart.length + " 項商品";  // 描述

  return { name, total, desc };
};

// 送出支付請求的方法
const sendPayment = () => {
  const body = convertCartToPaymentData();

  axios.post("http://localhost:8080/pages/ecpay/send", body)
    .then((response) => {
      // 在這裡處理後端回傳的結果
      console.log(response.data);
      const container = document.getElementById("payment");
      container.innerHTML = response.data;
      eval(container.querySelector("script").textContent); 
    })
    .catch((error) => {
      console.error("支付請求發送失敗", error);
    });
};
</script>

<style scoped>
body {
  font-family: Arial, sans-serif;
  background-color: #f0f0f0;
  margin: 0;
  padding: 0;
}

#app {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

header {
  background-color: #343a40;
  color: white;
  padding: 20px;
  text-align: center;
}

nav {
  margin-top: 15px;
}

nav a {
  margin: 0 20px;
  text-decoration: none;
  color: #007bff;
  font-size: 18px;
}

nav a:hover {
  text-decoration: underline;
}

/* 原本按鈕樣式 */
button {
  background-color: #28a745;
  color: white;
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #218838;
}
</style>
