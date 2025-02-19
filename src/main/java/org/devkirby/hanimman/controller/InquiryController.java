package org.devkirby.hanimman.controller;

import lombok.RequiredArgsConstructor;
import org.devkirby.hanimman.dto.InquiryDTO;
import org.devkirby.hanimman.entity.User;
import org.devkirby.hanimman.service.InquiryService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/inquiry")
@RequiredArgsConstructor
public class InquiryController {
    private final InquiryService inquiryService;

    private static final String ERROR_TITLE_LENGTH = "제목의 길이는 1자 이상, 255자 이하여야 합니다. 현재 길이: %d";
    private static final String ERROR_CONTENT_LENGTH = "내용의 길이는 65535자 이하여야 합니다. 현재 길이: %d";
    private static final String ERROR_IMAGE_LIMIT = "이미지는 최대 10개까지 업로드할 수 있습니다. 현재 이미지 개수: %d";
    private static final String ERROR_UNAUTHORIZED_DELETE = "본인이 작성한 문의만 삭제할 수 있습니다.";

    private static final String MSG_SUCCESS_CREATE = "1:1 문의 작성에 성공했습니다.";
    private static final String MSG_SUCCESS_UPDATE = "1:1 문의 수정에 성공했습니다.";
    private static final String MSG_SUCCESS_DELETE = "1:1 문의 삭제에 성공했습니다.";

    /**
     * 1:1 문의 작성
     * 
     * @param inquiryDTO 1:1 문의 정보
     * @param files      첨부파일
     * @param loginUser  로그인 유저
     * @return 작성 결과
     * @throws IOException 파일 처리 예외
     */
    @PostMapping("/create")
    public Map<String, Object> createInquiry(@RequestPart("inquiryDTO") InquiryDTO inquiryDTO,
            @RequestPart(name = "files", required = false) List<MultipartFile> files,
            @AuthenticationPrincipal User loginUser) throws IOException {
        validateInquiry(inquiryDTO, files);
        if (files != null && !files.isEmpty()) {
            inquiryDTO.setFiles(files);
        }
        inquiryService.create(inquiryDTO);
        return createResponse(200, MSG_SUCCESS_CREATE);
    }

    /**
     * 1:1 문의 조회
     * 
     * @param id 1:1 문의 아이디
     * @return 1:1 문의 정보
     */
    @GetMapping("/{id}")
    public InquiryDTO readInquiry(@PathVariable Integer id) {
        return inquiryService.read(id);
    }

    /**
     * 1:1 문의 수정
     * 
     * @param id         1:1 문의 아이디
     * @param inquiryDTO 1:1 문의 정보
     * @param loginUser  로그인 유저
     * @return 수정 결과
     * @throws IOException 파일 처리 예외
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateInquiry(@PathVariable Integer id, @RequestBody InquiryDTO inquiryDTO,
            @AuthenticationPrincipal User loginUser) throws IOException {
        inquiryDTO.setId(id);
        validateInquiry(inquiryDTO, inquiryDTO.getFiles());
        inquiryDTO.setUserId(loginUser.getId());
        inquiryService.create(inquiryDTO);
        return createResponse(200, MSG_SUCCESS_UPDATE);
    }

    /**
     * 1:1 문의 삭제
     * 
     * @param id        1:1 문의 아이디
     * @param loginUser 로그인 유저
     * @return 삭제 결과
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteInquiry(@PathVariable Integer id, @AuthenticationPrincipal User loginUser) {
        if (!loginUser.getId().equals(inquiryService.read(id).getUserId())) {
            throw new IllegalArgumentException(ERROR_UNAUTHORIZED_DELETE);
        }
        inquiryService.delete(id);
        return createResponse(200, MSG_SUCCESS_DELETE);
    }

    /**
     * 1:1 문의 목록 조회
     * 
     * @param pageable 페이지 정보
     * @return 1:1 문의 목록
     */
    @GetMapping("/list")
    public Page<InquiryDTO> listAllInquiries(@PageableDefault(size = 10) Pageable pageable) {
        return inquiryService.listAll(pageable);
    }

    /**
     * 1:1 문의 검색
     * 
     * @param id       1:1 문의 아이디
     * @param pageable 페이지 정보
     * @return 1:1 문의 목록
     */
    @GetMapping("/search")
    public Page<InquiryDTO> searchInquiries(@RequestParam Integer id, @PageableDefault(size = 10) Pageable pageable) {
        return inquiryService.searchById(id, pageable);
    }

    /**
     * 1:1 문의 첨부파일 다운로드
     * 
     * @param id 1:1 문의 아이디
     * @return 첨부파일
     * @throws Exception 파일 처리 예외
     */
    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam Integer id) throws Exception {
        File file = inquiryService.downloadImage(id);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        return ResponseEntity.ok()
                .header("content-disposition",
                        "filename=" + URLEncoder.encode(file.getName(), "utf-8"))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }

    /**
     * 1:1 문의 유효성 검사
     * 
     * @param inquiryDTO 1:1 문의 정보
     * @param files      첨부파일
     */
    private void validateInquiry(InquiryDTO inquiryDTO, List<MultipartFile> files) {
        if (inquiryDTO.getTitle().length() > 255 || inquiryDTO.getTitle().isEmpty()) {
            throw new IllegalArgumentException(String.format(ERROR_TITLE_LENGTH, inquiryDTO.getTitle().length()));
        } else if (inquiryDTO.getContent().length() > 65535) {
            throw new IllegalArgumentException(String.format(ERROR_CONTENT_LENGTH, inquiryDTO.getContent().length()));
        } else if (files != null && files.size() > 10) {
            throw new IllegalArgumentException(String.format(ERROR_IMAGE_LIMIT, files.size()));
        }
    }

    private Map<String, Object> createResponse(int code, String msg) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("msg", msg);
        return map;
    }
}
