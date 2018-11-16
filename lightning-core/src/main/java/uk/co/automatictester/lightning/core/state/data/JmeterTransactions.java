package uk.co.automatictester.lightning.core.state.data;

import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentLabelException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Math.min;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;
import static uk.co.automatictester.lightning.core.enums.JmeterColumns.*;

public class JmeterTransactions {

    private static final int MAX_NUMBER_OF_LONGEST_TRANSACTIONS = 5;
    private List<String[]> entries;

    private JmeterTransactions(List<String[]> jmeterTransactions) {
        entries = new ArrayList<>(jmeterTransactions);
    }

    public static JmeterTransactions fromList(List<String[]> entries) {
        return new JmeterTransactions(entries);
    }

    public JmeterTransactions transactionsWith(String expectedTransactionName) {
        Predicate<String[]> isTransactionNameEqualToExpected = t -> t[TRANSACTION_LABEL.getColumn()].equals(expectedTransactionName);
        return transactions(isTransactionNameEqualToExpected, expectedTransactionName);
    }

    public JmeterTransactions transactionsMatching(String expectedTransactionName) {
        Pattern pattern = Pattern.compile(expectedTransactionName);
        Predicate<String[]> isTransactionNameMatchingExpected = t -> pattern.matcher(t[TRANSACTION_LABEL.getColumn()]).matches();
        return transactions(isTransactionNameMatchingExpected, expectedTransactionName);
    }

    private JmeterTransactions transactions(Predicate<String[]> predicate, String expectedTransactionName) {
        List<String[]> transactions = entries.stream()
                .filter(predicate)
                .collect(collectingAndThen(toList(), filteredList -> validateAndReturn(filteredList, expectedTransactionName)));
        return JmeterTransactions.fromList(transactions);
    }

    public List<Integer> longestTransactions() {
        int numberOfLongestTransactions = min(entries.size(), MAX_NUMBER_OF_LONGEST_TRANSACTIONS);
        return entries.stream()
                .map(e -> Integer.parseInt(e[TRANSACTION_DURATION.getColumn()]))
                .sorted(reverseOrder())
                .limit(numberOfLongestTransactions)
                .collect(toList());
    }

    public int failCount() {
        return (int) entries.stream()
                .filter(t -> "false".equals(t[TRANSACTION_RESULT.getColumn()]))
                .count();
    }

    public int size() {
        return entries.size();
    }

    public Stream<String[]> asStream() {
        return entries.stream();
    }

    public List<String[]> asList() {
        return entries;
    }

    public long firstTransactionTimestamp() {
        return entries.stream()
                .mapToLong(e -> Long.parseLong(e[TRANSACTION_TIMESTAMP.getColumn()]))
                .sorted()
                .limit(1)
                .findFirst()
                .getAsLong();
    }

    public long lastTransactionTimestamp() {
        return entries.stream()
                .map(e -> Long.parseLong(e[TRANSACTION_TIMESTAMP.getColumn()]))
                .sorted(reverseOrder())
                .limit(1)
                .mapToLong(e -> e)
                .findFirst()
                .getAsLong();
    }

    private List<String[]> validateAndReturn(List<String[]> list, String expectedTransactionName) {
        if (list.size() == 0) {
            throw new CSVFileNonexistentLabelException(expectedTransactionName);
        }
        return list;
    }
}
