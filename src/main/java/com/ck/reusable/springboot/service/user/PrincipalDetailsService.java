package com.ck.reusable.springboot.service.user;

import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.domain.user.UserRepository;
import com.ck.reusable.springboot.security.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//http://localhost:8080/login
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("CustomUserDatilService의 loadUserByUserName()");
        User user = userRepository.findUserByEmail(username);

        if(user == null)
        {
            throw new UsernameNotFoundException("존재하지 않습니다.");
        }

        System.out.println("userEntity : " + user);

        return new PrincipalDetails(user);
    }
}
