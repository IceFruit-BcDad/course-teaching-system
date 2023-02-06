package com.icefruit.courseteachingsystem.listener;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.icefruit.courseteachingsystem.service.FileService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    private final static ILogger logger = SLoggerFactory.getLogger(RedisKeyExpirationListener.class);

    private final FileService fileService;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer,
                                      FileService fileService) {
        super(listenerContainer);
        this.fileService = fileService;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expireKey = new String(message.getBody());
        fileService.delete(expireKey);
    }
}
