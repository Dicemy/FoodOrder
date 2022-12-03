package com.dicemy.reggie.dto;

import com.dicemy.reggie.entity.Setmeal;
import com.dicemy.reggie.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
