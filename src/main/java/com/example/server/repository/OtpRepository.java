package com.example.server.repository;

import com.example.server.cache.CacheStore;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.util.Random;
import java.util.concurrent.TimeUnit;
@Repository
public class OtpRepository {
    private final CacheStore<String> cacheStore = new CacheStore<>(180, TimeUnit.SECONDS);
    public String generateOTP(String email) {
        String otp= new DecimalFormat("000000").format(new Random().nextInt(999999));
        cacheStore.add(email, otp);
        return otp;
    }

    public String getState(String email) throws NullPointerException{
        return cacheStore.get(email);
    }
    public void delete(String email) {
        cacheStore.clear(email);
    }
}
