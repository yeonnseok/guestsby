package com.brtrip.recommend;

import com.google.common.collect.Sets;
import java.util.*;

public class AssociationRule {

    private Map<Set<String>, Float> itemset;
    private String metric;
    private Float min_threshold;
    private List<AssociationRuleObject> rules;

    public AssociationRule(Map<Set<String>, Float> itemset, String metric, Float min_threshold) {
        this.itemset = itemset;
        this.metric = metric;
        this.min_threshold = min_threshold;
        this.rules = new LinkedList<>();
    }

    public List<AssociationRuleObject> getRules() {
        return rules;
    }

    private Float scoreByMetric(String metric, Float sAC, Float sA, Float sC) {
        Float score;
        if (metric.equals("support")) { score = getSupport(sAC); }
        else if (metric.equals("confidence")) { score = getConfidence(sAC, sA); }
        else if (metric.equals("lift")) { score = getLift(sAC, sA, sC); }
        else { score = null; }

        return score;
    }

    private Float getSupport(Float sAC) { return sAC; }

    private Float getConfidence(Float sAC, Float sA) { return sAC / sA; }

    private Float getLift(Float sAC, Float sA, Float sC) {
        return (sAC/sA) / sC;
    }

    private void filterItemset() {
        itemset.entrySet().removeIf(x -> x.getKey().size() > 2);
    }

    public void run() {
        filterItemset();

        for (Set<String> item : itemset.keySet()) {
            Float sAC = itemset.get(item);
            for (Integer idx = item.size()-1; idx > 0; idx--) {
                Set<Set<String>> combination = Sets.combinations(item, idx);
                for (Set<String> comb : combination) {
                    Set<String> antecedent  = comb;
                    Set<String> consequent = Sets.difference(item, comb);

                    Float sA = itemset.get(antecedent);
                    Float sC = itemset.get(consequent);
                    Float score = scoreByMetric(metric, sAC, sA, sC);
                    if (score >= min_threshold) {
                        rules.add(new AssociationRuleObject(antecedent, consequent, sAC, getConfidence(sAC, sA), getLift(sAC, sA, sC)));
                    }
                }
            }
        }
    }
}
