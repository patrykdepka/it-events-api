package pl.patrykdepka.iteventsapi.core;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DateTimeValidator.class)
public @interface DateTime {

    String message() default "{validation.annotation.DateTime.date.invalidFormat.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    ISO iso() default ISO.DATE;

    enum ISO {
        DATE,
        DATE_TIME
    }
}
