package com.icefruit.courseteachingsystem.contoller;

import com.icefruit.courseteachingsystem.api.DataResponse;
import com.icefruit.courseteachingsystem.api.DtoList;
import com.icefruit.courseteachingsystem.api.ListResponse;
import com.icefruit.courseteachingsystem.api.Response;
import com.icefruit.courseteachingsystem.auth.AuthConstant;
import com.icefruit.courseteachingsystem.auth.Authorize;
import com.icefruit.courseteachingsystem.dto.ClassificationDto;
import com.icefruit.courseteachingsystem.dto.CreateOrUpdateClassificationRequest;
import com.icefruit.courseteachingsystem.service.ClassificationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.stream;


@RestController
@RequestMapping("/api/classification")
public class ClassificationController {

    private static final Logger logger = LoggerFactory.getLogger(ClassificationController.class);

    private final ClassificationService classificationService;

    public ClassificationController(ClassificationService classificationService) {
        this.classificationService = classificationService;
    }

    @GetMapping
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ANONYMOUS_WEB,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER,
            AuthConstant.AUTHORIZATION_ADMINISTRATOR_USER
    })
    public ListResponse<ClassificationDto> getClassifications(){
        DtoList<ClassificationDto> list = classificationService.list();
        return new ListResponse<>(list);

    }

    @PostMapping
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ADMINISTRATOR_USER,
    })
    public DataResponse<ClassificationDto> create(@RequestBody @Valid CreateOrUpdateClassificationRequest request){
        ClassificationDto classificationDto = classificationService.create(request.getLevel(),
                request.getName(), request.getParentId());
        return new DataResponse<>(classificationDto);
    }

    @GetMapping("/{id}")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ANONYMOUS_WEB,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_ADMINISTRATOR_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public DataResponse<ClassificationDto> get(@PathVariable long id){
        final ClassificationDto classificationDto = classificationService.get(id);
        return new DataResponse<>(classificationDto);
    }

    @PutMapping("/{id}")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ADMINISTRATOR_USER,
    })
    public DataResponse<ClassificationDto> update(@PathVariable long id,
                                                  @RequestBody @Valid CreateOrUpdateClassificationRequest request){
        final ClassificationDto classificationDto = classificationService.update(id, request.getLevel(),
                request.getName(), request.getParentId());
        return new DataResponse<>(classificationDto);
    }

    @DeleteMapping("/{ids}")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ADMINISTRATOR_USER,
    })
    public Response delete(@PathVariable String ids){
        classificationService.delete(ids);
        return new Response();
    }
}
