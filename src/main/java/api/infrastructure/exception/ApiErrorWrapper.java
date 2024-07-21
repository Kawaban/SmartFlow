package api.infrastructure.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class ApiErrorWrapper {
    @JsonProperty("Error")
    private ApiError errorBody;

}
