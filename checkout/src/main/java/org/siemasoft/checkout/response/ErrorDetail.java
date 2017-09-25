package org.siemasoft.checkout.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetail {

    private String code;

    private String field;

    private Object value;
}
