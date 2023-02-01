package com.icefruit.courseteachingsystem.contoller;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.icefruit.courseteachingsystem.api.DataResponse;
import com.icefruit.courseteachingsystem.api.DtoList;
import com.icefruit.courseteachingsystem.api.ListResponse;
import com.icefruit.courseteachingsystem.auth.AuthConstant;
import com.icefruit.courseteachingsystem.auth.Authorize;
import com.icefruit.courseteachingsystem.dto.ClassificationDto;
import com.icefruit.courseteachingsystem.dto.CourseDto;
import com.icefruit.courseteachingsystem.dto.CreateClassificationRequest;
import com.icefruit.courseteachingsystem.dto.CreateCourseRequest;
import com.icefruit.courseteachingsystem.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    static ILogger logger = SLoggerFactory.getLogger(CourseController.class);

    private CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ANONYMOUS_WEB,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public ListResponse<CourseDto> getCourses(@RequestParam int offset, @RequestParam @Min(0) int limit){
        DtoList<CourseDto> list = courseService.list(offset, limit);
        return new ListResponse<>(list);

    }

    @PostMapping
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public DataResponse<CourseDto> create(@RequestBody @Valid CreateCourseRequest request){
        CourseDto courseDto = courseService.create(request.getClassificationId(), request.getName());
        return new DataResponse<>(courseDto);
    }
}
