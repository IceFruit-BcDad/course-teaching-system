package com.icefruit.courseteachingsystem.service;

import com.icefruit.courseteachingsystem.api.DtoList;
import com.icefruit.courseteachingsystem.dto.CourseDto;
import com.icefruit.courseteachingsystem.dto.UserDto;
import com.icefruit.courseteachingsystem.model.Course;
import com.icefruit.courseteachingsystem.model.User;
import com.icefruit.courseteachingsystem.repository.UserRepository;
import com.icefruit.courseteachingsystem.service.helper.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserService {

    static Logger logger = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    private ModelMapper modelMapper;

    private final ServiceHelper serviceHelper;

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

    private UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }
}
