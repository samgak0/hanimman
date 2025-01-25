package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.InquiryFileDTO;
import org.devkirby.hanimman.entity.Inquiry;
import org.devkirby.hanimman.entity.InquiryFile;
import org.devkirby.hanimman.repository.InquiryFileRepository;
import org.devkirby.hanimman.repository.InquiryRepository;
import org.devkirby.hanimman.util.ImageUploadUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InquiryFileServiceImpl implements InquiryFileService {
    @Value("${com.devkirby.hanimman.upload}")
    String uploadDir;

    private final InquiryFileRepository inquiryFileRepository;
    private final InquiryRepository inquiryRepository;
    private final ModelMapper modelMapper;
    private final ImageUploadUtil imageUploadUtil;

    @Override
    @Transactional
    public void create(InquiryFileDTO inquiryFileDTO) {
        InquiryFile inquiryFile = modelMapper.map(inquiryFileDTO, InquiryFile.class);
        inquiryFileRepository.save(inquiryFile);
    }

    @Override
    public InquiryFileDTO read(Integer id) {
        InquiryFile result = inquiryFileRepository.findById(id).orElseThrow();
        InquiryFileDTO inquiryFileDTO = modelMapper.map(result, InquiryFileDTO.class);
        return inquiryFileDTO;
    }

    @Override
    @Transactional
    public void update(InquiryFileDTO inquiryFileDTO) {
        InquiryFile inquiryFile = modelMapper.map(inquiryFileDTO, InquiryFile.class);
        inquiryFileRepository.save(inquiryFile);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        InquiryFile inquiryFile = inquiryFileRepository.findById(id).orElseThrow();
        inquiryFile.setDeletedAt(Instant.now());
        inquiryFileRepository.delete(inquiryFile);
    }

    @Override
    @Transactional
    public String uploadFile(MultipartFile file, Integer inquiryId) throws IOException {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 1:1문의 ID 입니다 : " + inquiryId));
        String serverName = imageUploadUtil.uploadImage(file);

       InquiryFile inquiryFile = InquiryFile.builder()
               .originalName(file.getOriginalFilename())
               .serverName(serverName)
               .mineType(file.getContentType())
               .fileSize((int)file.getSize())
               .parent(inquiry)
               .user(inquiry.getUser())
               .build();

        inquiryFileRepository.save(inquiryFile);

        return serverName;
    }

    @Override
    @Transactional
    public List<String> uploadFiles(List<MultipartFile> files, Integer inquiryId) throws IOException {
        List<String> result = new ArrayList<>();
        for (MultipartFile file : files) {
            result.add(uploadFile(file, inquiryId));
        }
        return result;
    }
}
