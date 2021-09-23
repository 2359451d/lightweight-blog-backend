package top.bento.blog.service;

import top.bento.blog.vo.CategoryVo;

import java.util.List;

public interface CategoryService {
    CategoryVo findCategoryById(Long id);
}
