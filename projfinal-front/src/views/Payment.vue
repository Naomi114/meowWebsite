<template>
  <div id="payment">
    <h2>支付頁面</h2>
    <p>確認購物車商品後，點擊下方按鈕完成支付。</p>
    <ul>
      <li v-for="item in selectedCart" :key="item.id">
        {{ item.name }} - 單價: {{ item.price }} 元，數量: {{ item.quantity }}
      </li>
    </ul>
    <p><strong>總金額:</strong> {{ totalAmount }} 元</p>
    <!-- 支付按鈕 -->
    <form @submit.prevent="sendPayment">
      <button type="submit">立即支付</button>
    </form>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useStore } from "vuex";
import axios from "axios";

// 使用 Vuex 抓取購物車資料
const store = useStore();
const cart = computed(() => store.state.cart);

// 篩選勾選的商品
const selectedCart = computed(() =>
  cart.value.filter((item) => item.selected)
);

// 計算總金額
const totalAmount = computed(() =>
  selectedCart.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
);

// 將勾選的購物車資料轉換為後端需要的格式
const convertCartToPaymentData = () => {
  const name = selectedCart.value.map((item) => item.name).join(", "); // 商品名稱
  const total = totalAmount.value.toString(); // 總金額
  const desc = `這是測試用交易，包含 ${selectedCart.value.length} 項商品`; // 描述
  return { name, total, desc };
};

// 發送支付請求
const sendPayment = () => {
  const body = convertCartToPaymentData();

  axios
    .post("http://localhost:8080/pages/ecpay/send", body)
    .then((response) => {
      console.log("支付請求成功，回應內容：", response.data);

      // 動態生成後端回傳的表單
      const container = document.createElement("div");
      container.id = "paymentForm";
      document.body.appendChild(container);
      container.innerHTML = response.data;

      // 執行返回的腳本以提交表單
      const script = container.querySelector("script");
      if (script) eval(script.textContent);
    })
    .catch((error) => {
      console.error("支付請求發送失敗：", error);
    });
};
</script>

<style scoped>
#payment {
  padding: 20px;
  background-color: #fff;
  border: 1px solid #ccc;
  border-radius: 5px;
}

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
