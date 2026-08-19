package com.itsilesia.si_learn.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {
	public static final String languagesCache = "languages";
	public static final String courseDictionaryIdsCache = "courseDictionaryIds";
	public static final String programingLanguageCompilersCache = "programingLanguageCompilers";

	@Value("${app.cache.big-storage.ttl.ttl:10m}")
	private Duration bigStorageTtl;
	@Value("${app.cache.big-storage.max-size:100}")
	private long bigStorageMaxSize;

	@Value("${app.cache.small-storage.ttl:30m}")
	private Duration smallStorageTtl;
	@Value("${app.cache.small-storage.max-size:1000}")
	private long smallStorageMaxSize;

	@Bean
	public CacheManager cacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager();
		cacheManager.registerCustomCache(
			languagesCache,
			Caffeine.newBuilder()
				.expireAfterWrite(smallStorageTtl)
				.maximumSize(smallStorageMaxSize)
				.build()
		);
		cacheManager.registerCustomCache(
			programingLanguageCompilersCache,
			Caffeine.newBuilder()
				.expireAfterWrite(smallStorageTtl)
				.maximumSize(smallStorageMaxSize)
				.build()
		);

		cacheManager.registerCustomCache(
			courseDictionaryIdsCache,
			Caffeine.newBuilder()
				.expireAfterWrite(bigStorageTtl)
				.maximumSize(bigStorageMaxSize)
				.build()
		);
		return cacheManager;
	}
}
