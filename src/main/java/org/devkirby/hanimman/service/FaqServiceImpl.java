package org.devkirby.hanimman.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.FaqDTO;
import org.devkirby.hanimman.entity.Faq;
import org.devkirby.hanimman.entity.FaqFile;
import org.devkirby.hanimman.repository.FaqFileRepository;
import org.devkirby.hanimman.repository.FaqRepository;
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
        if(faqDTO.getFiles() != null && !faqDTO.getFiles().isEmpty()){
            faqFileService.uploadFiles(faqDTO.getFiles(), faq.getId());
        }
    }

    @Override
    public FaqDTO read(Integer id) {
        Faq faq = faqRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("해당 ID의 자주 묻는 질문이 없습니다 : " + id));

        Integer view = faq.getViews() + 1;
        faq.setViews(view);
        faqRepository.save(faq);

        FaqDTO faqDTO = modelMapper.map(faq, FaqDTO.class);
        faqDTO.setImageIds(getFaqFiles(faq));

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
    @Transactional
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

    @Override
    public File downloadImage(Integer id) throws IOException {
        FaqFile faqFile = faqFileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 파일이 없습니다 : " + id));
        File file = new File("C:/upload/" + faqFile.getServerName());
        return file;
    }

    private List<Integer> getFaqFiles(Faq faq){
        List<Integer> imageIds = faqFileRepository.findByParent(faq)
                .stream()
                .map(faqFile -> faqFile.getId())
                .collect(Collectors.toList());
        return imageIds;
    }
}
