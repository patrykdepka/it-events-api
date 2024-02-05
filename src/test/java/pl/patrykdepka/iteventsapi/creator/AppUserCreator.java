package pl.patrykdepka.iteventsapi.creator;

import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;

import java.time.LocalDate;

import static pl.patrykdepka.iteventsapi.creator.ImageCreator.createDefaultProfileImage;

public class AppUserCreator {

    public static AppUser createUser() {
        return AppUser.builder()
                .id(3L)
                .uuid("e7992837-043c-4984-a3b0-eed743cf2eb0")
                .profileImage(createDefaultProfileImage())
                .firstName("User")
                .lastName("User")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .city("Rzeszów")
                .bio("Cześć! Jestem domyślnym użytkownikiem tego serwisu.")
                .password("{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe")
                .build();
    }

    public static AppUser createEventParticipant() {
        return AppUser.builder()
                .id(5L)
                .uuid("fb0228cd-1729-409d-9221-699fd2943fb2")
                .build();
    }

    public static AppUser createOrganizer() {
        return AppUser.builder()
                .id(2L)
                .uuid("48d28bdc-64b5-4415-9e29-9a86100aa22c")
                .profileImage(createDefaultProfileImage())
                .firstName("Organizer")
                .lastName("Organizer")
                .build();
    }

    public static AppUser createAdmin() {
        return AppUser.builder()
                .id(1L)
                .uuid("37ab2ef8-c644-44df-abe8-375009d957ac")
                .password("{bcrypt}$2a$10$nHSdx9AeVyj/KC9jqp9a8uiYy9Jr4lY/ILwMQJdshw98HBPn3mQhe")
                .build();
    }
}
