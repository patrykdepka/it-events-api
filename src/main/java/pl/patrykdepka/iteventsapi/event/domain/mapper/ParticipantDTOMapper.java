package pl.patrykdepka.iteventsapi.event.domain.mapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.patrykdepka.iteventsapi.appuser.domain.AppUser;
import pl.patrykdepka.iteventsapi.event.domain.dto.ParticipantDTO;

import java.util.List;
import java.util.stream.Collectors;

public class ParticipantDTOMapper {

    public static Page<ParticipantDTO> mapToParticipantDTOs(List<AppUser> participants, Pageable pageable) {
        List<ParticipantDTO> participantsList = participants.stream()
                .map(ParticipantDTOMapper::mapToParticipantDTO)
                .collect(Collectors.toList());

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), participantsList.size());
        List<ParticipantDTO> pageContent = participantsList.subList(start, end);

        return new PageImpl<>(pageContent, pageable, participantsList.size());
    }

    private static ParticipantDTO mapToParticipantDTO(AppUser user) {
        return new ParticipantDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}
