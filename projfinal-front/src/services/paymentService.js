import axios from 'axios';

const API_URL = 'http://localhost:8080/api/payment/create';  // 修正為後端的 API URL

export default {
    async initiatePayment(amount) {
        try {
            const response = await axios.post(API_URL, { amount });
            return response.data;  // 返回綠界支付頁面的 URL
        } catch (error) {
            console.error('支付請求失敗:', error);
            throw new Error("支付請求發生錯誤，請稍後再試！");  // 捕獲錯誤並拋出
        }
    }
};
