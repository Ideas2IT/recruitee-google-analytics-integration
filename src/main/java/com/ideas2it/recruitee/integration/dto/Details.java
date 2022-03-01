package com.ideas2it.recruitee.integration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Details {

    @JsonProperty(value = "disqualify_reason")
    private DisqualifyReason disqualifyReason;
    @JsonProperty(value = "from_stage")
    private FromStage fromStage;
    @JsonProperty(value = "to_stage")
    private ToStage toStage;

    public DisqualifyReason getDisqualifyReason() {
        return disqualifyReason;
    }

    public void setDisqualifyReason(DisqualifyReason disqualifyReason) {
        this.disqualifyReason = disqualifyReason;
    }

    public FromStage getFromStage() {
        return fromStage;
    }

    public void setFromStage(FromStage fromStage) {
        this.fromStage = fromStage;
    }

    public ToStage getToStage() {
        return toStage;
    }

    public void setToStage(ToStage toStage) {
        this.toStage = toStage;
    }
}
