package com.example.server.repository;

import com.example.server.cache.CacheStore;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository
public class TokenRepository {
    private final CacheStore<String> cacheStore = new CacheStore<>(180, TimeUnit.SECONDS);
    public String generateOTP(String email) {
        String token = UUID.randomUUID().toString();
        cacheStore.add(email, token);
        return token;
    }

    public String getState(String email) throws NullPointerException{
        return cacheStore.get(email);
    }
    public void delete(String email) {
        cacheStore.clear(email);
    }
}
