package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.NoticeDTO;
import org.devkirby.hanimman.entity.Notice;
import org.devkirby.hanimman.entity.NoticeFile;
import org.devkirby.hanimman.repository.NoticeFileRepository;
import org.devkirby.hanimman.repository.NoticeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeFileRepository noticeFileRepository;
    private final NoticeFileService noticeFileService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void create(NoticeDTO noticeDTO) throws IOException {
        Notice notice = modelMapper.map(noticeDTO, Notice.class);
        noticeRepository.save(notice);
        if(noticeDTO.getFiles() != null && !noticeDTO.getFiles().isEmpty()){
            noticeFileService.uploadFiles(noticeDTO.getFiles(), notice.getId());
        }

    }

    @Override
    public NoticeDTO read(Integer id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항이 없습니다 : " + id));
        Integer view = notice.getViews() + 1;
        notice.setViews(view);
        noticeRepository.save(notice);

        NoticeDTO noticeDTO = modelMapper.map(notice, NoticeDTO.class);
        noticeDTO.setImageIds(getNoticeFiles(notice));

        return noticeDTO;
    }

    @Override
    @Transactional
    public void update(NoticeDTO noticeDTO) throws IOException {
        Notice existingNotice = noticeRepository.findById(noticeDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항이 없습니다 : " + noticeDTO.getId()));
        noticeFileService.deleteByParent(noticeDTO.getId());

        modelMapper.map(noticeDTO, existingNotice);
        noticeRepository.save(existingNotice);

        noticeFileService.uploadFiles(noticeDTO.getFiles(), noticeDTO.getId());
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 공지사항이 없습니다 : " + id));
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

    @Override
    @Transactional
    public File downloadImage(Integer id) throws IOException {
        NoticeFile noticeFile = noticeFileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 파일이 없습니다 : " + id));
        File file = new File("C:/upload/" + noticeFile.getServerName());
        return file;
    }

    private List<Integer> getNoticeFiles(Notice notice){
        List<Integer> imageIds = noticeFileRepository.findByParentAndDeletedAtIsNull(notice)
                .stream()
                .map(noticeFile -> noticeFile.getId())
                .collect(Collectors.toList());
        return imageIds;
    }
}