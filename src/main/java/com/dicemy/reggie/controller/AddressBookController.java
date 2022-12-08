package com.dicemy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.dicemy.reggie.common.BaseContext;
import com.dicemy.reggie.common.R;
import com.dicemy.reggie.entity.AddressBook;
import com.dicemy.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 *@RestController = @Controller + @ResponseBody
 * @Controller注解是定义了一个满足SpringMVC Controller的Bean，@ResponseBody表示将Controller方法中的return的对象序列化后返回一个字符串
 * 在配置中加入了JacksonObjectMapper序列化器，这个序列化器可以将Java对象转化成json格式。所以@ResponseBody返回的是一个json格式的字符串
 */

/*
 *@RequestMapping注解定义了该控制器可以处理哪些URL请求。相当于Servlet中在web.xml中配置
 */
@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {
    //@Autowired实现bean的自动装配
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     * @param addressBook
     * @return
     */
    //@RequestBody用来接收前端传递给后端的json字符串中的数据
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
            addressBook.setUserId(BaseContext.getCurrentId());
            addressBookService.save(addressBook);
            return R.success(addressBook);
    }

    /**
     * 设置默认地址
     * @param addressBook
     * @return
     */
    //@Transactional启动事务控制，使用AOP对方法前后进行拦截。
    @PutMapping("/default")
    @Transactional
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(wrapper);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        }
        else {
            return R.error("没有找到对象");
        }
    }

    /**
     * 查询默认地址信息
     * @return
     */
    @GetMapping("/default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        if (null == addressBook) {
            return R.error("没有找到对象");
        }
        else {
            return R.success(addressBook);
        }
    }

    /**
     * 查询指定用户的全部地址
     * @param addressBook
     * @return
     */
    @GetMapping("/list")
    public R<List<AddressBook>> list(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        return R.success(addressBookService.list(queryWrapper));
    }

}
