package com.bcnc_group_test.exceptions;

import com.bcnc_group_test.handler.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ControllerAdvice
public class ValidationExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException exception) {
        String errorMessage = String.format("The input data is invalid. Error: '%s'",exception.getMessage());
        return ResponseHandler.generateResponse(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> notValid(MethodArgumentTypeMismatchException exception, HttpServletRequest request) {
        MethodParameter methodParameter = exception.getParameter();
        String parameterName = methodParameter.getParameterName();
        String valueSent = request.getParameter(parameterName);
        Class<?> expectedType = methodParameter.getParameterType();

        String expectedFormat = getExpectedFormat(expectedType);
        String errorMessage = String.format("The value %s in the field %s is invalid. Expected format: %s",
            valueSent, parameterName, expectedFormat);

        return ResponseHandler.generateResponse(errorMessage, HttpStatus.BAD_REQUEST);
    }


    /**
     * This method returns the expected format based on the type of the input parameter.
     *
     * @param expectedType The type for which the expected format is needed.
     * @return The expected format as a String.
     */
    private String getExpectedFormat(Class<?> expectedType) {
        if (expectedType.equals(LocalDateTime.class)) {
            return "yyyy-MM-dd HH:mm:ss";
        } else if (expectedType.equals(LocalDate.class)) {
            return "yyyy-MM-dd";
        } else if (expectedType.equals(Integer.class) || expectedType.equals(int.class)) {
            return "an integer number";
        } else if (expectedType.equals(Double.class) || expectedType.equals(double.class)) {
            return "a decimal number";
        } else {
            return expectedType.getSimpleName();
        }
    }
}
