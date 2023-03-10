package com.icefruit.courseteachingsystem.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.icefruit.courseteachingsystem.api.DtoList;
import com.icefruit.courseteachingsystem.api.ResultCode;
import com.icefruit.courseteachingsystem.dto.UserDto;
import com.icefruit.courseteachingsystem.error.ServiceException;
import com.icefruit.courseteachingsystem.model.User;
import com.icefruit.courseteachingsystem.repository.UserRepository;
import com.icefruit.courseteachingsystem.service.helper.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserService {

    static ILogger logger = SLoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    private final ServiceHelper serviceHelper;

    public UserDto get(long userId){
        final User user = userRepository.findById(userId);
        if (user == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "user with specified userId not found");
        }
        return convertToDto(user);
    }

    public DtoList<UserDto> list(int offset, int limit){
        if (limit <= 0) {
            limit = 10;
        }
        Pageable pageRequest = PageRequest.of(offset, limit);
        Page<User> userPage = userRepository.findAll(pageRequest);
        List<UserDto> courseDtoList = userPage.getContent().stream().map(this::convertToDto).collect(toList());

        return DtoList.<UserDto>builder()
                .total(userPage.getTotalElements())
                .totalPages(userPage.getTotalPages())
                .limit(limit)
                .offset(offset)
                .list(courseDtoList)
                .build();
    }

    public UserDto create(Integer type, String phoneNumber, String name, String password){
        int count = userRepository.countByPhoneNumber(phoneNumber);
        if (count > 0){
            throw new ServiceException(String.format("????????????%1s??????????????????????????????????????????", name));
        }
        User user = User.builder()
                .type(type)
                .phoneNumber(phoneNumber)
                .name(name)
                .passwordHash(passwordEncoder.encode(password))
                .createTime(Instant.now())
                .build();

        try {
            userRepository.save(user);
        } catch (Exception ex){
            String errMsg = "?????????????????????";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        return convertToDto(user);
    }

    public UserDto verifyPassword(String phoneNumber, String password){
        final User user = userRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new ServiceException(ResultCode.NOT_FOUND, "user with specified phonenumber not found");
        }

        if (user.getPasswordHash().isEmpty()) {
            throw new ServiceException(ResultCode.REQ_REJECT, "This user has not set up their password");
        }

        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new ServiceException(ResultCode.UN_AUTHORIZED, "Incorrect password");
        }

        return convertToDto(user);
    }

    public UserDto update(long id, Integer type, String phoneNumber, String name, String password){
        final User user = userRepository.findById(id);
        if (user == null){
            throw new com.icefruit.courseteachingsystem.error.ServiceException(ResultCode.NOT_FOUND, "????????????id????????????");
        }
        if (type != null){
            user.setType(type);
        }
        if (phoneNumber != null){
            user.setPhoneNumber(phoneNumber);
        }
        if (name != null){
            user.setName(name);
        }
        if (password != null){
            user.setPasswordHash(passwordEncoder.encode(password));
        }

        try {
            userRepository.save(user);
        } catch (Exception ex){
            String errMsg = "????????????????????????";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new com.icefruit.courseteachingsystem.error.ServiceException(errMsg, ex);
        }

        return convertToDto(user);
    }

    public void delete(long id){
        userRepository.deleteById(id);
    }


    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
