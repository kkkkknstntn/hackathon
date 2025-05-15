package org.ru.backend.exception;

import feign.FeignException;
import org.ru.backend.enums.BusinessErrorCodes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ExceptionResponse> handleApiException(ApiException ex) {
        BusinessErrorCodes errorCode = ex.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatus())
                .body(new ExceptionResponse(
                        errorCode.name(),
                        errorCode.getDescription(),
                        ex.getMessage(),
                        errorCode.getCode()
                ));
    }

    @ExceptionHandler({LockedException.class})
    public ResponseEntity<ExceptionResponse> handleLockedException(LockedException ex) {
        BusinessErrorCodes code = BusinessErrorCodes.ACCOUNT_LOCKED;
        return buildErrorResponse(code, ex.getMessage());
    }
    @ExceptionHandler({DisabledException.class})
    public ResponseEntity<ExceptionResponse> handleDisabledException(DisabledException ex) {
        BusinessErrorCodes code = BusinessErrorCodes.ACCOUNT_DISABLED;
        return buildErrorResponse(code, ex.getMessage());
    }

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException ex) {
        BusinessErrorCodes code = BusinessErrorCodes.BAD_CREDENTIALS;
        return buildErrorResponse(code, ex.getMessage());
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex) {
        return buildErrorResponse(
                BusinessErrorCodes.INTERNAL_SERVER_ERROR,
                "Internal server error: " + ex.getMessage()
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BusinessErrorCodes code = BusinessErrorCodes.VALIDATION_ERROR;
        StringBuilder message = new StringBuilder("Validation failed for argument: ");
        ex.getBindingResult().getAllErrors().forEach(error -> {
            message.append(error.getDefaultMessage()).append("; ");
        });
        return buildErrorResponse(code, message.toString());
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ExceptionResponse> handleFeignException(FeignException ex) {
        String errorMessage = ex.getMessage();
        String responseBody = ex.contentUTF8();
        int statusCode = ex.status();

        BusinessErrorCodes code = BusinessErrorCodes.EXTERNAL_SERVICE_ERROR;

        String errorDetails = "Feign exception: " + errorMessage + ". Response: " + responseBody;

        return buildErrorResponse(code, errorDetails);
    }

    private ResponseEntity<ExceptionResponse> buildErrorResponse(BusinessErrorCodes code, String message) {
        return ResponseEntity.status(code.getHttpStatus())
                .body(new ExceptionResponse(
                        code.name(),
                        code.getDescription(),
                        message,
                        code.getCode()
                ));
    }

}