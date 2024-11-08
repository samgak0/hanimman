package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.InquiryDTO;
import org.devkirby.hanimman.entity.Inquiry;
import org.devkirby.hanimman.repository.InquiryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
    private final InquiryRepository inquiryRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(InquiryDTO inquiryDTO) {
        Inquiry inquiry = modelMapper.map(inquiryDTO, Inquiry.class);
        inquiryRepository.save(inquiry);
    }

    @Override
    public InquiryDTO read(Integer id) {
        Inquiry inquiry = inquiryRepository.findById(id).orElseThrow();
        return modelMapper.map(inquiry, InquiryDTO.class);
    }

    @Override
    public void update(InquiryDTO inquiryDTO) {
        inquiryDTO.setModifiedAt(Instant.now());
        Inquiry inquiry = modelMapper.map(inquiryDTO, Inquiry.class);
        inquiryRepository.save(inquiry);
    }

    @Override
    public void delete(Integer id) {
        Inquiry inquiry = inquiryRepository.findById(id).orElseThrow();
        inquiry.setDeletedAt(Instant.now());
        inquiryRepository.save(inquiry);
    }
}
