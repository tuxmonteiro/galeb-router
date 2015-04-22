package io.galeb.core.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class RuleTest {

    @Test
    public void newRuleInstance() {
        assertThat(new Rule()).isInstanceOf(Rule.class);
    }

}
