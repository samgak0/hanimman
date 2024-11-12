package org.devkirby.hanimman.service;

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

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {
    private final FaqRepository faqRepository;
    private final FaqFileRepository faqFileRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(FaqDTO faqDTO, FaqFileDTO faqFileDTO) {
        Faq faq = modelMapper.map(faqDTO, Faq.class);
        FaqFile faqFile = modelMapper.map(faqFileDTO, FaqFile.class);
        faqRepository.save(faq);
        faqFile.setParent(faq);
        faqFileRepository.save(faqFile);
    }

    @Override
    public FaqDTO read(Integer id) {
        Faq faq = faqRepository.findById(id).orElseThrow();
        FaqDTO faqDTO = modelMapper.map(faq, FaqDTO.class);
        List<FaqFile> faqFiles = faqFileRepository.findByParent(faq);
        List<String> fileUrls = faqFiles.stream()
                .map(FaqFile::getServerName)
                .collect(Collectors.toList());
        faqDTO.setFileUrls(fileUrls);
        return faqDTO;
    }

    @Override
    public void update(FaqDTO faqDTO) {
        faqDTO.setModifiedAt(Instant.now());
        Faq faq = modelMapper.map(faqDTO, Faq.class);
        faqRepository.save(faq);
    }

    @Override
    public void delete(Integer id) {
        Faq faq = faqRepository.findById(id).orElseThrow();
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
