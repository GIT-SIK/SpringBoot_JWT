package com.example.jwt.usr.entity;

import com.example.jwt.usr.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USER_TB")

public class User {
        @Id
        @Column(name = "USER_ID")
        private String userId;

        @Column(name = "USER_ROLE")
        private String userRl;

        @Column(name ="USER_PW")
        private String userPw;

        public static User toEntity(UserDto userDto) {
                return User.builder()
                        .userId(userDto.getUserId())
                        .userPw(userDto.getUserPw())
                        .userRl(userDto.getUserRl()).build();

        }

}

