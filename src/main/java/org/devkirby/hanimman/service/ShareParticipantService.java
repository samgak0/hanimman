package org.devkirby.hanimman.service;

import org.devkirby.hanimman.dto.ShareParticipantDTO;

import java.util.List;

public interface ShareParticipantService {
    void create(ShareParticipantDTO shareParticipantDTO);
    ShareParticipantDTO read(Integer id);
    void update(ShareParticipantDTO shareParticipantDTO);
    void delete(Integer id);

    void rejected(Integer id);
    void accepted(Integer id);
    void complete(Integer id);

    List<ShareParticipantDTO> listAllByParentId(Integer parentId);

    List<ShareParticipantDTO> listAllByParentIdAndRejectedIsNull(Integer parentId);

    List<ShareParticipantDTO> listAllByUserId(Integer userId);
}
