package com.dicemy.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dicemy.reggie.common.CustomException;
import com.dicemy.reggie.entity.Category;
import com.dicemy.reggie.entity.Dish;
import com.dicemy.reggie.entity.Employee;
import com.dicemy.reggie.entity.Setmeal;
import com.dicemy.reggie.mapper.CategoryMapper;
import com.dicemy.reggie.mapper.EmployeeMapper;
import com.dicemy.reggie.service.CategoryService;
import com.dicemy.reggie.service.DishService;
import com.dicemy.reggie.service.EmployeeService;
import com.dicemy.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(queryWrapper);
        if (count1 > 0) {
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Setmeal::getCategoryId, id);
        int count2 = setmealService.count(queryWrapper1);
        if (count2 > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        super.removeById(id);
    }
}
