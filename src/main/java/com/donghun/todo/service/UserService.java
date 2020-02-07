package com.donghun.todo.service;

import com.donghun.todo.domain.Token;
import com.donghun.todo.domain.User;
import com.donghun.todo.repository.TokenRepository;
import com.donghun.todo.repository.UserRepository;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    private final JwtResolver jwtResolver;

    @Value("${todo.jjwt.secret}")
    private String secret;

    @Value("${todo.jjwt.expiration}")
    private String expirationTime;

    public ResponseEntity<?> validation(BindingResult bindingResult) {
        List<ObjectError> list = bindingResult.getAllErrors();
        StringBuilder msg = new StringBuilder();

        for (ObjectError error : list)
            msg.append(error.getDefaultMessage()).append("\n");

        ErrorResponseDTO response = new ErrorResponseDTO("400",
                "Bad Request", msg.toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public boolean isNameCheck(String name) {
        return userRepository.findByUsername(name) != null;
    }

    public ResponseEntity<?> duplicateUser() {
        ErrorResponseDTO response = new ErrorResponseDTO("400", "Bad Request",
                "이미 존재하는 유저네임입니다.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> createUser(UserDTO userDTO) {
        User saveUser = userRepository.save(userDTO.createUser(passwordEncoder));
        return new ResponseEntity<>(saveUser, HttpStatus.CREATED);
    }

    public boolean userCheck(LoginDTO loginDTO) {
        if (userRepository.findByUsername(loginDTO.getName()) == null)
            return true;

        User user = userRepository.findByUsername(loginDTO.getName());

        return !passwordEncoder.matches(loginDTO.getPassword(), user.getPassword());
    }

    public ResponseEntity<?> invalidUser() {
        ErrorResponseDTO response = new ErrorResponseDTO("400", "Bad Request",
                "아이디가 존재하지 않거나 비밀번호가 틀렸습니다");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<?> generateToken(LoginDTO loginDTO) {
        byte[] signingKey = secret.getBytes();
        User crrentUser = userRepository.findByUsername(loginDTO.getName());

        String token = Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject("Todo JWT Token")
                .claim("idx", crrentUser.getIdx())
                .claim("userName", crrentUser.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(expirationTime)))
                .compact();

        crrentUser.setToken(token);
        userRepository.save(crrentUser);

        Token dbToken = Token.builder().username(crrentUser.getUsername()).token(token).build();
        tokenRepository.save(dbToken);

        return new ResponseEntity<>(crrentUser, HttpStatus.CREATED);
    }

    public ResponseEntity<?> logoutLogic(HttpServletRequest request) {

        if (jwtResolver.isTokenExist(request)) {
            ErrorResponseDTO response = new ErrorResponseDTO("401",
                    "Not Authorized", "권한이 없습니다");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        } else if (jwtResolver.validateToken(request)
                || tokenRepository.findByToken(jwtResolver.getToken(request)) == null) {
            ErrorResponseDTO response = new ErrorResponseDTO("401",
                    "Not Authorized", "유효하지 않은 토큰입니다");

            tokenRepository.deleteByToken(jwtResolver.getToken(request)); // 만료된 토큰 삭제
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        tokenRepository.deleteByToken(jwtResolver.getToken(request));
        return new ResponseEntity<>(new DefaultSuccessDTO(), HttpStatus.OK);
    }
}
