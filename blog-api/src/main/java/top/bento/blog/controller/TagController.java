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
public class TagController {
    @Autowired
    private TagService tagService;

    // GET tags/hot
    @GetMapping("hot")
    public Result listTopTags(){
        // list 6 the most popular tags
        int limit = 6;
        List<TagVo> hotTags = tagService.findHotTagIds(limit);
        return Result.success(hotTags);
    }

    // GET tags/
    @GetMapping
    public Result listAllTags(){
        List<TagVo> tagVoList = tagService.listAllTags();
        return Result.success(tagVoList);
    }

}
