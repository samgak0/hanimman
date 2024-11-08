package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.NoticeDTO;
import org.devkirby.hanimman.entity.Notice;
import org.devkirby.hanimman.repository.NoticeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(NoticeDTO noticeDTO) {
        Notice notice = modelMapper.map(noticeDTO, Notice.class);
        noticeRepository.save(notice);
    }

    @Override
    public NoticeDTO read(Integer id) {
        Notice notice = noticeRepository.findById(id).orElseThrow();
        return modelMapper.map(notice, NoticeDTO.class);
    }

    @Override
    public void update(NoticeDTO noticeDTO) {
        noticeDTO.setModifiedAt(Instant.now());
        Notice notice = modelMapper.map(noticeDTO, Notice.class);
        noticeRepository.save(notice);
    }

    @Override
    public void delete(Integer id) {
        Notice notice = noticeRepository.findById(id).orElseThrow();
        notice.setDeletedAt(Instant.now());
        noticeRepository.save(notice);
    }

    @Override
    public List<NoticeDTO> listAll() {
        return noticeRepository.findAll().stream()
                .map(notice -> modelMapper.map(notice, NoticeDTO.class))
                .collect(Collectors.toList());
    }
}
