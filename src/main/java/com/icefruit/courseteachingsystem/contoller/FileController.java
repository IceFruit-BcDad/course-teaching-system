package com.icefruit.courseteachingsystem.contoller;

import com.icefruit.courseteachingsystem.api.DataResponse;
import com.icefruit.courseteachingsystem.api.Response;
import com.icefruit.courseteachingsystem.auth.AuthConstant;
import com.icefruit.courseteachingsystem.auth.Authorize;
import com.icefruit.courseteachingsystem.service.FileManagerService;
import com.icefruit.courseteachingsystem.service.FileService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/file")
public class FileController {
    // 文件上传可以指定格式检查，不传默认不检查
    // 上传文件进行存储后，记录进Redis，24小时过期没有使用，通过Redis过期回调进行文件删除

    private final FileService fileService;

    private final FileManagerService fileManagerService;

    public FileController(FileService fileService, FileManagerService fileManagerService) {
        this.fileService = fileService;
        this.fileManagerService = fileManagerService;
    }

    @PostMapping("/uploadImg")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public DataResponse<String> uploadImg(@RequestParam MultipartFile file) {
        String filename = fileService.saveImgFile(file);
        return new DataResponse<>(filename);
    }

    @PostMapping("/upload")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_ADMINISTRATOR_USER
    })
    public DataResponse<String> upload(@RequestParam("file") MultipartFile file) {
        String filename = fileService.saveFile(file);
        return new DataResponse<>(filename);
    }

    @GetMapping("/useFile/{filename}")
    public Response testUseFile(@PathVariable String filename){
        fileManagerService.useFile(filename);
        return new Response();
    }
}
