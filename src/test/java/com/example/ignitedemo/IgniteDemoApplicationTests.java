package com.example.ignitedemo;

import java.util.Collections;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.ClientConfiguration;
import org.apache.ignite.configuration.ClientConnectorConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = IgniteDemoApplication.class)
public class IgniteDemoApplicationTests {

	@ClassRule
	public static final IgniteFixedPortContainer igniteContainer1 = IgniteFixedPortContainer.newIgniteContainer();

	@ClassRule
	public static final IgniteContainer igniteContainer2 = IgniteContainer.newIgniteContainer();


	public static Ignite ignite;

	public static final String cacheName = "MyCache";

	@BeforeClass
	public static void beforeClass() {
		IgniteConfiguration cfg = new IgniteConfiguration();
		cfg.setIgniteInstanceName("IGNITE-client");
		cfg.setClientMode(true);
		TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
		ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
		cfg.setDiscoverySpi(new TcpDiscoverySpi().setIpFinder(ipFinder));

		CacheConfiguration<Integer, String> cacheCfg = new CacheConfiguration<>();

		ignite = Ignition.start(cfg);

		cacheCfg.setName(cacheName);
		cacheCfg.setAtomicityMode(CacheAtomicityMode.TRANSACTIONAL);
		// Create a cache with the given name if it does not exist.
		IgniteCache<Integer, String> cache = ignite.getOrCreateCache(cacheCfg);

		cache.put(1,"test");

		ignite.close();
	}

	@AfterClass
	public static void afterClass() {

	}

	@Test
	public void thinClientThroughNode() {
		ClientConnectorConfiguration clientConnectorCfg = new ClientConnectorConfiguration();
		IgniteConfiguration cfg = new IgniteConfiguration().setClientConnectorConfiguration(clientConnectorCfg);

		// Start a thin client
		Ignite ignite = Ignition.start(cfg);

		IgniteCache<Integer, String> cache = ignite.getOrCreateCache(cacheName);

		Assert.assertEquals("test", cache.get(1));
	}

	@Test
	public void thinClient() throws Exception {
		ClientConfiguration cfg = new ClientConfiguration().setAddresses(
				igniteContainer1.getContainerIpAddress() +":" + igniteContainer1.getMappedPort(10800));
		try (IgniteClient client = Ignition.startClient(cfg)) {
			ClientCache<Integer, String> cache = client.cache(cacheName);

			Assert.assertEquals("test", cache.get(1));
		}
	}

}
