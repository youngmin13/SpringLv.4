package com.example.springlv_4.security;

import com.example.springlv_4.entity.User;
import com.example.springlv_4.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 받은 username으로 데이터베이스에서 검색한 후
        // 해당 username을 가진 user가 존재하면 UserDetailsImpl로 감싸서 리턴
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 username : " + username + "을 가진 사용자를 찾을 수 없습니다."));

        return new UserDetailsImpl(user);
    }
}
