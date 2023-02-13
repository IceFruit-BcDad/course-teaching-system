package com.icefruit.courseteachingsystem.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.icefruit.courseteachingsystem.config.AppProperties;
import com.icefruit.courseteachingsystem.error.ServiceException;
import com.icefruit.courseteachingsystem.model.File;
import com.icefruit.courseteachingsystem.repository.FileRepository;
import com.icefruit.courseteachingsystem.service.helper.ServiceHelper;
import com.icefruit.courseteachingsystem.utils.FileTypeUtils;
import com.icefruit.courseteachingsystem.utils.UUIDUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
public class FileService {

    private final static ILogger logger = SLoggerFactory.getLogger(FileService.class);

    private final AppProperties appProperties;

    private final ServiceHelper serviceHelper;

    private final FileRepository fileRepository;

    private final StringRedisTemplate stringRedisTemplate;

    public FileService(AppProperties appProperties, ServiceHelper serviceHelper,
                       FileRepository fileRepository, StringRedisTemplate stringRedisTemplate) {
        this.appProperties = appProperties;
        this.serviceHelper = serviceHelper;
        this.fileRepository = fileRepository;
        this.stringRedisTemplate = stringRedisTemplate;
        Init();
    }

    private void Init(){
        if (!Files.exists(getFilesDirPath()) || !Files.isDirectory(getFilesDirPath())){
            try {
                Files.createDirectory(getFilesDirPath());
            } catch (IOException ex) {
                String errMsg = "创建Files文件夹失败";
                serviceHelper.handleException(logger, ex, errMsg);
                throw new ServiceException(errMsg, ex);
            }
        }
    }

    private Path getFilesDirPath(){
        return Paths.get(appProperties.getFileSavePath());
    }

    public String saveImgFile(MultipartFile file){
        String filename = file.getOriginalFilename();
        String suffix = FileTypeUtils.getFileTypeBySuffix(filename);
        if (Arrays.stream(appProperties.getSupportImgType()).filter(item -> item.equals(suffix)).findAny().isEmpty()){
            throw new ServiceException("上传的图片文件后缀格式不支持！");
        }
        try {
            String fileType = FileTypeUtils.getFileTypeByMagicNumber(file.getInputStream());
            if (Arrays.stream(appProperties.getSupportImgType()).filter(item -> item.equals(fileType)).findAny().isEmpty()){
                throw new ServiceException("上传的图片文件格式不支持！");
            }
            String newFilename = UUIDUtils.getUUIDFilenameBySuffix(fileType);
            Files.copy(file.getInputStream(), getFilesDirPath().resolve(newFilename));
            newFile(newFilename);
            return newFilename;
        } catch (IOException ex) {
            String errMsg = "获取文件格式或存储文件失败！";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
    }

    public String saveFile(MultipartFile file){
        String filename = file.getOriginalFilename();
        String newFilename = UUIDUtils.getUUIDFilename(filename);
        try {
            Files.copy(file.getInputStream(), getFilesDirPath().resolve(newFilename));
            newFile(newFilename);
            return newFilename;
        } catch (IOException ex) {
            String errMsg = "存储文件失败！";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
    }

    public void create(String filename, String used){
        Instant now = Instant.now();
        File file = File.builder()
                .filename(filename)
                .used(used)
                .createTime(now)
                .lastModifyTime(now)
                .build();
        try {
            fileRepository.save(file);
        } catch (Exception ex){
            String errMsg = "创建文件信息失败";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
    }

    public void delete(String filename){
        Path filePath = getFilesDirPath().resolve(filename);
        if (!Files.exists(filePath)){
            throw new ServiceException(String.format("删除文件失败，无法找到文件%s。",filename));
        }
        try {
            Files.delete(filePath);
        } catch (IOException ex) {
            String errMsg = "删除文件失败！";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
    }

    private void addUseInfo(String filename, String used){
        File file = fileRepository.findByFilename(filename);
        if (file == null){
            create(filename, used);
            return;
        }
        String newUsed = file.getUsed();
        if (StringUtils.hasText(newUsed)){
            newUsed += String.format(";%s", used);
        } else {
            newUsed = used;
        }
        fileRepository.updateUsedById(newUsed, file.getId());
    }

    private void removeUseInfo(String filename, String cancelUse){
        File file = fileRepository.findByFilename(filename);
        if (file == null){
            return;
        }
        String used = file.getUsed();
        if (!StringUtils.hasText(used)){
            delete(filename);
            return;
        }
        used = used.replace(cancelUse, "");
        fileRepository.updateUsedById(used, file.getId());
    }

    public void newFile(String filename){
        stringRedisTemplate.opsForValue().set(filename, Instant.now().toString(), 24, TimeUnit.HOURS);
    }

    public void useFile(String filename, Class<?> c, Object userId){
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(filename))){
            throw new ServiceException(String.format("使用文件%s失败，未找到该文件", filename));
        }
        stringRedisTemplate.delete(filename);
        String used = String.format("%1s:%2s", c.getName(), userId.toString());
        addUseInfo(filename, used);
    }

    public void cancelUseFile(String filename, Class<?> c, Object userId){
        String cancelUse = String.format("%1s:%2s", c.getName(), userId.toString());
        removeUseInfo(filename, cancelUse);
    }
}
