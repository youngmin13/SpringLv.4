package com.example.springlv_4.service;

import com.example.springlv_4.dto.AuthRequestDto;
import com.example.springlv_4.entity.User;
import com.example.springlv_4.entity.UserRoleEnum;
import com.example.springlv_4.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;
    public void signUp(AuthRequestDto requestDto) {
        // request 한 객체 값
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        UserRoleEnum role = requestDto.getRole();

        // 해당 이름을 가진 사용자가 존재한다면
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("해당 이름을 가진 사용자가 존재합니다.");
        }

        // 존재하지 않는다면 새로 User 객체를 만들어서 저장
        User user = new User (username, password, role);
        userRepository.save(user);
    }

    public void login(AuthRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        //사용자 확인 (username 이 없는 경우)
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new IllegalArgumentException("등록된 사용자가 없습니다.")
        );

        //비밀번호 확인 (password 가 다른 경우)
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        else {

        }
    }
}
