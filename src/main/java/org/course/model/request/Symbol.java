package org.course.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.math.BigDecimal;

@JsonSerialize
public class Symbol {
    @JsonProperty("reward_multiplier")
    private BigDecimal rewardMultiplier;
    @JsonProperty("type")
    private String type;
    @JsonProperty("extra")
    private BigDecimal extra;
    @JsonProperty("impact")
    private String impact; //fixed,  changed to enum

    public BigDecimal getRewardMultiplier() {
        return rewardMultiplier;
    }

    public void setRewardMultiplier(BigDecimal rewardMultiplier) {
        this.rewardMultiplier = rewardMultiplier;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getExtra() {
        return extra;
    }

    public void setExtra(BigDecimal extra) {
        this.extra = extra;
    }

    public String getImpact() {
        return impact;
    }

    public void setImpact(String impact) {
        this.impact = impact;
    }
}
