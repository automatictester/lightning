package uk.co.automatictester.lightning.data;

import uk.co.automatictester.lightning.exceptions.CSVFileNonexistentLabelException;
import uk.co.deliverymind.lightning.exceptions.CSVFileNonexistentLabelException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JMeterTransactions extends ArrayList<String[]> {

    public JMeterTransactions excludeLabelsOtherThan(String label) {
        JMeterTransactions transactions = new JMeterTransactions();
        for (String[] transaction : this) {
            if (transaction[0].equals(label)) {
                transactions.add(transaction);
            }
        }
        if (transactions.size() == 0) {
            throw new CSVFileNonexistentLabelException(label);
        }
        return transactions;
    }

    public JMeterTransactions excludeLabelsNotMatching(String labelPattern) {
        JMeterTransactions transactions = new JMeterTransactions();
        for (String[] transaction : this) {
            if (transaction[0].matches(labelPattern)) {
                transactions.add(transaction);
            }
        }
        if (transactions.size() == 0) {
            throw new CSVFileNonexistentLabelException(labelPattern);
        }
        return transactions;
    }

    public List<Integer> getLongestTransactions() {
        List<Integer> longestTransactions = new ArrayList<>();
        for (String[] transaction : this) {
            String elapsed = transaction[1];
            longestTransactions.add(Integer.parseInt(elapsed));
        }
        Collections.sort(longestTransactions);
        Collections.reverse(longestTransactions);
        if (longestTransactions.size() >= 5) {
            return longestTransactions.subList(0, 5);
        } else {
            return longestTransactions.subList(0, longestTransactions.size());
        }
    }

    public int getFailCount() {
        int failCount = 0;
        for (String[] transaction : this) {
            if ("false".equals(transaction[2])) {
                failCount++;
            }
        }
        return failCount;
    }

    public int getTransactionCount() {
        return this.size();
    }

    public double getThroughput() {
        double transactionTimespanInMilliseconds = getLastTransactionTimestamp() - getFirstTransactionTimestamp();
        return getTransactionCount() / (transactionTimespanInMilliseconds / 1000);
    }

    private long getFirstTransactionTimestamp() {
        long minTimestamp = 0;
        for (String[] transaction : this) {
            long currentTransactionTimestamp = Long.parseLong(transaction[3]);
            if (minTimestamp == 0 || currentTransactionTimestamp < minTimestamp) {
                minTimestamp = currentTransactionTimestamp;
            }
        }
        return minTimestamp;
    }

    private long getLastTransactionTimestamp() {
        long maxTimestamp = 0;
        for (String[] transaction : this) {
            long currentTransactionTimestamp = Long.parseLong(transaction[3]);
            if (maxTimestamp == 0 || currentTransactionTimestamp > maxTimestamp) {
                maxTimestamp = currentTransactionTimestamp;
            }
        }
        return maxTimestamp;
    }
}
