package com.donghun.todo.service;

import com.donghun.todo.config.FileConfig;
import com.donghun.todo.config.exception.FileUploadException;
import com.donghun.todo.domain.User;
import com.donghun.todo.repository.UserRepository;
import com.donghun.todo.web.auth.JwtResolver;
import com.donghun.todo.web.dto.ErrorResponseDTO;
import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    private final Path fileLocation;

    private static final String FILE_PREFIX = "PROFILE_IMG_";

    private static final String DEFAULT_PROFILE_IMG = "USER_DEFAULT_PROFILE_IMG.png";

    private final UserRepository userRepository;

    private final JwtResolver jwtResolver;

    public FileService(FileConfig props, UserRepository userRepository, JwtResolver jwtResolver) {
        this.fileLocation = Paths.get(props.getUploadDir()).toAbsolutePath().normalize();
        this.userRepository = userRepository;
        this.jwtResolver = jwtResolver;

        try {
            Files.createDirectories(this.fileLocation);
        } catch (Exception e) {
            throw new FileUploadException("디렉토리 생성 실패", e);
        }
    }

    public ResponseEntity<?> loadFileAsResponse(String filename, HttpServletRequest request) {
        try {
            Path filePath = this.fileLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            ErrorResponseDTO response;

            if (resource.exists()) {
                String contentType;

                try {
                    contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
                } catch (IOException e) {
                    response = ErrorResponseDTO.builder().status("400").error("Bad Request")
                            .message("파일의 타입이 올바르지 않습니다.").build();

                    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                }

                if (contentType.isEmpty()) {
                    contentType = "application/octet-stream";
                }

                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.valueOf(contentType));

                return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
            } else {
                response = ErrorResponseDTO.builder().status("404").error("Not Found")
                        .message("파일을 찾을 수 없습니다.").build();

                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (MalformedURLException e) {
            ErrorResponseDTO response = ErrorResponseDTO.builder().status("404").error("Not Found")
                    .message("파일을 찾을 수 없습니다.").build();

            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> uploadFile(MultipartFile file, HttpServletRequest request) {
        String originFileName = StringUtils.cleanPath(file.getOriginalFilename());
        User user = userRepository.findByIdx(jwtResolver.getUserByToken(request));
        String originUrl = user.getImage();
        ErrorResponseDTO response;

        if (originFileName.contains("..")) {
            response = ErrorResponseDTO.builder().status("400").error("Bad Request")
                    .message("파일에 부적절한 문자가 포함되어 있습니다.").build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        String extension = FilenameUtils.getExtension(originFileName);
        String fileName = FILE_PREFIX + System.currentTimeMillis() + "." + extension;
        Path targetLocation = this.fileLocation.resolve(fileName);

        if (!"jpg".equals(extension) && !"jpeg".equals(extension) && !"png".equals(extension)) {
            response = ErrorResponseDTO.builder().status("400").error("Bad Request")
                    .message("지원하지 않는 확장자입니다.").build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            String fileDownloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/user/image/")
                    .path(fileName)
                    .toUriString();

            deleteOriginProfile(originUrl);
            user.setImage(fileDownloadUrl);
            userRepository.save(user);

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (IOException e) {
            response = ErrorResponseDTO.builder().status("400").error("Bad Request")
                    .message("파일 업로드에 실패하였습니다.").build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    private void deleteOriginProfile(String originUrl) {
        String filename = originUrl.substring(originUrl.lastIndexOf('/') + 1);

        if (filename.equals(DEFAULT_PROFILE_IMG))
            return;

        Path path = this.fileLocation.resolve(filename).normalize();

        try {
            Files.delete(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResponseEntity<?> setDefaultImage(HttpServletRequest request) {
        User user = userRepository.findByIdx(jwtResolver.getUserByToken(request));
        String originImgUrl = user.getImage();
        String defaultImageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/image/")
                .path(DEFAULT_PROFILE_IMG)
                .toUriString();

        deleteOriginProfile(originImgUrl);
        user.setImage(defaultImageUrl);
        userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
