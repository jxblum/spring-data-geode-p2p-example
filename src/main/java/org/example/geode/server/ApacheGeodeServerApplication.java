package org.example.geode.server;

import static java.lang.System.in;

import java.util.Scanner;

import org.apache.geode.cache.Cache;
import org.apache.geode.cache.CacheFactory;
import org.apache.geode.cache.RegionShortcut;
import org.apache.geode.distributed.internal.DistributionConfig;

import org.springframework.lang.NonNull;

/**
 * Apache Geode Server configured with the API
 *
 * @author John Blum
 * @see org.apache.geode.cache.Cache
 * @see org.apache.geode.cache.CacheFactory
 * @since 1.0.0
 */
public class ApacheGeodeServerApplication {

	public static void main(String[] args) {

		Cache peerCache = new CacheFactory().create();

		peerCache.createRegionFactory(RegionShortcut.REPLICATE)
			.create("ExampleReplicateRegion");

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
		Scanner scanner = new Scanner(in);
		scanner.nextLine();
	}
}
