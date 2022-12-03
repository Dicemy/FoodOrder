package com.dicemy.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dicemy.reggie.entity.Dish;
import com.dicemy.reggie.entity.DishFlavor;
import com.dicemy.reggie.mapper.DishFlavorMapper;
import com.dicemy.reggie.mapper.DishMapper;
import com.dicemy.reggie.service.DishFlavorService;
import com.dicemy.reggie.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {

}
