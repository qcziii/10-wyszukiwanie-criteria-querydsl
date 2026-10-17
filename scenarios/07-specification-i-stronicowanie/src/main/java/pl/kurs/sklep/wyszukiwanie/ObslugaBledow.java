package pl.kurs.sklep.wyszukiwanie;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class ObslugaBledow {

    @ExceptionHandler(NiedozwoloneSortowanieException.class)
    ProblemDetail niedozwoloneSortowanie(NiedozwoloneSortowanieException e) {
        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    }
}
