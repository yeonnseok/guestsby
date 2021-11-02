package com.brtrip.recommend;

import java.util.Set;

public class AssociationRuleObject {

    private Set<String> antecedent;
    private Set<String> consequent;
    private Float support;
    private Float confidence;
    private Float lift;

    public AssociationRuleObject(Set<String> antecedent, Set<String> consequent, Float support,
                                 Float confidence, Float lift) {
        this.antecedent = antecedent;
        this.consequent = consequent;
        this.support = support;
        this.confidence = confidence;
        this.lift = lift;
    }

    public Set<String> getAntecedent() {
        return antecedent;
    }

    public Set<String> getConsequent() {
        return consequent;
    }

    public Float getSupport() {
        return support;
    }

    public Float getConfidence() {
        return confidence;
    }

    public Float getLift() {
        return lift;
    }

    @Override
    public String toString() {
        return "AssociationRuleObj{" +
                "antecedent=" + antecedent +
                ", consequent=" + consequent +
                ", support=" + support +
                ", confidence=" + confidence +
                ", lift=" + lift +
                '}';
    }
}
