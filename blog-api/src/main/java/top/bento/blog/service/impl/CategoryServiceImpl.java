package top.bento.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.bento.blog.dao.mapper.CategoryMapper;
import top.bento.blog.dao.pojo.Category;
import top.bento.blog.service.CategoryService;
import top.bento.blog.vo.CategoryVo;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryVo findCategoryById(Long id){
        Category category = categoryMapper.selectById(id);
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    @Override
    public List<CategoryVo> findAll() {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Category::getId, Category::getCategoryName);
        List<Category> categories = categoryMapper.selectList(queryWrapper);
        // convert pojo to vo
        return copyList(categories);
    }

    @Override
    public List<CategoryVo> findAllDetails() {
        List<Category> categories = categoryMapper.selectList(new LambdaQueryWrapper<>());
        // convert pojo to vo
        return copyList(categories);
    }

    @Override
    public CategoryVo getCategoryDetailById(Long id) {
        Category category = categoryMapper.selectById(id);
        return copy(category);
    }

    public CategoryVo copy(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }

    public List<CategoryVo> copyList(List<Category> categoryList){
        List<CategoryVo> categoryVoList = new ArrayList<>();
        for (Category category : categoryList) {
            categoryVoList.add(copy(category));
        }
        return categoryVoList;
    }
}
