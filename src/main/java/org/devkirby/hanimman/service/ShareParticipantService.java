package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareParticipantDTO;

public interface ShareParticipantService {
    void create(ShareParticipantDTO shareParticipantDTO);
    ShareParticipantDTO read(Integer id);
    void update(ShareParticipantDTO shareParticipantDTO);
    void delete(Integer id);

    void rejected(Integer id);
}
