package com.itsilesia.si_learn.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.AllArgsConstructor;
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
	public static final String coursesModulesCache = "coursesModules";
	public static final String modulesLessonsCache = "modulesLessons";
	public static final String lessonsTasksCache = "lessonsTasks";
	public static final String coursesTasksCache = "coursesTasks";

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
		new CaffeineCacheManagerChainable(cacheManager, bigStorageTtl, bigStorageMaxSize, smallStorageTtl, smallStorageMaxSize)
			.addToSmallStorage(languagesCache)
			.addToSmallStorage(programingLanguageCompilersCache)

			.addToBigStorage(courseDictionaryIdsCache)
			.addToBigStorage(coursesModulesCache)
			.addToBigStorage(modulesLessonsCache)
			.addToBigStorage(lessonsTasksCache)
			.addToBigStorage(coursesTasksCache);
		return cacheManager;
	}

	@AllArgsConstructor
	private static class CaffeineCacheManagerChainable {
		private final CaffeineCacheManager cacheManager;
		private Duration bigStorageTtl;
		private long bigStorageMaxSize;
		private Duration smallStorageTtl;
		private long smallStorageMaxSize;

		public CaffeineCacheManagerChainable addToSmallStorage(String cacheName) {
			cacheManager.registerCustomCache(
				programingLanguageCompilersCache,
				Caffeine.newBuilder()
					.expireAfterWrite(smallStorageTtl)
					.maximumSize(smallStorageMaxSize)
					.build()
			);
			return this;
		}

		public CaffeineCacheManagerChainable addToBigStorage(String cacheName) {
			cacheManager.registerCustomCache(
				programingLanguageCompilersCache,
				Caffeine.newBuilder()
					.expireAfterWrite(bigStorageTtl)
					.maximumSize(bigStorageMaxSize)
					.build()
			);
			return this;
		}
	}

}
