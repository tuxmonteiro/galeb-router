package io.galeb.core.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

public class VirtualHostTest {

    VirtualHost virtualHost;
    Rule rule;
    Rule nullRule = null;
    String ruleId = "/";
    String ruleIdAsJson = String.format("{'id':'%s'}", ruleId);
    String ruleId2AsJson = "{'id':'/test'}";

    @Before
    public void setUp() {
        virtualHost = new VirtualHost();
        rule = (Rule) new Rule().setId(ruleId);
    }

    @Test
    public void getRulesAtVirtualHost() {
        assertThat(virtualHost.getRules()).isEmpty();
    }

    @Test
    public void clearRulesAtVirtualHost() {
        virtualHost.newRule(ruleIdAsJson);
        assertThat(virtualHost.getRules()).hasSize(1);
        virtualHost.clearRules();
        assertThat(virtualHost.getRules()).isEmpty();
    }

    @Test
    public void containRuleIsFalseInDefaultRulesAtVirtualHost() {
        assertThat(virtualHost.containRule(ruleId)).isFalse();
    }

    @Test
    public void containRuleIsTrueAfterAddRuleAtVirtualHost() {
        virtualHost.newRule(ruleIdAsJson);
        assertThat(virtualHost.containRule(ruleId)).isTrue();
    }

    @Test
    public void delRuleAtVirtualHost() {
        virtualHost.newRule(ruleIdAsJson);
        assertThat(virtualHost.getRules()).hasSize(1);
        virtualHost.delRule(ruleId);
        assertThat(virtualHost.getRules()).isEmpty();
    }

    @Test
    public void delNullRuleAtVirtualHost() {
        virtualHost.newRule(ruleIdAsJson);
        assertThat(virtualHost.getRules()).hasSize(1);
        virtualHost.delRule(ruleId2AsJson);
        assertThat(virtualHost.getRules()).hasSize(1);
    }

    @Test
    public void getSingleRuleAtVirtualHost() {
        virtualHost.newRule(ruleIdAsJson);
        virtualHost.newRule(ruleId2AsJson);
        assertThat(virtualHost.getRule(ruleId)).isInstanceOf(Rule.class);
    }

}
