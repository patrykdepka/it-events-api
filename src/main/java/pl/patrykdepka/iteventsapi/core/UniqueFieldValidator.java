package pl.patrykdepka.iteventsapi.core;

import pl.patrykdepka.iteventsapi.appuser.service.AppUserService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UniqueFieldValidator implements ConstraintValidator<UniqueField, String> {
    private final AppUserService appUserService;

    public UniqueFieldValidator(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public void initialize(UniqueField constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String fieldValue, ConstraintValidatorContext constraintValidatorContext) {
        return !appUserService.checkIfUserExists(fieldValue);
    }
}
