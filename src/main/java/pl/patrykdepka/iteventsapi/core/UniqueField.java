package pl.patrykdepka.iteventsapi.core;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueFieldValidator.class)
public @interface UniqueField {
    String message() default "{form.field.someField.error.uniqueValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
