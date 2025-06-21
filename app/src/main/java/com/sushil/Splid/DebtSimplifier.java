package com.sushil.Splid;

import java.util.*;

public class DebtSimplifier {

    public static class Transaction {
        public String from;
        public String to;
        public double amount;

        public Transaction(String from, String to, double amount) {
            this.from = from;
            this.to = to;
            this.amount = amount;
        }
    }

    public static List<Transaction> simplifyDebts(Map<String, Double> balances) {
        List<Transaction> result = new ArrayList<>();

        PriorityQueue<Map.Entry<String, Double>> debtors = new PriorityQueue<>((a, b) -> Double.compare(a.getValue(), b.getValue()));
        PriorityQueue<Map.Entry<String, Double>> creditors = new PriorityQueue<>((a, b) -> Double.compare(b.getValue(), a.getValue()));

        for (Map.Entry<String, Double> entry : balances.entrySet()) {
            if (Math.abs(entry.getValue()) < 0.01) continue;
            if (entry.getValue() > 0) {
                creditors.offer(entry);
            } else {
                debtors.offer(entry);
            }
        }

        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            Map.Entry<String, Double> debtor = debtors.poll();
            Map.Entry<String, Double> creditor = creditors.poll();

            double amount = Math.min(-debtor.getValue(), creditor.getValue());
            result.add(new Transaction(debtor.getKey(), creditor.getKey(), amount));

            double newDebtorBalance = debtor.getValue() + amount;
            double newCreditorBalance = creditor.getValue() - amount;

            if (Math.abs(newDebtorBalance) > 0.01)
                debtors.offer(new AbstractMap.SimpleEntry<>(debtor.getKey(), newDebtorBalance));
            if (Math.abs(newCreditorBalance) > 0.01)
                creditors.offer(new AbstractMap.SimpleEntry<>(creditor.getKey(), newCreditorBalance));
        }

        return result;
    }
}
