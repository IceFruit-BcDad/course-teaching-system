package com.icefruit.courseteachingsystem.service;

import com.icefruit.courseteachingsystem.service.helper.ServiceHelper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    static Logger logger = LoggerFactory.getLogger(UserService.class);

    private ModelMapper modelMapper;

    private final ServiceHelper serviceHelper;
}
