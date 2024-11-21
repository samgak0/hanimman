package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.TogetherParticipantDTO;

import java.util.List;

public interface TogetherParticipantService {
    void create(TogetherParticipantDTO togetherParticipantDTO);
    TogetherParticipantDTO read(Integer id);
    void update(TogetherParticipantDTO togetherParticipantDTO);
    void delete(Integer id);

    void rejected(Integer id);

    List<TogetherParticipantDTO> listAllByParentId(Integer parentId);

    List<TogetherParticipantDTO> listAllByParentIdAndRejectedIsFalse(Integer parentId);
}
