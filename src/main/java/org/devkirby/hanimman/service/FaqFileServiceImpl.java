package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.FaqFileDTO;
import org.devkirby.hanimman.entity.Faq;
import org.devkirby.hanimman.entity.FaqFile;
import org.devkirby.hanimman.repository.FaqFileRepository;
import org.devkirby.hanimman.repository.FaqRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FaqFileServiceImpl implements FaqFileService {
    @Value("${com.devkirby.hanimman.upload}")
    String uploadDir;

    private final FaqFileRepository faqFileRepository;
    private final FaqRepository faqRepository;
    private final ModelMapper modelMapper;

    @Override
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
    public void update(FaqFileDTO faqFileDTO) {
        FaqFile faqFile = modelMapper.map(faqFileDTO, FaqFile.class);
        faqFileRepository.save(faqFile);
    }

    @Override
    public void delete(Integer id) {
        FaqFile faqFile = faqFileRepository.findById(id).orElseThrow();
        faqFileRepository.delete(faqFile);
    }

    @Override
    public String uploadFile(MultipartFile file, Integer faqId) throws IOException{
        File uploadFile = new File(uploadDir);
        if (!uploadFile.exists()) {
            uploadFile.mkdirs();
        }

        String originalName = file.getOriginalFilename();
        String serverName = UUID.randomUUID().toString() + "_" + originalName;
        Path targetPath = Paths.get(uploadDir).resolve(serverName);

        try(InputStream inputStream = file.getInputStream()){
            Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e) {
            throw new IOException("FAQ 파일 업로드 실패!!!", e);
        }

        Faq faq = faqRepository.findById(faqId).orElseThrow();

        FaqFile faqFile = FaqFile.builder()
                .originalName(originalName)
                .serverName(serverName)
                .mineType(file.getContentType())
                .fileSize((int)file.getSize())
                .parent(faq)
                .user(faq.getUser())
                .build();

        return "파일이 정상적으로 올라갔습니다. 파일이름 : " + serverName;
    }

}
