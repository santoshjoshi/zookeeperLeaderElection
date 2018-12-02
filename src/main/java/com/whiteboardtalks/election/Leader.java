package com.whiteboardtalks.election;

import java.util.function.Consumer;

import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Santosh Joshi
 * 
 * Starts the Leader Election 
 * Connect
 *
 */

public class Leader {

	private final static Logger log = LoggerFactory.getLogger(Leader.class);
	
	//at this path the leader election will take place
	//Zookeeper will create and ephemeral sequential node under this path
	private static String leaderSelectorPath = "/WBLeaderSelect";

	private LeaderListener leaderListener;

	/**
	 * Initializes the Curator framework and  starts the leader election
	 * 
	 * @param name
	 * @param consumer
	 * @throws Exception
	 */
	public void init(String name, Consumer<Boolean> consumer) throws Exception {
		log.info("Leader Selection initiated");

		//Apache Library
		CuratorFramework client = Curator.curatorFramework();
		
		//Initialize the leadershiplistener
		leaderListener = new LeaderListener(name, consumer, client, leaderSelectorPath);
		leaderListener.start();

	}
	
	public boolean isLeader() {
		return leaderListener.isLeader();
	}
}
