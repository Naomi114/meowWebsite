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
    private static final String RETURN_URL = "https://62bc-1-160-20-20.ngrok-free.app/pages/ecpay/return";
    private static final String MERCHANT_ID = "2000132";
    private static final String HASH_KEY = "5294y06JbISpM5x9";
    private static final String HASH_IV = "v77hoKGq4kWxNNIS";

    // Method to build the ECPay form
    public String buildEcpayForm(String body, String string) {
        JSONObject obj = new JSONObject(body);

        String id = obj.isNull("id") ? String.valueOf(System.currentTimeMillis()) : obj.getString("id");
        id = id.replaceAll("[^0-9]", "");
        if (id.length() > 20) {
            id = id.substring(0, 20);
        }

        String name = obj.isNull("name") ? "測試商品 1" : obj.getString("name");
        String total = obj.isNull("total") ? "1000" : obj.getString("total");
        String desc = obj.isNull("desc") ? "這是一個測試用交易" : obj.getString("desc");
        String date = obj.isNull("date") ? "2025/01/15 12:00:00" : obj.getString("date");

        // Create ECPay parameters
        Map<String, String> parameters = this.createEcpayData(id, name, total, desc, date);

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

    // Helper method to create ECPay parameters
    private Map<String, String> createEcpayData(String id, String name, String total, String desc, String date) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("MerchantID", MERCHANT_ID);

        String merchantTradeNo = id.replaceAll("[^0-9]", "");
        if (merchantTradeNo.length() > 20) {
            merchantTradeNo = merchantTradeNo.substring(0, 20);
        }
        parameters.put("MerchantTradeNo", merchantTradeNo);
        parameters.put("ItemName", name);
        parameters.put("TotalAmount", total);
        parameters.put("TradeDesc", desc);
        parameters.put("MerchantTradeDate", date);
        parameters.put("PaymentType", "aio");
        parameters.put("ChoosePayment", "ALL");
        parameters.put("ReturnURL", RETURN_URL);

        // Generate CheckMacValue
        String checkMacValue = genCheckMacValue(parameters, HASH_KEY, HASH_IV);
        parameters.put("CheckMacValue", checkMacValue);

        return parameters;
    }

    // Method to generate CheckMacValue
    private String genCheckMacValue(Map<String, String> params, String hashKey, String hashIV) {
        Set<String> keySet = params.keySet();
        TreeSet<String> treeSet = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        treeSet.addAll(keySet);
        StringBuilder paramStr = new StringBuilder();
        for (String param : treeSet) {
            if (!param.equals("CheckMacValue")) {
                paramStr.append("&").append(param).append("=").append(params.get(param));
            }
        }

        // URL encode and hash the parameters
        String urlEncode = urlEncode("HashKey=" + hashKey + paramStr + "&HashIV=" + hashIV).toLowerCase();
        urlEncode = urlEncode.replaceAll("%21", "!").replaceAll("%28", "(").replaceAll("%29", ")");

        return hash(urlEncode.getBytes(), "SHA-256");
    }

    // URL encode method
    private static String urlEncode(String data) {
        try {
            return URLEncoder.encode(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    // Hash method to generate the hash of data
    private String hash(byte[] data, String mode) {
        try {
            MessageDigest md = MessageDigest.getInstance(mode);
            return bytesToHex(md.digest(data));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    // Convert bytes to hex string
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

    // Method to verify CheckMacValue returned by ECPay
    public boolean verifyCheckMacValue(Map<String, String> returnParams) {
        String returnCheckMacValue = returnParams.get("CheckMacValue");
        // Generate expected CheckMacValue
        String generatedCheckMacValue = genCheckMacValue(returnParams, HASH_KEY, HASH_IV);
        // Compare and return if they match
        return returnCheckMacValue.equals(generatedCheckMacValue);
    }

    // Method to handle the ECPay return parameters
    public void handleEcpayReturn(Map<String, String> returnParams) {
        // Print all returned parameters
        for (Map.Entry<String, String> entry : returnParams.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        // Verify CheckMacValue
        if (verifyCheckMacValue(returnParams)) {
            // Handle the transaction if verification is successful
            String merchantTradeNo = returnParams.get("MerchantTradeNo");
            String tradeAmt = returnParams.get("TradeAmt");
            System.out.println("交易成功, MerchantTradeNo: " + merchantTradeNo + ", 交易金額: " + tradeAmt);
        } else {
            // Handle error if CheckMacValue verification fails
            System.out.println("CheckMacValue 驗證失敗！");
        }
    }

    // Method to handle and clean MerchantTradeNo
    public void handleMerchantTradeNo(String merchantTradeNo) {
        System.out.println("Merchant Trade No: " + merchantTradeNo);
        String numericPart = merchantTradeNo.replaceAll("[^0-9]", "");
        if (numericPart.length() > 20) {
            numericPart = numericPart.substring(0, 20);
        }
        System.out.println("Numeric part: " + numericPart);
    }
}
