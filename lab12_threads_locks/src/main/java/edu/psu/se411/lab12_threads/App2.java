package edu.psu.se411.lab12_threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab12_threads.model.CryptoAccount;
import edu.psu.se411.lab12_threads.model.Transaction;

/**
 * Lab 12, part 1: same simulation as App, but using a CACHED thread pool
 * instead of a fixed one. A cached pool creates a new thread whenever a
 * task arrives and no idle thread is available, and reuses idle threads
 * otherwise -- unlike a fixed pool, it is not bounded. Run with
 * mvn exec:java -Dexec.args="&lt;nbAccounts&gt; &lt;nbTransactions&gt;"
 * (same arguments as App, for an apples-to-apples comparison).
 */
public class App2 {

	static Logger logger = LoggerFactory.getLogger(App2.class);

	public static Integer nbAccounts = 10;
	public static Double initialBalance = 10000.0;
	public static Integer nbTransactions = 100000;

	public static void main(String[] args) {
		if (args.length >= 1) {
			nbAccounts = Integer.parseInt(args[0]);
		}
		if (args.length >= 2) {
			nbTransactions = Integer.parseInt(args[1]);
		}

		logger.info("Application is starting... (cached pool, nbAccounts={}, nbTransactions={})", nbAccounts,
				nbTransactions);

		// 1- Create accounts
		CryptoAccount[] accounts = new CryptoAccount[nbAccounts];
		for (Integer i = 0; i < nbAccounts; i++) {
			accounts[i] = new CryptoAccount("acc_" + i.toString(), initialBalance);
		}

		System.out.printf("[cached pool] nbAccounts=%d nbTransactions=%d%n", nbAccounts, nbTransactions);
		System.out.printf("Initial money volume in system: %f%n%n", sumTotalMoneyInAccounts(accounts));

		ExecutorService executor = Executors.newCachedThreadPool();

		long startTime = System.currentTimeMillis();

		// 2- choose sender and receiver accounts randomly
		for (int i = 0; i < nbTransactions; i++) {
			int sender_index = (int) (Math.random() * nbAccounts);
			int receiver_index = (int) (Math.random() * nbAccounts);
			if (sender_index == receiver_index) {
				sender_index = (sender_index + 1) % nbAccounts;
			}
			assert (sender_index != receiver_index);
			CryptoAccount sender = accounts[sender_index];
			CryptoAccount receiver = accounts[receiver_index];

			// 3- execute transaction
			Double amount = Math.random() * 100;
			Transaction t = new Transaction("id_" + i, sender, receiver, amount);
			executor.execute(t);
		}

		// 4- wait for all transactions to complete before verifying total money in system again
		executor.shutdown();
		while (!executor.isTerminated()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// 5- check total money in system after all transactions
		System.out.printf("Final money volume in system: %f%n%n", sumTotalMoneyInAccounts(accounts));

		long endTime = System.currentTimeMillis();
		System.out.println("Total time for multi-threading using CACHED thread pool: "
				+ (endTime - startTime) + " milliseconds");

		logger.info("Application is closing... (cached pool, elapsed={} ms)", (endTime - startTime));
	}

	private static Double sumTotalMoneyInAccounts(CryptoAccount[] accounts) {
		Double total = 0.0;
		for (CryptoAccount c : accounts) {
			total += c.getBalance();
		}
		return total;
	}

}
