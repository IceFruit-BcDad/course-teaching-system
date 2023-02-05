package com.icefruit.courseteachingsystem.service.helper;

import com.github.structlog4j.ILogger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServiceHelper {


    public void handleException(ILogger log, Exception ex, String errMsg) {
        log.error(errMsg, ex);
    }


}
