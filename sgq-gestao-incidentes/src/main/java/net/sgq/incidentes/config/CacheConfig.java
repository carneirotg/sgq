package net.sgq.incidentes.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class CacheConfig {

	@Bean
	public CacheManager cacheManager() {
		return new NoOpCacheManager();
	}
	
}
