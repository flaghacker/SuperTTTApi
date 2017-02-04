package com.flaghacker.uttt.common;

import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Util
{
	private static int nextId = 0;
	private static Random random = new Random();
	private static int[] seeds = {};

	private static ScheduledExecutorService exec;

	public static Random loggedRandom()
	{
		int seed;
		if (nextId >= seeds.length)
		{
			seed = random.nextInt();
			System.err.println(String.format("random #%d seed: %d", nextId, seed));
		}
		else
		{
			seed = seeds[nextId];
			System.err.println(String.format("picked %d seed: %d", nextId, seed));
		}

		nextId++;
		return new Random(seed);
	}

	public static Coord moveBotWithTimeOut(final Bot bot, Board board, long time)
	{
		checkAndInitExecutor();

		final boolean[] runTimeUp = {true};
		exec.schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if (runTimeUp[0])
				{
					runTimeUp[0] = false;
					bot.timeUp();
				}
			}
		}, time, TimeUnit.MILLISECONDS);
		Coord move = bot.move(board);
		runTimeUp[0] = false;
		return move;
	}

	private static void checkAndInitExecutor()
	{
		if (exec != null)
			return;

		exec = new ScheduledThreadPoolExecutor(1, new ThreadFactory()
		{
			@Override
			public Thread newThread(Runnable runnable)
			{
				Thread thread = new Thread(runnable);
				thread.setDaemon(true);
				thread.setName("Util executor thread");
				return thread;
			}
		});
	}
}
