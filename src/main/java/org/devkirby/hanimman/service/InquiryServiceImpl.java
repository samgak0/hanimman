package org.devkirby.hanimman.service;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.InquiryDTO;
import org.devkirby.hanimman.dto.InquiryFileDTO;
import org.devkirby.hanimman.entity.Inquiry;
import org.devkirby.hanimman.entity.InquiryFile;
import org.devkirby.hanimman.repository.InquiryFileRepository;
import org.devkirby.hanimman.repository.InquiryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
    private final InquiryRepository inquiryRepository;
    private final InquiryFileRepository inquiryFileRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(InquiryDTO inquiryDTO, InquiryFileDTO inquiryFileDTO) {
        Inquiry inquiry = modelMapper.map(inquiryDTO, Inquiry.class);
        InquiryFile inquiryFile = modelMapper.map(inquiryFileDTO, InquiryFile.class);
        inquiryRepository.save(inquiry);
        inquiryFile.setParent(inquiry);
        inquiryFileRepository.save(inquiryFile);
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

    @Override
    public Page<InquiryDTO> listAll(Pageable pageable) {
        return inquiryRepository.findAll(pageable)
                .map(inquiry -> modelMapper.map(inquiry, InquiryDTO.class));
    }

    @Override
    public Page<InquiryDTO> searchById(Integer id, Pageable pageable) {
        return inquiryRepository.findByIdAndDeletedAtIsNull(id, pageable)
                .map(inquiry -> modelMapper.map(inquiry, InquiryDTO.class));
    }
}
