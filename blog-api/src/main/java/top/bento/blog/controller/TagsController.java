package top.bento.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bento.blog.service.TagService;
import top.bento.blog.vo.Result;
import top.bento.blog.vo.TagVo;

import java.util.List;

@RestController
@RequestMapping("tags")
public class TagsController {
    @Autowired
    private TagService tagService;

    // GET tags/hot
    @GetMapping("hot")
    public Result hot(){
        // list 6 the most popular tags
        int limit = 6;
        List<TagVo> hotTags = tagService.findHotTagIds(limit);
        return Result.success(hotTags);
    }
}
