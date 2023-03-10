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

import static java.util.Arrays.stream;
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
            ClassificationDto classificationDto = convertToDto(level1Classification);
            list.add(classificationDto);
            List<ClassificationDto> children = level2ClassificationList
                    .stream()
                    .filter(item -> item.getParentId() == level1Classification.getId())
                    .map(this::convertToDto)
                    .toList();
            if (children.size() == 0){
                continue;
            }
            classificationDto.setChildren(children);
        }
        return DtoList.<ClassificationDto>builder()
                .total(list.size())
                .list(list)
                .build();
    }

    public ClassificationDto create(int level, String name, Long parentId){
        int count = classificationRepository.countByName(name);
        if (count > 0){
            throw new ServiceException(String.format("?????????%s??????????????????????????????????????????", name));
        }
        Classification classification = Classification.builder()
                .level(level)
                .name(name)
                .parentId(parentId)
                .build();
        try {
            classificationRepository.save(classification);
        } catch (Exception ex){
            String errMsg = "??????????????????";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        return convertToDto(classification);
    }

    public ClassificationDto update(long id, int level, String name, Long parentId){
        final Classification classification = classificationRepository.findById(id);
        if (classification == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "????????????id????????????");
        }
        classification.setLevel(level);
        classification.setName(name);
        classification.setParentId(parentId);
        try {
            classificationRepository.save(classification);
        } catch (Exception ex){
            String errMsg = "??????????????????";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }

        return convertToDto(classification);
    }

    public void delete(String content){
        List<Long> ids = stream(content.split(",")).filter(this::isLong).map(Long::parseLong).toList();
        classificationRepository.deleteAllById(ids);
        classificationRepository.deleteAllByParentIdIn(ids);
    }

    private boolean isLong(String value){
        try {
            Long.parseLong(value);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public ClassificationDto get(long id){
        final Classification classification = classificationRepository.findById(id);
        if (classification == null){
            throw new ServiceException(ResultCode.NOT_FOUND, "????????????id????????????");
        }
        return convertToDto(classification);
    }

    private ClassificationDto convertToDto(Classification classification) {
        return modelMapper.map(classification, ClassificationDto.class);
    }
}
