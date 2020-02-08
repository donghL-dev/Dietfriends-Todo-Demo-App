package com.donghun.todo.service;

import com.donghun.todo.domain.Token;
import com.donghun.todo.domain.User;
import com.donghun.todo.repository.TokenRepository;
import com.donghun.todo.repository.UserRepository;
import com.donghun.todo.service.commons.BaseService;
import com.donghun.todo.web.auth.JwtResolver;
import com.donghun.todo.web.auth.SecurityConstants;
import com.donghun.todo.web.dto.DefaultSuccessDTO;
import com.donghun.todo.web.dto.ErrorResponseDTO;
import com.donghun.todo.web.dto.LoginDTO;
import com.donghun.todo.web.dto.UserDTO;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    private final JwtResolver jwtResolver;

    @Value("${todo.jjwt.secret}")
    private String secret;

    @Value("${todo.jjwt.expiration}")
    private String expirationTime;

    private ErrorResponseDTO response;


    public boolean isNameCheck(String name) {
        return userRepository.findByUsername(name) != null;
    }

    public ResponseEntity<?> duplicateUser() {
        response = ErrorResponseDTO.builder().status("400").error("Bad Request")
                .message("이미 존재하는 유저 네임입니다.").build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> createUser(UserDTO userDTO) {
        String defaultProfileImg = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/image/")
                .path("USER_DEFAULT_PROFILE_IMG.png")
                .toUriString();

        User saveUser = userRepository.save(userDTO.createUser(passwordEncoder, defaultProfileImg));

        return new ResponseEntity<>(saveUser, HttpStatus.CREATED);
    }

    public boolean userCheck(LoginDTO loginDTO) {
        if (userRepository.findByUsername(loginDTO.getName()) == null)
            return true;

        User user = userRepository.findByUsername(loginDTO.getName());

        return !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());
    }

    public ResponseEntity<?> invalidUser() {
        response = ErrorResponseDTO.builder().status("400").error("Bad Request")
                .message("아이디가 존재하지 않거나 비밀번호가 틀렸습니다.").build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> generateToken(LoginDTO loginDTO) {
        byte[] signingKey = secret.getBytes();
        User currentUser = userRepository.findByUsername(loginDTO.getName());

        if (tokenRepository.findByUsername(currentUser.getUsername()) != null)
            tokenRepository.deleteByUsername(currentUser.getUsername());

        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject("Todo JWT Token")
                .claim("idx", currentUser.getIdx())
                .claim("userName", currentUser.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expirationTime)))
                .compact();

        currentUser.setToken(token);
        userRepository.save(currentUser);

        Token dbToken = Token.builder().username(currentUser.getUsername()).token(token).build();
        tokenRepository.save(dbToken);

        return new ResponseEntity<>(currentUser, HttpStatus.CREATED);
    }

    public ResponseEntity<?> logoutLogic(HttpServletRequest request) {

        if (jwtResolver.isTokenExist(request)) {
            response = ErrorResponseDTO.builder().status("401").error("Not Authorized")
                    .message("권한이 없습니다.").build();

            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } else if (jwtResolver.validateToken(request)
                || tokenRepository.findByToken(jwtResolver.getToken(request)) == null) {
            response = ErrorResponseDTO.builder().status("401").error("Not Authorized")
                    .message("유효하지 않은 토큰입니다.").build();

            tokenRepository.deleteByToken(jwtResolver.getToken(request)); // 만료된 토큰 삭제
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        tokenRepository.deleteByToken(jwtResolver.getToken(request));
        return new ResponseEntity<>(new DefaultSuccessDTO(), HttpStatus.OK);
    }

    public ResponseEntity<?> getUser(HttpServletRequest request) {
        User user = userRepository.findByIdx(jwtResolver.getUserByToken(request));
        response = ErrorResponseDTO.builder().status("404").error("Not Found")
                .message("존재하지 않는 유저입니다.").build();

        return user == null ? new ResponseEntity<>(response, HttpStatus.NOT_FOUND) :
                new ResponseEntity<>(user, HttpStatus.OK);
    }
}
