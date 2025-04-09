package com.bubusyaka.demo.utils;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.concurrent.Callable;

public class TryUtils {

    public static <T> ResponseEntity<T> tryCatch(Callable<T> callable) {
        try {
            var result = callable.call();

            return ResponseEntity.ok(result);
        } catch (DateTimeParseException exception) {
            var problem = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), "Given dates are not valid");

            return ResponseEntity.of(problem).build();
        }
        catch (NumberFormatException exception) {
            var problem = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(400), "Given number parameters are not valid");

            return ResponseEntity.of(problem).build();
        } catch (Exception exception) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
