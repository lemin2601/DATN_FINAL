package com.leemin.genealogy.control;

import com.leemin.genealogy.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@Controller
public class ResourceController {

    @Autowired
    StorageService storageService;

    @RequestMapping(value = "/image/{id:.+}", method = RequestMethod.GET,
                    produces = MediaType.IMAGE_JPEG_VALUE)
    public void getImage(HttpServletResponse response,
            @PathVariable("id")
                    String id) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("headshots/" + id)
                                        .getFile());
        System.out.println(file.getAbsolutePath());
        ClassPathResource imgFile = new ClassPathResource("/headshots/" + id);
        System.out.println(imgFile.getFile()
                                  .getAbsolutePath());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    }

    @RequestMapping(value = "/img/{id:.+}", method = RequestMethod.GET,
                    produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<InputStreamResource> getImage(
            @PathVariable("id")
                    String id) throws IOException {
        Resource resource = storageService.loadFile(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(new InputStreamResource(resource.getInputStream()));
    }
}
