package com.example.jwt.usr.service;
import com.example.jwt.usr.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.example.jwt.security.JwtUtil;
import com.example.jwt.usr.dto.UserDto;
import com.example.jwt.usr.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
//    private final PasswordEncoderConfig passwordEncoderConfig;
    private final ModelMapper modelMapper;


    /**
     * 유저 데이터 반환
     * @Param : id
     * @Return : User -> UserDto
     */

    public UserDto findByUserId(String id){
        return modelMapper.map(userRepository.findByUserId(id),UserDto.class);
    }

    /**
     * 로그인시 유저 데이터 반환
     * @Param : id, password
     * @Return : AccessToken, User -> UserDto
     */

    public LoginResponse login(String id, String password){

        User user = userRepository.findByUserId(id);
        if(user == null) {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
        }

        if(!user.getUserPw().equals(password)) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        UserDto userDto = modelMapper.map(user,UserDto.class);

        String accessToken = jwtUtil.createAccessToken(userDto);
        return new LoginResponse(accessToken, userDto);
    }

    /**
     * Front로 반환될 데이터를 가공하는 클래스
     * @return : serviceToken, User
     *
     */
    public class LoginResponse {
        private String serviceToken;

        @JsonProperty("user")
        private UserDto userDto;

        public LoginResponse(String serviceToken, UserDto userDto) {
            this.serviceToken = serviceToken;
            this.userDto = userDto;
        }

        public String getServiceToken(){
            return serviceToken;

        }

        public UserDto getUserDto(){
            return userDto;

        }
    }
}
