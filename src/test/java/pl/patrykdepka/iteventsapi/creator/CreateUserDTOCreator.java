package pl.patrykdepka.iteventsapi.creator;

import pl.patrykdepka.iteventsapi.appuser.domain.dto.NewUserDTO;

import static liquibase.repackaged.org.apache.commons.lang3.StringUtils.stripAccents;

public class CreateUserDTOCreator {

    public static NewUserDTO createNewUserData(
            String firstName, String lastName, String dateOfBirth,
            String password, String confirmPassword
    ) {
        return NewUserDTO.builder()
                .firstName(firstName)
                .lastName(lastName)
                .dateOfBirth(dateOfBirth)
                .email(stripAccents(firstName + lastName + "@example.com").toLowerCase())
                .password(password)
                .confirmPassword(confirmPassword)
                .build();
    }
}
