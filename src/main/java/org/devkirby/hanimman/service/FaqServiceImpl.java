package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.FaqDTO;
import org.devkirby.hanimman.entity.Faq;
import org.devkirby.hanimman.repository.FaqRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {
    private final FaqRepository faqRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(FaqDTO faqDTO) {
        Faq faq = modelMapper.map(faqDTO, Faq.class);
        faqRepository.save(faq);
    }

    @Override
    public FaqDTO read(Integer id) {
        Faq faq = faqRepository.findById(id).orElseThrow();
        return modelMapper.map(faq, FaqDTO.class);
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
    public List<FaqDTO> listAll() {
        return faqRepository.findAll().stream()
                .map(faq -> modelMapper.map(faq, FaqDTO.class))
                .collect(Collectors.toList());
    }

}
