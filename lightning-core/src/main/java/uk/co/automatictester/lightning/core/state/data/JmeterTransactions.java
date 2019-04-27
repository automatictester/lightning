package uk.co.automatictester.lightning.core.state.data;

import uk.co.automatictester.lightning.core.exceptions.CSVFileNonexistentLabelException;
import uk.co.automatictester.lightning.core.readers.JmeterBean;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.Math.min;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class JmeterTransactions {

    private static final int MAX_NUMBER_OF_LONGEST_TRANSACTIONS = 5;
    private List<JmeterBean> entries;

    private JmeterTransactions(List<JmeterBean> jmeterTransactions) {
        entries = new ArrayList<>(jmeterTransactions);
    }

    public static JmeterTransactions fromList(List<JmeterBean> entries) {
        return new JmeterTransactions(entries);
    }

    public JmeterTransactions transactionsWith(String expectedTransactionName) {
        Predicate<JmeterBean> isTransactionNameEqualToExpected = t -> t.getLabel().equals(expectedTransactionName);
        return transactions(isTransactionNameEqualToExpected, expectedTransactionName);
    }

    public JmeterTransactions transactionsMatching(String expectedTransactionName) {
        Pattern pattern = Pattern.compile(expectedTransactionName);
        Predicate<JmeterBean> isTransactionNameMatchingExpected = t -> pattern.matcher(t.getLabel()).matches();
        return transactions(isTransactionNameMatchingExpected, expectedTransactionName);
    }

    private JmeterTransactions transactions(Predicate<JmeterBean> predicate, String expectedTransactionName) {
        List<JmeterBean> transactions = entries.stream()
                .filter(predicate)
                .collect(collectingAndThen(toList(), filteredList -> validateAndReturn(filteredList, expectedTransactionName)));
        return JmeterTransactions.fromList(transactions);
    }

    public List<Integer> longestTransactions() {
        int numberOfLongestTransactions = min(entries.size(), MAX_NUMBER_OF_LONGEST_TRANSACTIONS);
        return entries.stream()
                .map(e -> e.getElapsed())
                .sorted(reverseOrder())
                .limit(numberOfLongestTransactions)
                .collect(toList());
    }

    public int failCount() {
        return (int) entries.stream()
                .filter(t -> !t.isSuccess())
                .count();
    }

    public int size() {
        return entries.size();
    }

    public Stream<JmeterBean> asStream() {
        return entries.stream();
    }

    public List<JmeterBean> asList() {
        return entries;
    }

    public long firstTransactionTimestamp() {
        return entries.stream()
                .mapToLong(e -> e.getTimeStamp())
                .sorted()
                .limit(1)
                .findFirst()
                .getAsLong();
    }

    public long lastTransactionTimestamp() {
        return entries.stream()
                .map(e -> e.getTimeStamp())
                .sorted(reverseOrder())
                .limit(1)
                .mapToLong(e -> e)
                .findFirst()
                .getAsLong();
    }

    private List<JmeterBean> validateAndReturn(List<JmeterBean> list, String expectedTransactionName) {
        if (list.size() == 0) {
            throw new CSVFileNonexistentLabelException(expectedTransactionName);
        }
        return list;
    }
}
