package com.donghun.todo.web.controller;

import com.donghun.todo.service.FileService;
import com.donghun.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user/image")
@RequiredArgsConstructor
public class FileController {

    private final Logger logger = LoggerFactory.getLogger(FileController.class);

    private final FileService fileService;

    private final TodoService todoService;

    @GetMapping(value = "/{filename}")
    public ResponseEntity<?> getImage(@PathVariable String filename, HttpServletRequest request) {
        logger.info("Get Image API Accessed");

        return fileService.loadFileAsResponse(filename, request);
    }

    @PutMapping
    public ResponseEntity<?> putImage(@RequestParam("file") MultipartFile file,
                                      HttpServletRequest request) {
        logger.info("Put Image API Accessed");

        if (todoService.tokenCheck(request) != 3)
            return todoService.errorResponse(todoService.tokenCheck(request));

        return fileService.uploadFile(file, request);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteImage(HttpServletRequest request) {
        logger.info("Delete Image API Accessed");

        if (todoService.tokenCheck(request) != 3)
            return todoService.errorResponse(todoService.tokenCheck(request));

        return fileService.setDefaultImage(request);
    }



}
