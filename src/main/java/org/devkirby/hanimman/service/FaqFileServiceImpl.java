package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.FaqFileDTO;
import org.devkirby.hanimman.entity.Faq;
import org.devkirby.hanimman.entity.FaqFile;
import org.devkirby.hanimman.repository.FaqFileRepository;
import org.devkirby.hanimman.repository.FaqRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FaqFileServiceImpl implements FaqFileService {
    @Value("${com.devkirby.hanimman.upload}")
    String uploadDir;

    private final FaqFileRepository faqFileRepository;
    private final FaqRepository faqRepository;
    private final ModelMapper modelMapper;
    private final ImageUploadUtil imageUploadUtil;

    @Override
    @Transactional
    public void create(FaqFileDTO faqFileDTO) {
        FaqFile faqFile = modelMapper.map(faqFileDTO, FaqFile.class);
        faqFileRepository.save(faqFile);
    }

    @Override
    public FaqFileDTO read(Integer id) {
        FaqFile faqFile = faqFileRepository.findById(id).orElseThrow();
        return modelMapper.map(faqFile, FaqFileDTO.class);
    }

    @Override
    @Transactional
    public void update(FaqFileDTO faqFileDTO) {
        FaqFile faqFile = modelMapper.map(faqFileDTO, FaqFile.class);
        faqFileRepository.save(faqFile);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        FaqFile faqFile = faqFileRepository.findById(id).orElseThrow();
        faqFileRepository.delete(faqFile);
    }

    @Override
    @Transactional
    public String uploadFile(MultipartFile file, Integer faqId) throws IOException{
        String serverName = imageUploadUtil.uploadImage(file);

        Faq faq = faqRepository.findById(faqId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 FAQ ID 입니다 : " + faqId));

        FaqFile faqFile = FaqFile.builder()
                .serverName(serverName)
                .originalName(file.getOriginalFilename())
                .mineType(file.getContentType())
                .fileSize((int)file.getSize())
                .parent(faq)
                .user(faq.getUser())
                .build();
        faqFileRepository.save(faqFile);

        return serverName;
    }

    @Override
    @Transactional
    public List<String> uploadFiles(List<MultipartFile> files, Integer faqId) throws IOException {
        List<String> serverNames = new ArrayList<>();
        for(MultipartFile file : files){
            serverNames.add(uploadFile(file, faqId));
        }
        return serverNames;
    }

}
