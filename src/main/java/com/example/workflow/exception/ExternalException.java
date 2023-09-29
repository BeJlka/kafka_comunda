package com.example.workflow.exception;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"stackTrace", "cause", "suppressed", "message", "localizedMessage"})
public class ExternalException extends RuntimeException {

    private String userId;

    public ExternalException(String userId, Throwable cause) {
        super(cause);

        this.userId = userId;
    }
}
