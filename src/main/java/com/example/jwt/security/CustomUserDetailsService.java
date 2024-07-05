package com.example.jwt.security;

import com.example.jwt.usr.repository.UserRepository;
import com.example.jwt.usr.dto.UserDto;
import com.example.jwt.usr.entity.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(id);
        if(user==null) {
            throw new UsernameNotFoundException("아이디가 존재하지 않습니다.");
        }
        UserDto userDto = modelMapper.map(user, UserDto.class);
        return new CustomUserDetails(userDto);
    }

}