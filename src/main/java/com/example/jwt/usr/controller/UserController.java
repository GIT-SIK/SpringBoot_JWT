package com.example.jwt.usr.controller;

import com.example.jwt.security.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.jwt.usr.dto.UserDto;
import com.example.jwt.usr.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 토큰 여부 확인 후 유저 재반환
     * 확인 절차 - 시큐리티에 저장되어 있는 유저 데이터로 비교하여 반환
     * @return 유저
     */
    @GetMapping("/api/u/me")
    public ResponseEntity<?> UserRefresh(HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = ((CustomUserDetails) authentication.getPrincipal()).getUsername();
        UserDto userDto =  userService.findByUserId(userId);
        if (userDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(userDto);


    }

    /**
     * 로그인 
     * @param user (id, password)
     * @return AccessToken, user
     */
    
    @PostMapping("/api/u/login")
    public ResponseEntity<?> login(@RequestBody String user, @AuthenticationPrincipal UserDetails userDetails) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(user);
        String id = jsonNode.get("userId").asText();
        String password = jsonNode.get("userPw").asText();
        UserService.LoginResponse loginResponse = userService.login(id, password);
        System.out.println("Response " + loginResponse.getUserDto().getUserId());
        try {
            return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID or PWD");
        }

    }


}
