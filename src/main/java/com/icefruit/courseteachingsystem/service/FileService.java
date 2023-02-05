package com.icefruit.courseteachingsystem.service;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.icefruit.courseteachingsystem.config.AppProperties;
import com.icefruit.courseteachingsystem.error.ServiceException;
import com.icefruit.courseteachingsystem.service.helper.ServiceHelper;
import com.icefruit.courseteachingsystem.utils.FileTypeUtils;
import com.icefruit.courseteachingsystem.utils.UUIDUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Service
public class FileService {

    private final static ILogger logger = SLoggerFactory.getLogger(FileService.class);

    private final AppProperties appProperties;

    private final ServiceHelper serviceHelper;

    public FileService(AppProperties appProperties, ServiceHelper serviceHelper) {
        this.appProperties = appProperties;
        this.serviceHelper = serviceHelper;
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
            return newFilename;
        } catch (IOException ex) {
            String errMsg = "存储文件失败！";
            serviceHelper.handleException(logger, ex, errMsg);
            throw new ServiceException(errMsg, ex);
        }
    }

//    public Resource load(String filename){
//        Path filePath = getFilesDirPath().resolve(filename);
//        try {
//            Resource resource = new UrlResource(filePath.toUri());
//            if (!resource.exists()){
//                String errMsg = "该文件不存在";
//                throw new ServiceException(errMsg);
//            }
//            if (!resource.isReadable()){
//                String errMsg = "该文件不可读";
//                throw new ServiceException(errMsg);
//            }
//            return resource;
//        } catch (MalformedURLException ex) {
//            String errMsg = "加载文件Url错误";
//            serviceHelper.handleException(logger, ex, errMsg);
//            throw new ServiceException(errMsg, ex);
//        }
//    }
}
