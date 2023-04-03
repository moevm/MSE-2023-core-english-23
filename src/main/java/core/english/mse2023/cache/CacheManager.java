package core.english.mse2023.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.github.benmanes.caffeine.cache.RemovalCause;

import javax.annotation.Nonnull;

public class CacheManager {

    private final Cache<String, CacheData> cache;

    public CacheManager() {
        cache = createCache();
    }

    /**
     * This function gets cache's element to trigger time based eviction checker
     *
     * @param id - id of needed item from the cache
     */
    public void triggerTimeBasedEvictionChecker(String id) {
        cache.getIfPresent(id);
        cache.cleanUp();
    }

    public void cache(String userId, CacheData cacheData) {
        cache.put(userId, cacheData);
    }

    public CacheData getIfPresent(String userId) {
        return cache.getIfPresent(userId);
    }

    private Cache<String, CacheData> createCache() {
        return Caffeine.newBuilder()
                .expireAfter(createTimeExpireRule())
                .removalListener(((key, value, cause) -> {
                    if (cause == RemovalCause.EXPIRED) {
                        if (value != null) {
                            value.getHandler().removeFromCacheBy(key);
                        }
                    }
                }))
                .build();
    }

    private Expiry<String, CacheData> createTimeExpireRule() {
        return new Expiry<>() {

            // Expire time in nanoseconds
            //                      min   sec   ms      ns
            final long expireTime = 20L * 60 * 1000 * 1000000;

            @Override
            public long expireAfterCreate(@Nonnull String s, @Nonnull CacheData cacheData, long l) {
                return Long.MAX_VALUE;
            }

            @Override
            public long expireAfterUpdate(@Nonnull String s, @Nonnull CacheData cacheData, long l, long l1) {
                return Long.MAX_VALUE;
            }

            @Override
            public long expireAfterRead(@Nonnull String s, @Nonnull CacheData cacheData, long l, long l1) {

                if (!cacheData.getState().hasNext()) {
                    return 10;
                }
                return expireTime;
            }
        };
    }
}
