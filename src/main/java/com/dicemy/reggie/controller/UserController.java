package com.dicemy.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dicemy.reggie.common.R;
import com.dicemy.reggie.entity.User;
import com.dicemy.reggie.service.UserService;
import com.dicemy.reggie.utils.QQMailUtils;
import com.dicemy.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private QQMailUtils qqMailUtils;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送邮箱验证码
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        String email = user.getPhone();
        if (StringUtils.isNotEmpty(email)) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            qqMailUtils.sendSimpleMail(email,"「Dicemy」验证码","验证码："+code+"，该验证码5分钟内有效。为了保障您的账户安全，请勿向他人泄漏验证码信息。");
            log.info("验证码为：{}",code);
//            session.setAttribute("code", code);
            redisTemplate.opsForValue().set(email, code, 5l, TimeUnit.MINUTES);
            return R.success("邮箱验证码发送成功，请查看！");
        }
        return R.error("验证码发送失败");
    }

    /**
     * 移动端用户登录
     * @param map
     * @param session
     * @return
     */
    @PostMapping("/login")
    private R<User> login(@RequestBody Map map, HttpSession session) {
        String email = map.get("phone").toString();
        String code = map.get("code").toString();
//        Object codeInSession = session.getAttribute("code");
        Object codeInSession = redisTemplate.opsForValue().get(email);
//        log.info("email={}, code={}, codeInSession={}",email,code,codeInSession);
        if (codeInSession != null && codeInSession.equals(code)) {
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone, email);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(email);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            redisTemplate.delete(email);
            return R.success(user);
        }
        return R.error("登录失败");
    }

    @PostMapping("/loginout")
    public R<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return R.success("退出成功");
    }
}
