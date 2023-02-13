package com.icefruit.courseteachingsystem.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.icefruit.courseteachingsystem.api.DtoList;
import com.icefruit.courseteachingsystem.api.ResultCode;
import com.icefruit.courseteachingsystem.auth.AuthContext;
import com.icefruit.courseteachingsystem.dto.ClassificationDto;
import com.icefruit.courseteachingsystem.dto.CourseDto;
import com.icefruit.courseteachingsystem.error.ServiceException;
import com.icefruit.courseteachingsystem.model.Course;
import com.icefruit.courseteachingsystem.repository.CourseRepository;
import com.icefruit.courseteachingsystem.service.helper.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CourseService {
    static ILogger logger = SLoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;

    private final ClassificationService classificationService;

    private final ChapterService chapterService;

    private final FileService fileService;

    private final ModelMapper modelMapper;

    private final ServiceHelper serviceHelper;

    public CourseDto create(long classificationId, String name, String coverUrl){
        int count = courseRepository.countByNameAndClassificationId(name, classificationId);
        if (count > 0){
            throw new ServiceException(String.format("同一分类中名称为%s的课程已存在，请勿重复创建！", name));
        }
        String userIdStr = AuthContext.getUserId();
        long userId = Long.parseLong(userIdStr);
        final Instant now = Instant.now();
        Course course = Course.builder()
                .name(name)
                .classificationId(classificationId)
                .createUserId(userId)
                .coverUrl(coverUrl)
                .createTime(now)
                .lastModifyTime(now)
                .build();
        try {
            courseRepository.save(course);
            fileService.useFile(coverUrl, Course.class, course.getId());
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

    public CourseDto update(long id, Long classificationId, String name, String coverUrl){
        final Course course = courseRepository.findById(id);
        if (course == null){
            throw new com.icefruit.courseteachingsystem.error.ServiceException(ResultCode.NOT_FOUND, "未找到此id的课程。");
        }
        String oldCoverUrl = course.getCoverUrl();
        if (classificationId != null){
            course.setClassificationId(classificationId);
        }
        if (name != null){
            course.setName(name);
        }
        if (coverUrl != null){
            course.setCoverUrl(coverUrl);
        }
        course.setLastModifyTime(Instant.now());
        try {
            courseRepository.save(course);
            if (StringUtils.hasText(coverUrl)){
                fileService.useFile(coverUrl, Course.class, course.getId());
                if (StringUtils.hasText(oldCoverUrl)){
                    fileService.cancelUseFile(oldCoverUrl, Course.class, course.getId());
                }
            }
        } catch (Exception ex){
            String errMsg = "更新课程失败";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        return convertToDto(course);
    }

    public void delete(long id){
        Course course = courseRepository.findById(id);
        if (course == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "未找到此id的课程。");
        }
        courseRepository.deleteById(id);
        fileService.cancelUseFile(course.getCoverUrl(), Course.class, course.getId());
        chapterService.deleteByCourseId(id);
    }

    public CourseDto get(long id){
        final Course course = courseRepository.findById(id);
        if (course == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "未找到此id的课程。");
        }
        return convertToDto(course);
    }


    private CourseDto convertToDto(Course course) {
        CourseDto courseDto = modelMapper.map(course, CourseDto.class);
        ClassificationDto classificationDto = classificationService.get(course.getClassificationId());
        if (classificationDto != null){
            courseDto.setClassificationName(classificationDto.getName());
        }
        return courseDto;
    }
}
