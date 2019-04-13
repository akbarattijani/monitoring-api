package model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * @author AKBAR <akbar.attijani@gmail.com>
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "biner"
})
public class SampleModel {
    @JsonProperty("id")
    private int id;

    @JsonProperty("biner")
    private String biner;

    @JsonProperty("id")
    public int getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(int id) {
        this.id = id;
    }

    @JsonProperty("biner")
    public String getBiner() {
        return biner;
    }

    @JsonProperty("biner")
    public void setBiner(String biner) {
        this.biner = biner;
    }
}
