//package tw.com.ispan.controller.pet;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
////用來測試redis正確啟用
//@RestController
//public class RedisTestController {
//
//	 @Autowired
//	    private RedisTemplate<String, String> redisTemplate;
//
//	    @GetMapping("/redis-test")
//	    public String testRedis() {
//	        redisTemplate.opsForValue().set("testKey", "testValue");
//	        return redisTemplate.opsForValue().get("testKey");
//	    }
//}
