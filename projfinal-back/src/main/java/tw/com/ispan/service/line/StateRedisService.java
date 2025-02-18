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
	        redisTemplate.opsForValue().set(state, "valid", 10, TimeUnit.MINUTES); // 10 分鐘過期
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
	    
	    //透過state找到memberId
	    public Integer getMemberIdByState(String state) {
	        Object value = redisTemplate.opsForHash().get("stateMap", state);
			if (value instanceof Integer) {
				Integer memberId = (Integer) value;
				return memberId;
			} else if (value instanceof String) {
				Integer memberId = Integer.parseInt((String) value);
				return memberId;
			}
			return null;
	    }

	    // 驗證 state 是否存在且有效
	    public boolean validateState(String state) {
	        Boolean exists = redisTemplate.opsForHash().hasKey("stateMap", state); // 檢查哈希結構中的鍵
			System.out.println("要驗證囉" + state);
	        return exists != null && exists;
	    }

	    // 刪除 state（可選）
	    public void deleteState(String state) {
	        redisTemplate.delete(state);
	    }
}
