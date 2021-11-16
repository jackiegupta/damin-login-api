package vn.vme.io.rest360;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AddRiceRequest {

    @JsonProperty("user_id")
    private Long userId;
    private int amount;
    private String provider;
    @JsonProperty("request_id")
    private String requestId;
}
