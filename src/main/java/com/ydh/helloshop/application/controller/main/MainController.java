package com.ydh.helloshop.application.controller.main;

import com.ydh.helloshop.application.domain.member.CurrentMember;
import com.ydh.helloshop.application.domain.member.Member;
import com.ydh.helloshop.application.service.item.ItemServiceImpl;
import com.ydh.helloshop.infra.config.property.ImageProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainController {

    private final ItemServiceImpl itemService;

    private final ImageProperty imageProperty;

    @RequestMapping("/")
    public String home(Model model, @CurrentMember Member member) {
        model.addAttribute("member", member);

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "createTime"));
        model.addAttribute("items", itemService.findItemsWithPaging(pageRequest));
        return "home";
    }

    // TODO: 2021-05-18[양동혁] aws에서 uploadPath 설정
    @ResponseBody
    @PostMapping("/images/upload")
    public ResponseEntity<String> imageUpload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf('.'));
        String savedFilename = UUID.randomUUID() + extension;

        File savedFile = new File(imageProperty.getUploadPath() + "/" + savedFilename);
        try {
            file.transferTo(savedFile);
            return new ResponseEntity<>(imageProperty.getDownloadUrl() + "/" + savedFilename, HttpStatus.OK);
        } catch (IOException e) {
            log.error("파일 입력에서 에러가 발생했습니다.");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ResponseBody
    @GetMapping(value = "/images/download/{file}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> imageDownload(@PathVariable("file") String filename) {
        try {
            return new ResponseEntity<>(new FileInputStream(imageProperty.getUploadPath() + "/" + filename).readAllBytes(),
                    HttpStatus.OK);
        } catch (FileNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            log.error("파일 출력에서 에러가 발생했습니다.");
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
