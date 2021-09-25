package top.bento.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.bento.blog.service.CategoryService;
import top.bento.blog.vo.CategoryVo;
import top.bento.blog.vo.Result;

import java.util.List;

@RestController
@RequestMapping("categorys")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public Result listCategories() {
        List<CategoryVo> categoryVoList = categoryService.findAll();
        return Result.success(categoryVoList);
    }

    @GetMapping("detail")
    public Result listCategoryDetails() {
        return Result.success(categoryService.findAllDetails());
    }

    // GET /category/detail/{id}
    @GetMapping("detail/{id}")
    public Result getCategoryDetailById(@PathVariable("id") Long id) {
        return Result.success(categoryService.getCategoryDetailById(id));
    }
}
