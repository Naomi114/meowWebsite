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
	private static final String RETURN_URL = "https://f9d4-1-160-6-252.ngrok-free.app/pages/ecpay/return";
	private static final String MERCHANT_ID = "2000132";
	private static final String HASH_KEY = "5294y06JbISpM5x9";
	private static final String HASH_IV = "v77hoKGq4kWxNNIS";

	public String buildEcpayForm(String body) {
		JSONObject obj = new JSONObject(body);
		String id = obj.isNull("id") ? "iSpan" + System.currentTimeMillis() : obj.getString("id");
		String name = obj.isNull("name") ? "測試商品 1" : obj.getString("name");
		String total = obj.isNull("total") ? "1000" : obj.getString("total");
		String desc = obj.isNull("desc") ? "這是1個測試用交易" : obj.getString("desc");
		String date = obj.isNull("date") ? "2025/01/15 12:00:00" : obj.getString("date");

		Map<String, String> parameters = this.createEcpayData(id, name, total, desc, date);

		StringBuilder builder = new StringBuilder();
		builder.append("<form id='payForm' target='_blank' action='" + ACTION_URL + "' method='POST'>");

		Set<String> keys = parameters.keySet();
		for (String key : keys) {
			String value = parameters.get(key);
			builder.append("<input type='hidden' name='")
					.append(key).append("' value='")
					.append(value).append("'/>");
		}
		builder.append("<script>payForm.submit()</script>");
		builder.append("</form>");

		return builder.toString();
	}

	private Map<String, String> createEcpayData(String id, String name, String total, String desc, String date) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("MerchantID", MERCHANT_ID);
		parameters.put("MerchantTradeNo", id);
		parameters.put("ItemName", name);
		parameters.put("TotalAmount", total);
		parameters.put("TradeDesc", desc);
		parameters.put("MerchantTradeDate", date);
		parameters.put("PaymentType", "aio");
		parameters.put("ChoosePayment", "ALL");
		parameters.put("ReturnURL", RETURN_URL);

		String checkMacValue = genCheckMacValue(parameters, HASH_KEY, HASH_IV);
		parameters.put("CheckMacValue", checkMacValue);

		return parameters;
	}

	private String genCheckMacValue(Map<String, String> params, String hashKey, String hashIV) {
		Set<String> keySet = params.keySet();
		TreeSet<String> treeSet = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
		treeSet.addAll(keySet);
		String name[] = treeSet.toArray(new String[treeSet.size()]);
		String paramStr = "";
		for (int i = 0; i < name.length; i++) {
			if (!name[i].equals("CheckMacValue")) {
				paramStr += "&" + name[i] + "=" + params.get(name[i]);
			}
		}

		String urlEncode = urlEncode("Hashkey=" + hashKey + paramStr + "&HashIV=" + hashIV).toLowerCase();
		urlEncode = urlEncode.replaceAll("%21", "\\!").replaceAll("%28", "\\(").replaceAll("%29", "\\)");

		return hash(urlEncode.getBytes(), "SHA-256");
	}

	private static String urlEncode(String data) {
		String result = "";
		try {
			result = URLEncoder.encode(data, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	private String hash(byte data[], String mode) {
		MessageDigest md = null;
		try {
			if (mode == "MD5") {
				md = MessageDigest.getInstance("MD5");
			} else if (mode == "SHA-256") {
				md = MessageDigest.getInstance("SHA-256");
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return bytesToHex(md.digest(data));
	}

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
}
