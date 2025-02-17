package tw.com.ispan.service.line;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StateRedisService {

	  @Autowired
	    private StringRedisTemplate redisTemplate;

	    // 儲存 state 到 Redis，並設置過期時間
	  public void saveState(String state) {
		    redisTemplate.opsForHash().put("stateMap", state, "valid"); // 存入 Hash
		    redisTemplate.expire("stateMap", 10, TimeUnit.MINUTES); // 設置整個 Hash 過期
		}
	    
	    //同時儲存state和memberId
	    public void saveStateWithMemberId(String state, Integer memberId) {
			try {
				//Redis 的鍵和值默認是字串： 使用 RedisTemplate 時，默認的序列化方式是 JdkSerializationRedisSerializer，這會將 Java 對象序列化為二進制格式。 當你存入一個 Integer 時，RedisTemplate 嘗試將它序列化為字串或二進制格式，但你的序列化配置可能不兼容
				redisTemplate.opsForHash().put("stateMap", state, String.valueOf(memberId));
				redisTemplate.expire("stateMap", 10, TimeUnit.MINUTES); //為整個哈希設置過期時間10分鐘
				System.out.println("保存member:" + memberId + "及state" + state);
			} catch (Exception e) {
				System.err.println("保存到 Redis 出現錯誤：" + e.getMessage());
				throw new RuntimeException("Redis 操作失敗", e);
			}
	    }
	    
	    public Integer getMemberIdByState(String state) {
	        // 從 Redis 取得 state 的值
	        Object value = redisTemplate.opsForHash().get("stateMap", state);

	        // 如果 state 不存在，直接返回 null
	        if (value == null) {
	            System.out.println("State " + state + " 不存在於 Redis");
	            return null;
	        }

	        // 如果 state 值是 "valid"，代表這是非會員狀態，返回 null
	        if ("valid".equals(value)) {
	            System.out.println("State " + state + " 為 'valid'，代表非會員，返回 null");
	            return null;
	        }

	        // 嘗試解析 memberId
	        try {
	            return Integer.parseInt(value.toString());  // 轉換為數字格式
	        } catch (NumberFormatException e) {
	            System.err.println("無法解析 state " + state + " 的值：" + value);
	            return null;
	        }
	    }


	    // 驗證 state 是否存在且有效
	    public boolean validateState(String state) {
	        Boolean exists = redisTemplate.opsForHash().hasKey("stateMap", state); // 檢查哈希結構中的鍵
	        System.out.println("要驗證的 state：" + state + " 是否存在於 stateMap：" + exists);
	        return exists != null && exists;
	    }

	    // 刪除 state（可選）
	    public void deleteState(String state) {
	        redisTemplate.delete(state);
	    }
}
