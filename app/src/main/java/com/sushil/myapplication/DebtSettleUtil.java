package com.sushil.myapplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DebtSettleUtil {


    public static HashMap<String, Double> netPerMember(DatabaseHelper db, long groupId) {
        return db.calculateNetBalances(groupId);

    }


    public static List<String> minimalTransactions(HashMap<String,Double> net) {
        List<Map.Entry<String,Double>> creditors = new ArrayList<>();
        List<Map.Entry<String,Double>> debtors   = new ArrayList<>();

        for (Map.Entry<String,Double> e : net.entrySet()) {
            if (e.getValue() > 0.01)  creditors.add(e);
            else if (e.getValue() < -0.01) debtors.add(e);
        }

        int i=0,j=0; List<String> tx = new ArrayList<>();
        while (i < debtors.size() && j < creditors.size()) {
            Map.Entry<String,Double> d = debtors.get(i);
            Map.Entry<String,Double> c = creditors.get(j);

            double amt = Math.min(-d.getValue(), c.getValue());
            tx.add(d.getKey()+" pays ₹"+String.format("%.2f",amt)+" to "+c.getKey());

            d.setValue(d.getValue()+amt);
            c.setValue(c.getValue()-amt);
            if (Math.abs(d.getValue())<0.01) i++;
            if (Math.abs(c.getValue())<0.01) j++;
        }
        if (tx.isEmpty()) tx.add("Everyone is settled 🎉");
        return tx;
    }
}
