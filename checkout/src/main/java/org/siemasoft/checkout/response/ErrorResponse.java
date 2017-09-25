package org.siemasoft.checkout.response;

import java.util.List;
import java.util.Optional;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ErrorResponse {

    public static final ErrorResponse NULL = ErrorResponse.builder().build();

    private String code;

    private String message;

    private int status;

    @Singular
    List<ErrorDetail> details;

    public HttpStatus resolveHttpStatus() {
        return Optional.of(status)
                       .filter(s -> s > 0)
                       .map(HttpStatus::valueOf)
                       .orElse(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
