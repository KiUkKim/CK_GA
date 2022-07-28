package com.ck.reusable.springboot.service.user;

import com.ck.reusable.springboot.domain.ErrorMessage.errorMessage2;
import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.domain.user.UserRepository;
import com.ck.reusable.springboot.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;

@RequiredArgsConstructor
@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /*
    Dto 방식 Save Service
     */
    //User 저장
    @Transactional
    public UserDto.ForUserResponseDto registerMember(UserDto.ForUserSignRequestDto forUserDto)
    {
        UserDto.ForUserListResponseDto listResponseDto = new UserDto.ForUserListResponseDto();

        String password = forUserDto.getPasswd();

        System.out.println(password);

        listResponseDto.setRoles("ROLE_USER");
        listResponseDto.setPassword(bCryptPasswordEncoder.encode(forUserDto.getPasswd()));

        Assert.assertNotNull(listResponseDto.getPassword(), "Password is not null");

        BeanUtils.copyProperties(forUserDto, listResponseDto);

        User user = userRepository.save(listResponseDto.toEntity2());

        return new UserDto.ForUserResponseDto(user);
    }

    /*
    email이 중복되면 안된다.
     */
    public boolean validateDuplicated(String email)
    {
        if(userRepository.findByEmail(email).isPresent()){
            return true;
        }

        return false;
    }

    /*
    로그인 처리 시켜야함
     */
//    public UserDto.ForUserResponseDto LoginMember(UserDto.ForUserLoginRequestDto forUserLoginDto)
//    {
//        User user = userRepository.findUserByEmail(forUserLoginDto.getEmail());
//
//        Assert.notNull(user, "user is must not be null");
//
//        if (!passwordEncoder.matches(forUserLoginDto.getPasswd(), user.getPassword()))
//        {
//            throw new HTTPException(400);
//        }
//
//        String accessToken = jwtTokenProvider.createToken(forUserLoginDto.getEmail());
//        return new UserDto.ForUserLoginResponseDto(user.getEmail(), accessToken);
//    }


    /*
    User 정보 반환
     */
    public User findUser(String email)
    {
        return userRepository.findUserByEmail(email);
    }


}
