package pl.patrykdepka.iteventsapi.appuser.annotation;

import pl.patrykdepka.iteventsapi.appuser.validator.PasswordValueMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValueMatchValidator.class)
public @interface PasswordValueMatch {

    String message() default "{form.field.confirmPassword.error.unmatchedPasswordValue.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field();

    String fieldMatch();

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface List {
        PasswordValueMatch[] value();
    }
}
