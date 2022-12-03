package com.dicemy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dicemy.reggie.common.BaseContext;
import com.dicemy.reggie.common.R;
import com.dicemy.reggie.dto.OrdersDto;
import com.dicemy.reggie.entity.*;
import com.dicemy.reggie.service.OrderDetailService;
import com.dicemy.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders) {
        orderService.submit(orders);
        return R.success("下单成功");
    }

    /**
     * 查询手机端用户订单
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/userPage")
    public R<Page> userPage(int page, int pageSize) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(dtoPage, pageInfo, "records");
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtos = records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            ordersDto.setAddress(item.getAddress());
            ordersDto.setPhone(item.getPhone());
            ordersDto.setUserName(item.getUserName());
            ordersDto.setConsignee(item.getConsignee());
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailLambdaQueryWrapper);
            ordersDto.setOrderDetails(orderDetails);
            return ordersDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(ordersDtos);
        return R.success(dtoPage);
    }

    /**
     * 后台查看订单
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);
        Page<OrdersDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);
        BeanUtils.copyProperties(dtoPage, pageInfo, "records");
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> ordersDtos = records.stream().map((item)->{
            OrdersDto ordersDto = new OrdersDto();
            BeanUtils.copyProperties(item, ordersDto);
            ordersDto.setAddress(item.getAddress());
            ordersDto.setPhone(item.getPhone());
            ordersDto.setUserName(item.getUserName());
            ordersDto.setConsignee(item.getConsignee());
            LambdaQueryWrapper<OrderDetail> orderDetailLambdaQueryWrapper = new LambdaQueryWrapper<>();
            orderDetailLambdaQueryWrapper.eq(OrderDetail::getOrderId, item.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(orderDetailLambdaQueryWrapper);
            ordersDto.setOrderDetails(orderDetails);
            return ordersDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(ordersDtos);
        return R.success(dtoPage);
    }

    @PutMapping
    public R<String> updateStatus(@RequestBody Orders order) {
        Orders orders = orderService.getById(order.getId());
        orders.setStatus(order.getStatus());
        orderService.updateById(orders);
        return R.success("修改成功");
    }
}
