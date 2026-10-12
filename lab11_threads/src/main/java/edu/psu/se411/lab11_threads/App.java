package edu.psu.se411.lab11_threads;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.psu.se411.lab11_threads.model.CryptoAccount;
import edu.psu.se411.lab11_threads.model.Transaction;

/**
 * Build and run using Maven goal: clean package exec:java
 */
public class App {

	static Logger logger = LoggerFactory.getLogger(App.class);

	public static final int NB_ACCOUNTS = 10;
	public static final double INITIAL_BALANCE = 1000.0; // 10 accounts x 1000 = 10000 total
	public static final int NB_TRANSACTIONS = 10000;

	public static void main(String[] args) throws InterruptedException {
		logger.info("Application is starting...");

		System.out.println("=== Run 1: WITHOUT synchronization (double-spending expected) ===");
		runSimulation(false);

		System.out.println("\n=== Run 2: WITH synchronization (CryptoAccount.transfer is synchronized) ===");
		runSimulation(true);

		logger.info("Application is closing...");
	}

	private static void runSimulation(boolean synchronizedMode) throws InterruptedException {
		// 1- Create accounts
		List<CryptoAccount> accounts = new ArrayList<>();
		for (int i = 0; i < NB_ACCOUNTS; i++) {
			accounts.add(new CryptoAccount("acc_" + i, INITIAL_BALANCE));
		}

		// 2- build all ten thousand transactions/threads first
		List<Thread> threads = new ArrayList<>(NB_TRANSACTIONS);
		for (int i = 0; i < NB_TRANSACTIONS; i++) {
			CryptoAccount randomA = accounts.get((int) (Math.random() * NB_ACCOUNTS));
			CryptoAccount randomB = accounts.get((int) (Math.random() * NB_ACCOUNTS));
			if (randomA == randomB) {
				i--;
				continue;
			}
			Double randomAmount = (double) (Math.random() * 10 + 1);

			Transaction t = new Transaction(randomA, randomB, randomAmount, synchronizedMode);
			threads.add(new Thread(t));
		}

		// then start them all together so they genuinely run simultaneously,
		// maximizing (for run 1) or stress-testing (for run 2) contention
		for (Thread thread : threads) {
			thread.start();
		}

		// 3- wait for all threads to finish
		for (Thread thread : threads) {
			thread.join();
		}

		// 4- verify that we still have the same total balance in the monetary system
		double total = getTotalBalance(accounts);
		double expected = NB_ACCOUNTS * INITIAL_BALANCE;
		boolean consistent = Math.abs(total - expected) < 1e-6;

		System.out.printf("Total balance after %d transactions: %.6f (expected %.6f) -> %s%n",
				NB_TRANSACTIONS, total, expected, consistent ? "CONSISTENT" : "INCONSISTENT");

		if (!consistent) {
			logger.error("Double-spending detected (synchronizedMode={}): total balance drifted to {} (expected {})",
					synchronizedMode, total, expected);
		} else {
			logger.info("Total balance verified consistent (synchronizedMode={}): {}", synchronizedMode, total);
		}
	}

	public static double getTotalBalance(List<CryptoAccount> accounts) {
		double totalBalance = 0.0;
		for (CryptoAccount account : accounts) {
			totalBalance += account.getBalance();
		}
		return totalBalance;
	}
}
