package com.dicemy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dicemy.reggie.common.R;
import com.dicemy.reggie.dto.DishDto;
import com.dicemy.reggie.entity.Category;
import com.dicemy.reggie.entity.Dish;
import com.dicemy.reggie.entity.DishFlavor;
import com.dicemy.reggie.service.CategoryService;
import com.dicemy.reggie.service.DishFlavorService;
import com.dicemy.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        String key = "dish_" + dishDto.getCategoryName() + "_1";
        redisTemplate.delete(key);
        dishService.saveWithFlavor(dishDto);
        return R.success("保存成功");
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        queryWrapper.orderByAsc(Dish::getSort);
        dishService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo, dishDtoPage,"records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息和对应口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 根据id修改菜品信息和对应口味信息
     * @param dishDto
     * @return
     */
    @PutMapping()
    public R<String> update(@RequestBody DishDto dishDto) {
        String key = "dish_" + dishDto.getCategoryName() + "_1";
        redisTemplate.delete(key);

        dishService.updateByIdWithFlavor(dishDto);
        return R.success("修改成功");
    }

    /**
     * 根据id删除菜品信息和对应的口味信息
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long[] ids) {
        DishDto dishDto = null;
        String key = null;
        for (Long id : ids) {
            dishDto = dishService.getByIdWithFlavor(id);
            key = "dish_" + dishDto.getCategoryName() + "_1";
            if (redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
            }
        }
        dishService.removeByIdWithFlavor(ids);
        return R.success("删除成功");
    }

    /**
     * 通过id进行菜品状态的批量修改
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable int status, Long[] ids) {
//        log.info(ids.toString());
        DishDto dishDto = null;
        String key = null;
        for (Long id : ids) {
            dishDto = dishService.getByIdWithFlavor(id);
            key = "dish_" + dishDto.getCategoryName() + "_1";
            if (redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
            }
        }
        dishService.updateStatus(status, ids);
        return R.success("状态修改成功");
    }

    /**
     * 根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish) {
//        List<Dish> list = dishService.listBySomething(dish);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        List<DishDto> dishDtoList = null;
        String key = "dish" + dish.getCategoryId() + "_" + dish.getStatus();
        dishDtoList = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if(dishDtoList != null) {
            return R.success(dishDtoList);
        }
        List<Dish> list = dishService.listBySomething(dish);
        dishDtoList = list.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;
        }).collect(Collectors.toList());
        redisTemplate.opsForValue().set(key, dishDtoList, 60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }

}
