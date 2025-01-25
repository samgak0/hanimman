package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.InquiryDTO;
import org.devkirby.hanimman.entity.Inquiry;
import org.devkirby.hanimman.entity.InquiryFile;
import org.devkirby.hanimman.repository.InquiryFileRepository;
import org.devkirby.hanimman.repository.InquiryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
    private final InquiryRepository inquiryRepository;
    private final InquiryFileRepository inquiryFileRepository;
    private final InquiryFileService inquiryFileService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public void create(InquiryDTO inquiryDTO) throws IOException {
        Inquiry inquiry = modelMapper.map(inquiryDTO, Inquiry.class);
        inquiryDTO.setId(inquiryRepository.save(inquiry).getId());
        if(inquiryDTO.getFiles() != null && !inquiryDTO.getFiles().isEmpty()){
            inquiryFileService.uploadFiles(inquiryDTO.getFiles(), inquiryDTO.getId());
        }
    }

    @Override
    public InquiryDTO read(Integer id) {
        Inquiry inquiry = inquiryRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 ID의 문의사항이 없습니다 : " + id));

        InquiryDTO inquiryDTO = modelMapper.map(inquiry, InquiryDTO.class);
        inquiryDTO.setImageIds(getInquiryFiles(inquiry));

        return inquiryDTO;
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

    @Override
    public File downloadImage(Integer id) throws IOException {
        InquiryFile inquiryFile = inquiryFileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 이미지 파일이 없습니다 : " + id));
        File file = new File("C:/upload/inquiry/" + inquiryFile.getServerName());
        
        if (!file.exists()) {
            throw new IOException("파일을 찾을 수 없습니다: " + file.getAbsolutePath());
        }
        
        return file;
    }

    private List<Integer> getInquiryFiles(Inquiry inquiry) {
        return inquiryFileRepository.findByParentAndDeletedAtIsNull(inquiry)
                .stream()
                .map(InquiryFile::getId)
                .collect(Collectors.toList());
    }
}
