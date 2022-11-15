package org.example.spring.geode.server;

import java.util.Scanner;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.distributed.internal.DistributionConfig;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.ReplicatedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.PeerCacheApplication;
import org.springframework.lang.NonNull;

/**
 * An Apache Geode {@link PeerCacheApplication} configured and bootstrapped with Spring.
 *
 * Run this application with:
 *
 * $ java -classpath ... org.example.spring.geode.server.SpringApacheGeodeServerApplication
 *     -Dspring.data.geode.cache.name=<NAME>
 *     -Dspring.data.geode.locators=localhost[11235],localhost[12480]
 *
 * @author John Blum
 * @see org.springframework.data.gemfire.config.annotation.PeerCacheApplication
 * @since 1.0.0
 */
@PeerCacheApplication
public class SpringApacheGeodeServerApplication {

	public static void main(String[] args) {

		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

		applicationContext.register(SpringApacheGeodeServerApplication.class);
		applicationContext.registerShutdownHook();
		applicationContext.refresh();

		Cache peerCache = applicationContext.getBean(Cache.class);

		block(peerCache);
	}

	private static void log(String message, Object... args) {
		System.out.printf(String.valueOf(message), args);
		System.out.flush();
	}

	private static void block(@NonNull Cache cache) {
		log("Peer Cache instance [%s] running and connected to cluster [%s]%n", cache.getName(),
			cache.getDistributedSystem().getProperties().getProperty(DistributionConfig.LOCATORS_NAME));
		log("Press <enter> to stop...%n");
		Scanner scanner = new Scanner(System.in);
		scanner.nextLine();
	}

	@Configuration
	static class ApacheGeodeConfiguration {

		@Bean("ExampleReplicateRegion")
		ReplicatedRegionFactoryBean<Object, Object> replicateRegion(GemFireCache gemfireCache) {

			ReplicatedRegionFactoryBean<Object, Object> replicateRegion = new ReplicatedRegionFactoryBean<>();

			replicateRegion.setCache(gemfireCache);
			replicateRegion.setPersistent(false);

			return replicateRegion;
		}
	}
}
