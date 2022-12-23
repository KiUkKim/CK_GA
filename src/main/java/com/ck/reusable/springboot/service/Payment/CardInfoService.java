package com.ck.reusable.springboot.service.Payment;

import com.ck.reusable.springboot.domain.user.User;
import com.ck.reusable.springboot.domain.user.UserCard;
import com.ck.reusable.springboot.domain.user.UserCardInfoRepository;
import com.ck.reusable.springboot.domain.user.UserRepository;
import com.ck.reusable.springboot.service.user.UserService;
import com.ck.reusable.springboot.web.dto.CardInfoDto;
import com.ck.reusable.springboot.web.dto.OrderDto.OrderInfoDto;
import com.ck.reusable.springboot.web.dto.UserDto;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;

@Service
public class CardInfoService {

    private UserCardInfoRepository userCardInfoRepository;

    private UserRepository userRepository;

    @Transactional
    public boolean CardInfoDuplicateCheck(String email)
    {
        if(userCardInfoRepository.existsByCardInfo(email))
        {
            return true;
        }
        else{
            return false;
        }
    }

    @Transactional
    public CardInfoDto.ForCardInfoListDto registerCard(CardInfoDto.ForCardRegisterRequestDto cardRegisterRequestDto)
    {
        UserCard userCard = userCardInfoRepository.save(cardRegisterRequestDto.toEntity());

        return new CardInfoDto.ForCardInfoListDto(cardRegisterRequestDto.getMerchant_uid(), cardRegisterRequestDto.getCustomer_uid(), userCard);
    }
}
