package com.openvraas.core.model;

import java.util.HashSet;
import java.util.Set;

import com.google.gson.annotations.Expose;
import com.openvraas.core.json.JsonObject;

public class VirtualHost extends Entity {

    private static final long serialVersionUID = 1L;

    @Expose private Set<Rule> rules = new HashSet<>();

    public Rule getRule(String ruleId) {
        Rule rule = null;
        for (Rule ruleTemp: rules) {
            if (ruleId.equals(ruleTemp.getId())) {
                rule = ruleTemp;
                break;
            }
        }
        return rule;
    }

    public Rule newRule(String json) {
        Rule rule = (Rule) JsonObject.fromJson(json, Rule.class);
        addRule(rule);
        return rule;
    }

    public VirtualHost addRule(Rule rule) {
        rules.add(rule);
        return this;
    }

    public VirtualHost delRule(String ruleId) {
        Rule rule = getRule(ruleId);
        if (rule!=null) {
            rules.remove(rule);
        }
        return this;
    }

    public boolean containRule(String ruleId) {
        return getRule(ruleId) != null;
    }

    public void clearRules() {
        rules.clear();
    }

    public Set<Rule> getRules() {
        return rules;
    }

}
