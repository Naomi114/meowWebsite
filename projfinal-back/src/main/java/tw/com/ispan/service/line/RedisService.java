package tw.com.ispan.service.line;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
		
	
	
	//以上為儲存line login state使用---------------------------------------------------------------------------------
	
	// 暫存memberId
	public void saveMemberId(String memberId) {
		//redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
		redisTemplate.opsForValue().set("LINE_BIND_" + memberId, "PENDING", 5, TimeUnit.MINUTES);
	}
	
	 // 查詢暫存的 memberId
    public String getMemberStatus(String memberId) {
        return redisTemplate.opsForValue().get("LINE_BIND_" + memberId);
    }

    // 綁定完成後刪除暫存
    public void deleteMemberId(String memberId) {
        redisTemplate.delete("LINE_BIND_" + memberId);
    }
	
	public String getMemberId(String lineId) {
	    return redisTemplate.opsForValue().get("LINE_BIND_" + lineId);
	}
}
