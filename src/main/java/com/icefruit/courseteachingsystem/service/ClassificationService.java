package com.icefruit.courseteachingsystem.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.icefruit.courseteachingsystem.api.DtoList;
import com.icefruit.courseteachingsystem.api.ResultCode;
import com.icefruit.courseteachingsystem.dto.ClassificationDto;
import com.icefruit.courseteachingsystem.error.ServiceException;
import com.icefruit.courseteachingsystem.model.Classification;
import com.icefruit.courseteachingsystem.repository.ClassificationRepository;
import com.icefruit.courseteachingsystem.service.helper.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ClassificationService {

    static ILogger logger = SLoggerFactory.getLogger(ClassificationService.class);

    private final ClassificationRepository classificationRepository;

    private final ServiceHelper serviceHelper;

    private final ModelMapper modelMapper;

    public DtoList<ClassificationDto> list(int offset, int limit){
        if (limit <= 0) {
            limit = 10;
        }
        Pageable pageRequest = PageRequest.of(offset, limit);
        Page<Classification> deviceControllerPage = classificationRepository.findAll(pageRequest);
        List<ClassificationDto> deviceControllerDtoList = deviceControllerPage.getContent().stream().map(this::convertToDto).collect(toList());

        return DtoList.<ClassificationDto>builder()
                .total(deviceControllerPage.getTotalElements())
                .totalPages(deviceControllerPage.getTotalPages())
                .limit(limit)
                .offset(offset)
                .list(deviceControllerDtoList)
                .build();
    }

    public DtoList<ClassificationDto> list(){
        List<ClassificationDto> list = new ArrayList<>();

        List<Classification> level1ClassificationList = classificationRepository.findClassificationsByLevel(1);
        List<Classification> level2ClassificationList = classificationRepository.findClassificationsByLevel(2);
        for (Classification level1Classification :
                level1ClassificationList) {
            List<ClassificationDto> children = level2ClassificationList
                    .stream()
                    .filter(item -> item.getParentId() == level1Classification.getId())
                    .map(this::convertToDto)
                    .toList();
            if (children.size() == 0){
                continue;
            }
            ClassificationDto classificationDto = convertToDto(level1Classification);
            classificationDto.setChildren(children);
            list.add(classificationDto);
        }
        return DtoList.<ClassificationDto>builder()
                .total(list.size())
                .list(list)
                .build();
    }

    public ClassificationDto create(int level, String name, Long parentId){
        int count = classificationRepository.countByName(name);
        if (count > 0){
            throw new ServiceException(String.format("名称为%s的分类已存在，请勿重复创建！", name));
        }
        Classification classification = Classification.builder()
                .level(level)
                .name(name)
                .parentId(parentId)
                .build();
        try {
            classificationRepository.save(classification);
        } catch (Exception ex){
            String errMsg = "无法创建分类";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        return convertToDto(classification);
    }

    public ClassificationDto update(long id, int level, String name, Long parentId){
        final Classification classification = classificationRepository.findById(id);
        if (classification == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "未找到此id的分类。");
        }
        classification.setLevel(level);
        classification.setName(name);
        classification.setParentId(parentId);
        try {
            classificationRepository.save(classification);
        } catch (Exception ex){
            String errMsg = "更新分类失败";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        return convertToDto(classification);
    }

    public void delete(long id){
        classificationRepository.deleteById(id);
    }

    public ClassificationDto get(long id){
        final Classification classification = classificationRepository.findById(id);
        if (classification == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "未找到此id的分类。");
        }
        return convertToDto(classification);
    }

    private ClassificationDto convertToDto(Classification classification) {
        return modelMapper.map(classification, ClassificationDto.class);
    }
}
