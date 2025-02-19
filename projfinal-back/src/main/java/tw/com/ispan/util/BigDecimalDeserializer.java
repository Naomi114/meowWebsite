package tw.com.ispan.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalDeserializer extends JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        try {
            if (value == null || value.trim().isEmpty()) {
                return null; // ✅ 如果是空字串，返回 null，避免解析錯誤
            }
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            return null; // ✅ 如果格式錯誤，返回 null
        }
    }
}
