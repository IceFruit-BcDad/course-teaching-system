package com.icefruit.courseteachingsystem.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.icefruit.courseteachingsystem.error.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class FileManagerService {

    // 后续添加文件关联表，标识文件使用情况，当没有实体使用时可以彻底删除，不占用硬盘。

    private final static ILogger logger = SLoggerFactory.getLogger(FileManagerService.class);

    private final StringRedisTemplate stringRedisTemplate;


    public void newFile(String filename){
        stringRedisTemplate.opsForValue().set(filename, Instant.now().toString(), 24, TimeUnit.HOURS);
    }

    public void useFile(String filename){
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(filename))){
            throw new ServiceException(String.format("使用文件%s失败，未找到该文件", filename));
        }
        stringRedisTemplate.delete(filename);
    }
}
