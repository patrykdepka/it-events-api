package pl.patrykdepka.iteventsapi.appuser.domain;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final AppUserService appUserService;

    UniqueEmailValidator(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String fieldValue, ConstraintValidatorContext constraintValidatorContext) {
        if (fieldValue != null) {
            return !appUserService.checkIfUserExists(fieldValue);
        }

        return true;
    }
}
