package com.icefruit.courseteachingsystem.contoller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/file")
public class FileController {
    // 文件上传可以指定格式检查，不传默认不检查
    // 上传文件进行存储后，记录进Redis，24小时过期没有使用，通过Redis过期回调进行文件删除
}
