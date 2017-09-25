package org.siemasoft.checkout.response;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reactivex.Single;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientResponseException;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Response<T> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    T dto;

    Throwable throwable;

    ErrorResponse errorResponse;

    public static <T> Response success(T dto) {
        T validDto = Optional.ofNullable(dto)
                             .orElseThrow(() -> new IllegalArgumentException("dto cannot be null"));
        return new Response<>(validDto, null, null);
    }

    public static Response error(Throwable throwable) {
        ErrorResponse errorResponse = resolveErrorResponse(throwable);
        return new Response<>(null, throwable, errorResponse);
    }

    private static ErrorResponse resolveErrorResponse(Throwable throwable) {
        return Single.just(throwable)
                     .filter(exception -> exception instanceof RestClientResponseException)
                     .cast(RestClientResponseException.class)
                     .map(RestClientResponseException::getResponseBodyAsString)
                     .filter(errorResponseAsString -> !errorResponseAsString.isEmpty())
                     .map(errorResponseAsString -> OBJECT_MAPPER.readValue(errorResponseAsString, ErrorResponse.class))
                     .onErrorReturnItem(ErrorResponse.NULL)
                     .blockingGet();
    }

    public boolean isError() {
        return throwable != null;
    }

    public boolean hasErrorStatus(HttpStatus expectedStatus) {
        return throwable != null
                && !Single.just(throwable)
                          .filter(exception -> exception instanceof RestClientResponseException)
                          .cast(RestClientResponseException.class)
                          .map(RestClientResponseException::getRawStatusCode)
                          .filter(status -> status == expectedStatus.value())
                          .isEmpty()
                          .blockingGet();
    }
}