package com.dicemy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dicemy.reggie.entity.Category;
import com.dicemy.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
