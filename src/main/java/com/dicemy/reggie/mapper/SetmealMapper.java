package com.dicemy.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dicemy.reggie.entity.Dish;
import com.dicemy.reggie.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
}
