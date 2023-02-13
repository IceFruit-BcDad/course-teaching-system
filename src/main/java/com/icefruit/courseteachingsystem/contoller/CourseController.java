package com.icefruit.courseteachingsystem.contoller;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.icefruit.courseteachingsystem.api.DataResponse;
import com.icefruit.courseteachingsystem.api.DtoList;
import com.icefruit.courseteachingsystem.api.ListResponse;
import com.icefruit.courseteachingsystem.api.Response;
import com.icefruit.courseteachingsystem.auth.AuthConstant;
import com.icefruit.courseteachingsystem.auth.Authorize;
import com.icefruit.courseteachingsystem.dto.CourseDto;
import com.icefruit.courseteachingsystem.dto.CreateCourseRequest;
import com.icefruit.courseteachingsystem.dto.UpdateCourseRequest;
import com.icefruit.courseteachingsystem.service.CourseService;
import jakarta.annotation.Nullable;
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
    public ListResponse<CourseDto> getCourses(@RequestParam int offset, @RequestParam @Min(0) int limit,
                                              @RequestParam @Nullable Long classificationId){
        DtoList<CourseDto> list = courseService.list(offset, limit, classificationId);
        return new ListResponse<>(list);

    }

    @PostMapping
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public DataResponse<CourseDto> create(@RequestBody @Valid CreateCourseRequest request){
        CourseDto courseDto = courseService.create(request.getClassificationId(),
                request.getName(), request.getCoverUrl());
        return new DataResponse<>(courseDto);
    }

    @GetMapping("/{id}")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ANONYMOUS_WEB,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public DataResponse<CourseDto> get(@PathVariable long id){
        final CourseDto courseDto = courseService.get(id);
        return new DataResponse<>(courseDto);
    }

    @PutMapping("/{id}")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public DataResponse<CourseDto> update(@PathVariable long id,
                                                  @RequestBody @Valid UpdateCourseRequest request){
        final CourseDto courseDto = courseService.update(id, request.getClassificationId(),
                request.getName(), request.getCoverUrl());
        return new DataResponse<>(courseDto);
    }

    @DeleteMapping("/{id}")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public Response delete(@PathVariable long id){
        courseService.delete(id);
        return new Response();
    }
}
