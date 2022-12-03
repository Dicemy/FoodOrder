package com.dicemy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dicemy.reggie.dto.DishDto;
import com.dicemy.reggie.entity.Category;
import com.dicemy.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
    public DishDto getByIdWithFlavor(Long id);
    public void updateByIdWithFlavor(DishDto dishDto);
    public void removeByIdWithFlavor(Long[] ids);
    public void updateStatus(int status, Long[] ids);
    public List<Dish> listBySomething(Dish dish);
}
