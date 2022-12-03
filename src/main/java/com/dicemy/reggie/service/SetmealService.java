package com.dicemy.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dicemy.reggie.dto.SetmealDto;
import com.dicemy.reggie.entity.Setmeal;

public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    public SetmealDto getByIdWithDish(Long id);

    public void removeByIdWithDishes(Long[] ids);

    public void updateByIdWithDishes(SetmealDto setmealDto);

    public void updateStatus(int status, Long[] ids);
}
