package api.infrastructure.exception;

import jakarta.persistence.OptimisticLockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;

@RestControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiErrorWrapper> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        final String uri = extractRequestUri(request);
        final ApiError apiError = ApiError.builder()
                .path(uri)
                .statusCode(HttpStatus.NOT_FOUND.value())
                .message(HttpStatus.NOT_FOUND.getReasonPhrase())
                .status(HttpStatus.NOT_FOUND.getReasonPhrase())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiErrorWrapper(apiError));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ApiErrorWrapper> handleAllExceptions(Exception ex, WebRequest request) {
        final String uri = extractRequestUri(request);
        final ApiError apiError = ApiError.builder()
                .path(uri)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .message(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiErrorWrapper(apiError));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorWrapper> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        final String uri = extractRequestUri(request);
        final ApiError apiError = ApiError.builder()
                .path(uri)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .status(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiErrorWrapper(apiError));


    }

    @ExceptionHandler(OptimisticLockException.class)
    public ResponseEntity<ApiErrorWrapper> handleOptimisticLockException(OptimisticLockException ex, WebRequest request) {
        final String uri = extractRequestUri(request);
        final ApiError apiError = ApiError.builder()
                .path(uri)
                .statusCode(HttpStatus.CONFLICT.value())
                .status(HttpStatus.CONFLICT.getReasonPhrase())
                .message(HttpStatus.CONFLICT.getReasonPhrase())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiErrorWrapper(apiError));
    }

    private String extractRequestUri(WebRequest w) {
        if (w instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getRequest().getRequestURI();
        }

        return "Unknown URL";
    }
}
