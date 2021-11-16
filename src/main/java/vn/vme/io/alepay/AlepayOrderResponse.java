package vn.vme.io.alepay;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlepayOrderResponse {
    private String endpoint;
    @JsonProperty(value = "data")
    private AlepayOrderDataResponse alepayOrderDataResponse;
}
