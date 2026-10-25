package edu.psu.se411.lab12_threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab12_threads.model.CryptoAccount;
import edu.psu.se411.lab12_threads.model.Transaction;

/**
 * Lab 11 solution carried over: fixed thread pool. Run with
 * mvn exec:java -Dexec.args="&lt;nbAccounts&gt; &lt;nbTransactions&gt;"
 * (both optional, defaults below are used if omitted).
 */
public class App {

	static Logger logger = LoggerFactory.getLogger(App.class);

	public static Integer nbAccounts = 10;
	public static Double initialBalance = 10000.0;
	public static Integer nbTransactions = 100000;

	/**
	 * Fix: the starter sized the "fixed" pool to nbTransactions, i.e. it asked
	 * for as many threads as there are transactions (up to a million in the
	 * cases this lab asks us to test) -- that defeats the whole point of a
	 * FIXED pool and will crash with "unable to create new native thread"
	 * well before reaching a million. A fixed pool is meant to bound
	 * concurrency to a small, reusable set of worker threads; sizing it to
	 * the number of CPU cores is the standard choice.
	 */
	public static final int FIXED_POOL_SIZE = Runtime.getRuntime().availableProcessors();

	public static void main(String[] args) {
		if (args.length >= 1) {
			nbAccounts = Integer.parseInt(args[0]);
		}
		if (args.length >= 2) {
			nbTransactions = Integer.parseInt(args[1]);
		}

		logger.info("Application is starting... (fixed pool, nbAccounts={}, nbTransactions={})", nbAccounts,
				nbTransactions);

		// 1- Create accounts
		CryptoAccount[] accounts = new CryptoAccount[nbAccounts];
		for (Integer i = 0; i < nbAccounts; i++) {
			accounts[i] = new CryptoAccount("acc_" + i.toString(), initialBalance);
		}

		System.out.printf("[fixed pool] nbAccounts=%d nbTransactions=%d%n", nbAccounts, nbTransactions);
		System.out.printf("Initial money volume in system: %f%n%n", sumTotalMoneyInAccounts(accounts));

		ExecutorService executor = Executors.newFixedThreadPool(FIXED_POOL_SIZE);

		long startTime = System.currentTimeMillis();

		// 2- choose sender and receiver accounts randomly
		for (int i = 0; i < nbTransactions; i++) {
			int sender_index = (int) (Math.random() * nbAccounts);
			int receiver_index = (int) (Math.random() * nbAccounts);
			// make sure indexes are different
			if (sender_index == receiver_index) {
				sender_index = (sender_index + 1) % nbAccounts;
			}
			assert (sender_index != receiver_index); // verify assumption at dev time
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
		System.out.println(
				"Total time for multi-threading using FIXED thread pool (" + FIXED_POOL_SIZE + " threads): "
						+ (endTime - startTime) + " milliseconds");

		logger.info("Application is closing... (fixed pool, elapsed={} ms)", (endTime - startTime));
	}

	private static Double sumTotalMoneyInAccounts(CryptoAccount[] accounts) {
		Double total = 0.0;
		for (CryptoAccount c : accounts) {
			total += c.getBalance();
		}
		return total;
	}

}
