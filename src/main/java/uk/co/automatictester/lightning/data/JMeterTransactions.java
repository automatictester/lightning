package uk.co.automatictester.lightning.data;

import uk.co.automatictester.lightning.exceptions.CSVFileNonexistentLabelException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JMeterTransactions extends ArrayList<ArrayList<String>> {

    public JMeterTransactions excludeLabelsOtherThan(String label) {
        JMeterTransactions transactions = new JMeterTransactions();
        for (ArrayList<String> transaction : this) {
            if (transaction.get(0).equals(label)) {
                transactions.add(transaction);
            }
        }
        if (transactions.size() == 0)
            throw new CSVFileNonexistentLabelException(label);
        return transactions;
    }

    public List<Integer> getLongestTransactions() {
        List<Integer> longestTransactions = new ArrayList<>();
        for (List<String> transaction : this) {
            String elapsed = transaction.get(1);
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
        for (ArrayList<String> transaction : this) {
            if (transaction.get(2).equals("false")) {
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
        for (ArrayList<String> transaction : this) {
            long currentTransactionTimestamp = Long.parseLong(transaction.get(3));
            if (minTimestamp == 0) {
                minTimestamp = currentTransactionTimestamp;
            } else if (currentTransactionTimestamp < minTimestamp) {
                minTimestamp = currentTransactionTimestamp;
            }
        }
        return minTimestamp;
    }

    private long getLastTransactionTimestamp() {
        long maxTimestamp = 0;
        for (ArrayList<String> transaction : this) {
            long currentTransactionTimestamp = Long.parseLong(transaction.get(3));
            if (maxTimestamp == 0) {
                maxTimestamp = currentTransactionTimestamp;
            } else if (currentTransactionTimestamp > maxTimestamp) {
                maxTimestamp = currentTransactionTimestamp;
            }
        }
        return maxTimestamp;
    }
}
