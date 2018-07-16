package com.whiteboardtalks;

import java.util.Random;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.whiteboardtalks.election.Leader;

/**
 * 
 * @author Santosh Joshi
 *
 */
public class Main {

	private final static Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String... args) {

		System.setProperty("zkHosts", "localhost:2181");
		String name = getRandomName();

		log.info("leader election started, contender is {}", name);

		Leader leader = new Leader();
		try {
			leader.init(name, new Consumer<Boolean>() {

				@Override
				public void accept(Boolean isLeader) {

					log.info("Leader Election is Leader {} ", isLeader);
					if (isLeader) {
						startLeaderTask();
					} else {
						stopLeaderTask();
					}
				}
			});
		} catch (Exception e) {
			log.info("Exception initializing leader ", e);
		}

		try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected static void stopLeaderTask() {
		log.info("---Removed leader permission");
	}

	protected static void startLeaderTask() {
		log.info("---Elected as leader ");
	}

	public static String getRandomName() {

		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 10;
		Random random = new Random();
		StringBuilder buffer = new StringBuilder(targetStringLength);
		for (int i = 0; i < targetStringLength; i++) {
			int randomLimitedInt = leftLimit + (int) (random.nextFloat() * (rightLimit - leftLimit + 1));
			buffer.append((char) randomLimitedInt);
		}
		String name = buffer.toString();
		return name.toUpperCase();

	}
}
