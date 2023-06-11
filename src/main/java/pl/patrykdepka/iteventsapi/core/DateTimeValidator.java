package pl.patrykdepka.iteventsapi.core;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeValidator implements ConstraintValidator<DateTime, String> {
    private DateTime.ISO iso;

    @Override
    public void initialize(DateTime constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.iso = constraintAnnotation.iso();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null) {
            if (iso.equals(DateTime.ISO.DATE)) {
                try {
                    LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException ex) {
                    return false;
                }
            }

            if (iso.equals(DateTime.ISO.DATE_TIME)) {
                try {
                    LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                } catch (DateTimeParseException ex) {
                    return false;
                }
            }
        }

        return true;
    }
}
