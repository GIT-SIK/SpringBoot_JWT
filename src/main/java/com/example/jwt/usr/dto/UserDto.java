package com.example.jwt.usr.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.jwt.usr.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String userId;
    private String userRl;

    /* 프론트로 반환할 시 비밀번호는 제외함. */
    @JsonIgnore
    private String userPw;

    public static UserDto toDTO(User user){
        return UserDto.builder()
                .userId(user.getUserId())
                .userPw(user.getUserPw())
                .userRl(user.getUserRl()).build();

    }
}
