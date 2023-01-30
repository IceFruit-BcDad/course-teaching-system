package com.icefruit.courseteachingsystem.contoller;

import com.icefruit.courseteachingsystem.api.DataResponse;
import com.icefruit.courseteachingsystem.api.DtoList;
import com.icefruit.courseteachingsystem.api.ListResponse;
import com.icefruit.courseteachingsystem.auth.AuthConstant;
import com.icefruit.courseteachingsystem.auth.Authorize;
import com.icefruit.courseteachingsystem.dto.ClassificationDto;
import com.icefruit.courseteachingsystem.dto.CreateClassificationRequest;
import com.icefruit.courseteachingsystem.model.Classification;
import com.icefruit.courseteachingsystem.service.ClassificationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/classification")
public class ClassificationController {

    private static Logger logger = LoggerFactory.getLogger(ClassificationController.class);

    private final ClassificationService classificationService;

    public ClassificationController(ClassificationService classificationService) {
        this.classificationService = classificationService;
    }

    @GetMapping
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_ANONYMOUS_WEB,
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public ListResponse<ClassificationDto> getClassifications(){
        DtoList<ClassificationDto> list = classificationService.list();
        return new ListResponse<>(list);

    }

    @PostMapping
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public DataResponse<ClassificationDto> create(@RequestBody @Valid CreateClassificationRequest request){
        ClassificationDto classificationDto = classificationService.create(request.getLevel(),
                request.getName(), request.getParentId());
        return new DataResponse<>(classificationDto);
    }
}
