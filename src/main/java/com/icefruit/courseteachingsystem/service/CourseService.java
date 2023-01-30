package com.icefruit.courseteachingsystem.service;

import com.icefruit.courseteachingsystem.api.DtoList;
import com.icefruit.courseteachingsystem.auth.AuthContext;
import com.icefruit.courseteachingsystem.dto.ClassificationDto;
import com.icefruit.courseteachingsystem.dto.CourseDto;
import com.icefruit.courseteachingsystem.model.Classification;
import com.icefruit.courseteachingsystem.model.Course;
import com.icefruit.courseteachingsystem.repository.CourseRepository;
import com.icefruit.courseteachingsystem.service.helper.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
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
public class CourseService {
    static Logger logger = LoggerFactory.getLogger(CourseService.class);

    private CourseRepository courseRepository;

    private ModelMapper modelMapper;

    private final ServiceHelper serviceHelper;

    public CourseDto create(long classificationId, String name){
        int count = courseRepository.countByNameAndClassificationId(name, classificationId);
        if (count > 0){
            throw new ServiceException(String.format("同一分类中名称为%s的课程已存在，请勿重复创建！", name));
        }
        String userIdStr = AuthContext.getUserId();
        long userId = Long.parseLong(userIdStr);
        Course course = Course.builder()
                .name(name)
                .classificationId(classificationId)
                .createUserId(userId)
                .build();
        try {
            courseRepository.save(course);
        } catch (Exception ex){
            String errMsg = "无法创建课程";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        return convertToDto(course);
    }

    public DtoList<CourseDto> list(int offset, int limit){
        if (limit <= 0) {
            limit = 10;
        }
        Pageable pageRequest = PageRequest.of(offset, limit);
        Page<Course> coursePage = courseRepository.findAll(pageRequest);
        List<CourseDto> courseDtoList = coursePage.getContent().stream().map(this::convertToDto).collect(toList());

        return DtoList.<CourseDto>builder()
                .total(coursePage.getTotalElements())
                .totalPages(coursePage.getTotalPages())
                .limit(limit)
                .offset(offset)
                .list(courseDtoList)
                .build();
    }

    private CourseDto convertToDto(Course course) {
        return modelMapper.map(course, CourseDto.class);
    }
}
