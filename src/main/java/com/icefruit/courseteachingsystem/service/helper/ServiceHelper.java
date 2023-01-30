package com.icefruit.courseteachingsystem.service.helper;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServiceHelper {


    public void handleException(Logger log, Exception ex, String errMsg) {
        log.error(errMsg, ex);
    }


}
