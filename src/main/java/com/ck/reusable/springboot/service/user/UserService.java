package com.ck.reusable.springboot.service.user;

import com.ck.reusable.springboot.domain.Cup.CupRepository;
import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.domain.user.UserRepository;
import com.ck.reusable.springboot.web.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final CupRepository cupRepository;
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
        listResponseDto.setNow_cnt(0);
        listResponseDto.setTotal_cnt(0);
        listResponseDto.setBanUser(false);
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

    /*
    전화번호 중복 방지
     */
    public boolean validateDuplicatedTel(String tel){
        if(userRepository.findByTel(tel).isPresent()){
            return true;
        }
        return false;
    }


    /*
    User 정보 반환
     */
    public User findUser(String email)
    {
        return userRepository.findUserByEmail(email);
    }

    // 특정 User 정보 출력
    @Transactional
    public List<UserDto.ForUserTokenResponseDto> searchUserByEmail(String email)
    {
        return userRepository.findUserInfoByEmail(email).stream()
                .map(UserDto.ForUserTokenResponseDto::new)
                .collect(Collectors.toList());
    }


    //TDOO
    // 테스트 완료한 후에 명확하게 이름 바꿀 것!
    @Transactional
    public User searchUserByEmail2(String email)
    {
        return userRepository.findUserInfoByEmail2(email);
    }

    /*
    User Email 현재 컵 개수 반환
     */
    @Transactional
    public Integer UserCupNowCnt(String email)
    {
        return userRepository.UserNowCnt(email);
    }

    /*
    User Id 찾아주는 로직
     */
    @Transactional
    public Long userIdByEmail(String email)
    {
        return userRepository.findUserIdByEmail(email);
    }

    /*
    User 현재 - 총 컵 대여 횟수 증가
     */
    @Transactional
    public void UserRental(String email, Long goodAttitudeCup_Uid){
        cupRepository.UpdateCupState(goodAttitudeCup_Uid);
        userRepository.UpdateUserCnt(email);
    }


    //////////////////////////////// 나중에 삭제해주어야 함/////////////////////////////
    @Transactional
    public List<User> findUserAll()
    {
        return userRepository.findAllUser();
    }
}
