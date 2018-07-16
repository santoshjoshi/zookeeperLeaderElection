package com.whiteboardtalks.election;

import java.util.function.Consumer;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Santosh Joshi
 *
 */

public class Leader {

	private final static Logger log = LoggerFactory.getLogger(Leader.class);
	private static String leaderSelectorPath = "/WBLeaderSelect";

	private LeaderListener leaderListener;

	public void init(String name, Consumer<Boolean> consumer) throws Exception {
		log.info("Leader Selection initiated");

		CuratorFramework client = Curator.curatorFramework();
		leaderListener = new LeaderListener(name, consumer, client, leaderSelectorPath);
		leaderListener.start();

	}
	
	public boolean isLeader() {
		return leaderListener.isLeader();
	}
}
