package utils;

import org.apache.log4j.Logger;

import java.util.Random;

public class Util {

	private static final Logger log = Logger.getLogger(ClassNameUtil.getCurrentClassName());

	public static void sleep(long millis) {
		try {
			log.info("*Start TO* Wait " + millis + " milliseconds");
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static int getRandom(int size) {
		return (size == 0) ? size : new Random().nextInt(size - 1);
	}

}
