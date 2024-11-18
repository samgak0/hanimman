package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.NoticeFileDTO;
import org.devkirby.hanimman.entity.Notice;
import org.devkirby.hanimman.entity.NoticeFile;
import org.devkirby.hanimman.repository.NoticeFileRepository;
import org.devkirby.hanimman.repository.NoticeRepository;
import org.devkirby.hanimman.util.ImageUploadUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoticeFileServiceImpl implements NoticeFileService {
    @Value("${com.devkirby.hanimman.upload}")
    String uploadDir;

    private final NoticeFileRepository noticeFileRepository;
    private final NoticeRepository noticeRepository;
    private final ImageUploadUtil imageUploadUtil;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void create(NoticeFileDTO noticeFileDTO) {
        NoticeFile noticeFile = modelMapper.map(noticeFileDTO, NoticeFile.class);
        NoticeFile result = noticeFileRepository.save(noticeFile);
    }

    @Override
    public NoticeFileDTO read(Integer id) {
        NoticeFile result = noticeFileRepository.findById(id).orElseThrow();
        NoticeFileDTO noticeFileDTO = modelMapper.map(result, NoticeFileDTO.class);
        return noticeFileDTO;
    }

    @Override
    @Transactional
    public void update(NoticeFileDTO noticeFileDTO) {
        NoticeFile noticeFile = modelMapper.map(noticeFileDTO, NoticeFile.class);
        noticeFileRepository.save(noticeFile);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        NoticeFile noticeFile = noticeFileRepository.findById(id).orElseThrow();
        noticeFile.setDeletedAt(Instant.now());
        noticeFileRepository.save(noticeFile);
    }

    @Override
    @Transactional
    public void deleteByParent(Integer noticeId) {
        List<NoticeFile> noticeFiles = noticeFileRepository.findAllByParentId(noticeId);
        for(NoticeFile noticeFile : noticeFiles){
            noticeFile.setDeletedAt(Instant.now());
            noticeFileRepository.save(noticeFile);
        }
    }

    @Override
    @Transactional
    public String uploadFile(MultipartFile file, Integer noticeId) throws IOException {
        String serverName = imageUploadUtil.uploadImage(file);
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(()->
                        new IllegalArgumentException("해당 ID의 공지사항이 없습니다 : " + noticeId));

        NoticeFile noticeFile = NoticeFile.builder()
                .originalName(file.getOriginalFilename())
                .serverName(serverName)
                .mineType(file.getContentType())
                .fileSize((int)file.getSize())
                .parent(notice)
                .user(notice.getUser())
                .build();

        noticeFileRepository.save(noticeFile);
        return serverName;
    }

    @Override
    @Transactional
    public List<String> uploadFiles(List<MultipartFile> files, Integer noticeId) throws IOException {
        List<String> result = new ArrayList<>();
        for(MultipartFile file : files){
            result.add(uploadFile(file, noticeId));
        }
        return result;
    }
}
