package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.TogetherParticipantDTO;

public interface TogetherParticipantService {
    void create(TogetherParticipantDTO togetherParticipantDTO);
    TogetherParticipantDTO read(Integer id);
    void update(TogetherParticipantDTO togetherParticipantDTO);
    void delete(Integer id);

    void rejected(Integer id);
}
