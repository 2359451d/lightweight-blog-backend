package top.bento.blog.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.bento.blog.utils.QiniuUtils;
import top.bento.blog.vo.Result;

import java.util.UUID;

@RestController
@RequestMapping("upload")
public class UploadController {

    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping
    public Result uploadImg(@RequestParam("image") MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        String suffix = StringUtils.substringAfterLast(originalFilename, '.');
        // unique file name
        String fileName = UUID.randomUUID().toString() + "." + suffix;

        boolean upload = qiniuUtils.upload(file, fileName);
        if (upload) {
            return Result.success(QiniuUtils.url + fileName);
        }
        return Result.fail(20001, "fail to upload");
    }
}
