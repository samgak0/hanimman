package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.NoticeDTO;
import org.devkirby.hanimman.dto.NoticeFileDTO;
import org.devkirby.hanimman.entity.Notice;
import org.devkirby.hanimman.entity.NoticeFile;
import org.devkirby.hanimman.repository.NoticeFileRepository;
import org.devkirby.hanimman.repository.NoticeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeFileRepository noticeFileRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void create(NoticeDTO noticeDTO, NoticeFileDTO noticeFileDTO) {
        Notice notice = modelMapper.map(noticeDTO, Notice.class);
        NoticeFile noticeFile = modelMapper.map(noticeFileDTO, NoticeFile.class);
        noticeRepository.save(notice);
        noticeFile.setParent(notice);
        noticeFileRepository.save(noticeFile);
    }

    @Override
    public NoticeDTO read(Integer id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found with id: " + id));
        NoticeDTO noticeDTO = modelMapper.map(notice, NoticeDTO.class);
        List<NoticeFile> noticeFiles = noticeFileRepository.findByParentAndDeletedAtIsNull(notice);
        List<String> fileUrls = noticeFiles.stream()
                .map(NoticeFile::getServerName)
                .collect(Collectors.toList());
        noticeDTO.setFileUrls(fileUrls);
        return noticeDTO;
    }

    @Override
    @Transactional
    public void update(NoticeDTO noticeDTO) {
        noticeDTO.setModifiedAt(Instant.now());
        Notice notice = modelMapper.map(noticeDTO, Notice.class);
        noticeRepository.save(notice);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notice not found with id: " + id));
        notice.setDeletedAt(Instant.now());
        noticeRepository.save(notice);
    }

    @Override
    public Page<NoticeDTO> listAll(Pageable pageable) {
        return noticeRepository.findAll(pageable)
                .map(notice -> modelMapper.map(notice, NoticeDTO.class));
    }

    @Override
    public Page<NoticeDTO> searchByKeywords(String keyword, Pageable pageable) {
        return noticeRepository.findByTitleContainingOrContentContainingAndDeletedAtIsNull(keyword, keyword, pageable)
                .map(notice -> modelMapper.map(notice, NoticeDTO.class));
    }
}