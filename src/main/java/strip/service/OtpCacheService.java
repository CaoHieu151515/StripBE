package strip.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class OtpCacheService {

    @Autowired
    private CacheManager cacheManager;

    private final Logger log = LoggerFactory.getLogger(OtpCacheService.class);
    private static final String OTP_CACHE_NAME = "otpCache";

    // Lưu OTP vào cache với email làm key
    public void saveOtp(String email, String otp) {
        Cache cache = cacheManager.getCache(OTP_CACHE_NAME);
        if (cache != null) {
            cache.put(email, otp);
            log.info("Saved OTP '{}' for email '{}' in cache", otp, email);
        } else {
            log.warn("Cache '{}' not found!", OTP_CACHE_NAME);
        }
    }

    public String getOtp(String email) {
        Cache cache = cacheManager.getCache(OTP_CACHE_NAME);
        if (cache != null) {
            String otp = cache.get(email, String.class);
            log.info("Retrieved OTP '{}' for email '{}' from cache", otp, email);
            return otp;
        } else {
            log.warn("Cache '{}' not found!", OTP_CACHE_NAME);
            return null;
        }
    }

    public void removeOtp(String email) {
        Cache cache = cacheManager.getCache(OTP_CACHE_NAME);
        if (cache != null) {
            cache.evict(email);
            log.info("Removed OTP for email '{}' from cache", email);
        } else {
            log.warn("Cache '{}' not found!", OTP_CACHE_NAME);
        }
    }
}
