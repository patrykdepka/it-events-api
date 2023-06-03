package pl.patrykdepka.iteventsapi.core;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ImageValidator.class)
public @interface Image {

    int width() default 100;

    int height() default 100;

    String message() default "Invalid profile image";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
