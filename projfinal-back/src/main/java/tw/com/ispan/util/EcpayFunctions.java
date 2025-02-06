package tw.com.ispan.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class EcpayFunctions {
    private static final String ACTION_URL = "https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5";
    private static final String RETURN_URL = "https://28f7-1-160-21-206.ngrok-free.app/pages/ecpay/return";
    private static final String MERCHANT_ID = "2000132";
    private static final String HASH_KEY = "5294y06JbISpM5x9";
    private static final String HASH_IV = "v77hoKGq4kWxNNIS";

    // 建立 ECPay 付款表單
    public String buildEcpayForm(String body, String merchantTradeNo) {
        JSONObject obj = new JSONObject(body);

        // 確保 MerchantTradeNo 為唯一且符合格式
        if (merchantTradeNo == null || merchantTradeNo.isEmpty()) {
            merchantTradeNo = String.valueOf(System.currentTimeMillis());
        }
        merchantTradeNo = merchantTradeNo.replaceAll("[^0-9]", "");
        if (merchantTradeNo.length() > 20) {
            merchantTradeNo = merchantTradeNo.substring(0, 20);
        }

        String name = obj.isNull("name") ? "測試商品1" : obj.getString("name");
        String total = obj.isNull("total") ? "1000" : obj.getString("total");
        String desc = obj.isNull("desc") ? "這是一個測試用交易" : obj.getString("desc");
        String date = obj.isNull("date") ? "2025/01/15 12:00:00" : obj.getString("date");

        // 產生 ECPay 付款參數
        Map<String, String> parameters = createEcpayData(merchantTradeNo, name, total, desc, date);

        StringBuilder builder = new StringBuilder();
        builder.append("<form id='payForm' target='_blank' action='").append(ACTION_URL).append("' method='POST'>");

        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            builder.append("<input type='hidden' name='")
                    .append(entry.getKey()).append("' value='")
                    .append(entry.getValue()).append("'/>");
        }
        builder.append("<script>document.getElementById('payForm').submit();</script>");
        builder.append("</form>");

        return builder.toString();
    }

    // 建立 ECPay 付款參數
    private Map<String, String> createEcpayData(String merchantTradeNo, String name, String total, String desc,
            String date) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("MerchantID", MERCHANT_ID);
        parameters.put("MerchantTradeNo", merchantTradeNo);
        parameters.put("ItemName", name); // 如果有多個商品，應用 `#` 分隔
        parameters.put("TotalAmount", total);
        parameters.put("TradeDesc", desc);
        parameters.put("MerchantTradeDate", date);
        parameters.put("PaymentType", "aio");
        parameters.put("ChoosePayment", "ALL");
        parameters.put("ReturnURL", RETURN_URL);

        // 產生 CheckMacValue
        String checkMacValue = genCheckMacValue(parameters, HASH_KEY, HASH_IV);
        parameters.put("CheckMacValue", checkMacValue);

        return parameters;
    }

    // 產生 CheckMacValue
    private String genCheckMacValue(Map<String, String> params, String hashKey, String hashIV) {
        Set<String> keySet = params.keySet();
        TreeSet<String> sortedKeys = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        sortedKeys.addAll(keySet);

        StringBuilder paramStr = new StringBuilder();
        for (String param : sortedKeys) {
            if (!param.equals("CheckMacValue")) {
                paramStr.append("&").append(param).append("=").append(params.get(param));
            }
        }

        // URL encode + SHA-256 雜湊
        String rawData = "HashKey=" + hashKey + paramStr + "&HashIV=" + hashIV;
        String urlEncodedData = urlEncode(rawData).toLowerCase();
        urlEncodedData = urlEncodedData.replaceAll("%21", "!").replaceAll("%28", "(").replaceAll("%29", ")");

        return hash(urlEncodedData.getBytes(), "SHA-256");
    }

    // URL Encode 方法
    private static String urlEncode(String data) {
        try {
            return URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    // SHA-256 雜湊方法
    private String hash(byte[] data, String mode) {
        try {
            MessageDigest md = MessageDigest.getInstance(mode);
            return bytesToHex(md.digest(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    // 轉換 Byte 陣列為 Hex 字串
    private String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    // 驗證 ECPay 回傳的 CheckMacValue
    public boolean verifyCheckMacValue(Map<String, String> returnParams) {
        String returnCheckMacValue = returnParams.get("CheckMacValue");
        if (returnCheckMacValue == null) {
            System.out.println("CheckMacValue 不存在！");
            return false;
        }

        String generatedCheckMacValue = genCheckMacValue(returnParams, HASH_KEY, HASH_IV);
        if (!returnCheckMacValue.equals(generatedCheckMacValue)) {
            System.out.println("CheckMacValue 驗證失敗！");
            return false;
        }

        return true;
    }

    // 處理 ECPay 回傳參數
    public void handleEcpayReturn(Map<String, String> returnParams) {
        for (Map.Entry<String, String> entry : returnParams.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        if (verifyCheckMacValue(returnParams)) {
            String merchantTradeNo = returnParams.get("MerchantTradeNo");
            String tradeAmt = returnParams.get("TradeAmt");
            System.out.println("交易成功, MerchantTradeNo: " + merchantTradeNo + ", 交易金額: " + tradeAmt);
        } else {
            System.out.println("CheckMacValue 驗證失敗！");
        }
    }
}
