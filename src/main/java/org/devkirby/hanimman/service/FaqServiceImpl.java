package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.FaqDTO;
import org.devkirby.hanimman.dto.FaqFileDTO;
import org.devkirby.hanimman.entity.Faq;
import org.devkirby.hanimman.entity.FaqFile;
import org.devkirby.hanimman.repository.FaqFileRepository;
import org.devkirby.hanimman.repository.FaqRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {
    private final FaqRepository faqRepository;
    private final FaqFileRepository faqFileRepository;
    private final FaqFileService faqFileService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void create(FaqDTO faqDTO) throws IOException {
        Faq faq = modelMapper.map(faqDTO, Faq.class);
        faqRepository.save(faq);
        faqFileService.uploadFiles(faqDTO.getFiles(), faq.getId());
    }

    @Override
    public FaqDTO read(Integer id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 ID의 자주 묻는 질문이 없습니다 : " + id));

        Integer view = faq.getViews() + 1;
        faq.setViews(view);
        faqRepository.save(faq);

        FaqDTO faqDTO = modelMapper.map(faq, FaqDTO.class);
        List<FaqFile> faqFiles = faqFileRepository.findByParent(faq);
        List<String> fileUrls = faqFiles.stream()
                .map(FaqFile::getServerName)
                .collect(Collectors.toList());
        faqDTO.setFileUrls(fileUrls);
        return faqDTO;
    }

    @Override
    @Transactional
    public void update(FaqDTO faqDTO) throws IOException {
        Faq existingFaq = faqRepository.findById(faqDTO.getId())
                .orElseThrow(()->
                        new IllegalArgumentException("해당 ID의 자주 묻는 질문이 없습니다 : " + faqDTO.getId()));
        faqFileRepository.deleteByParent(existingFaq);
        faqDTO.setModifiedAt(Instant.now());
        modelMapper.map(faqDTO, existingFaq);
        faqRepository.save(existingFaq);

        faqFileService.uploadFiles(faqDTO.getFiles(), faqDTO.getId());
    }

    @Override
    public void delete(Integer id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 ID의 자주 묻는 질문이 없습니다 : " + id));
        faq.setDeletedAt(Instant.now());
        faqRepository.save(faq);
    }

    @Override
    public Page<FaqDTO> listAll(Pageable pageable) {
        return faqRepository.findAll(pageable)
                .map(faq -> modelMapper.map(faq, FaqDTO.class));
    }

    @Override
    public Page<FaqDTO> searchByKeywords(String keyword, Pageable pageable) {
        return faqRepository.findByTitleContainingOrContentContainingAndDeletedAtIsNull(keyword, keyword, pageable)
                .map(faq -> modelMapper.map(faq, FaqDTO.class));
    }
}
