package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.InquiryFileDTO;
import org.devkirby.hanimman.entity.Inquiry;
import org.devkirby.hanimman.entity.InquiryFile;
import org.devkirby.hanimman.repository.InquiryFileRepository;
import org.devkirby.hanimman.repository.InquiryRepository;
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
public class InquiryFileServiceImpl implements InquiryFileService {
    @Value("${com.devkirby.hanimman.upload}")
    String uploadDir;

    private final InquiryFileRepository inquiryFileRepository;
    private final InquiryRepository inquiryRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(InquiryFileDTO inquiryFileDTO) {
        InquiryFile inquiryFile = modelMapper.map(inquiryFileDTO, InquiryFile.class);
        InquiryFile result = inquiryFileRepository.save(inquiryFile);
    }

    @Override
    public InquiryFileDTO read(Integer id) {
        InquiryFile result = inquiryFileRepository.findById(id).orElseThrow();
        InquiryFileDTO inquiryFileDTO = modelMapper.map(result, InquiryFileDTO.class);
        return inquiryFileDTO;
    }

    @Override
    public void update(InquiryFileDTO inquiryFileDTO) {
        InquiryFile inquiryFile = modelMapper.map(inquiryFileDTO, InquiryFile.class);
        inquiryFileRepository.save(inquiryFile);
    }

    @Override
    public void delete(Integer id) {
        InquiryFile inquiryFile = inquiryFileRepository.findById(id).orElseThrow();
        inquiryFile.setDeletedAt(Instant.now());
        inquiryFileRepository.delete(inquiryFile);
    }

    @Override
    public String uploadFile(MultipartFile file, Integer inquiryId) throws IOException {
        File uploadFile= new File(uploadDir);
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

        Inquiry inquiry = inquiryRepository.findById(inquiryId).orElseThrow();

        InquiryFile inquiryFile = InquiryFile.builder()
                .originalName(originalName)
                .serverName(serverName)
                .mineType(file.getContentType())
                .fileSize((int)file.getSize())
                .parent(inquiry)
                .user(inquiry.getUser())
                .build();

        inquiryFileRepository.save(inquiryFile);

        return "파일이 정상적으로 올라갔습니다. 파일이름 : " + serverName;
    }
}
