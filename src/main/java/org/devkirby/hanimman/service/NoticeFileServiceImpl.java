package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.NoticeFileDTO;
import org.devkirby.hanimman.entity.Notice;
import org.devkirby.hanimman.entity.NoticeFile;
import org.devkirby.hanimman.repository.NoticeFileRepository;
import org.devkirby.hanimman.repository.NoticeRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NoticeFileServiceImpl implements NoticeFileService {
    @Value("${com.devkirby.hanimman.upload}")
    String uploadDir;

    private final NoticeFileRepository noticeFileRepository;
    private final NoticeRepository noticeRepository;
    private final ModelMapper modelMapper;

    @Override
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
    public void update(NoticeFileDTO noticeFileDTO) {
        NoticeFile noticeFile = modelMapper.map(noticeFileDTO, NoticeFile.class);
        noticeFileRepository.save(noticeFile);
    }

    @Override
    public void delete(Integer id) {
        NoticeFile noticeFile = noticeFileRepository.findById(id).orElseThrow();
        noticeFile.setDeletedAt(Instant.now());
        noticeFileRepository.save(noticeFile);
    }

    @Override
    public String uploadFile(MultipartFile file, Integer noticeId) throws IOException {
        File uploadFile = new File(uploadDir);
        if(!uploadFile.exists()) {
            uploadFile.mkdirs();
        }

        String originalName = file.getOriginalFilename();
        String serverName = UUID.randomUUID().toString() + "_" + originalName;
        Path targetPath = Paths.get(uploadDir).resolve(serverName);

        try(InputStream inputStream = file.getInputStream()){
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            throw new IOException("공지 파일 업로드 실패!!!", e);
        }

        Notice notice = noticeRepository.findById(noticeId).orElseThrow();

        NoticeFile noticeFile = NoticeFile.builder()
                .originalName(originalName)
                .serverName(serverName)
                .serverName(serverName)
                .mineType(file.getContentType())
                .fileSize((int)file.getSize())
                .parent(notice)
                .user(notice.getUser())
                .build();

        noticeFileRepository.save(noticeFile);

        return "파일이 정상적으로 올라갔습니다. 파일이름 : " + serverName;
    }
}
