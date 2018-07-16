package com.whiteboardtalks.election;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Leader election listener, whenever a new leader is elected takeLeadership
 * method takes over, leadership get revoked when connection state changes
 * 
 * 
 * @author Santosh Joshi
 *
 */
public class LeaderListener extends LeaderSelectorListenerAdapter implements Closeable {
	private final static Logger log = LoggerFactory.getLogger(LeaderListener.class);

	private final AtomicInteger leaderCount = new AtomicInteger();
	private LeaderSelector leaderSelector;
	// private CountDownLatch countDownLatch = new CountDownLatch(1);
	private Consumer<Boolean> consumer;
	private String name;

	public LeaderListener(String name, Consumer<Boolean> consumer, CuratorFramework client, String path) {
		this.consumer = consumer;
		this.name = name;
		leaderSelector = new LeaderSelector(client, path, this);
		leaderSelector.autoRequeue();
	}

	public void start() throws IOException {
		leaderSelector.start();
	}

	@Override
	public void close() throws IOException {
		leaderSelector.close();
	}
	
	public boolean isLeader() {
		if(leaderSelector != null) {
			return leaderSelector.hasLeadership();
		}
		
		return false;
	}

	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {
		if ((newState == ConnectionState.SUSPENDED) || (newState == ConnectionState.LOST)) {
			consumer.accept(false);
		}
	}

	@Override
	public void takeLeadership(CuratorFramework client) throws Exception {
		log.info("Leader is {} for {} time ", this.name, this.leaderCount.getAndIncrement());
		consumer.accept(true);
		// countDownLatch.await();
		// we are now the leader. This method should not return until we want to
		// relinquish leadership

		final int waitSeconds = (int) (10 * Math.random()) + 1;
		try {
			Thread.sleep(TimeUnit.SECONDS.toMillis(waitSeconds));
		} catch (InterruptedException e) {
			log.error(this.name + " was interrupted.");
			Thread.currentThread().interrupt();
		} finally {
			log.info("{} relinquishing leadership", this.name);
		}
	}
}