package com.whiteboardtalks.election;

import java.util.concurrent.TimeUnit;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Santosh Joshi
 */

public class Curator {

	private final static Logger log = LoggerFactory.getLogger(Curator.class);

	public void initCurator() {

	}

	public static CuratorFramework curatorFramework() throws Exception {

		String zkConnectionString = System.getProperty("zkHosts");
		log.info("Zk Connection String is {}", zkConnectionString);

		CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
		builder.connectString(zkConnectionString);
		CuratorFramework curator = builder.retryPolicy(exponentialBackoffRetry()).build();

		curator.start();
		curator.blockUntilConnected(10, TimeUnit.SECONDS);

		return curator;
	}

	public static RetryPolicy exponentialBackoffRetry() {
		return new ExponentialBackoffRetry(50, 10, 500);
	}

}
