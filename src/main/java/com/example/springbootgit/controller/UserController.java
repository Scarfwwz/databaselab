package com.example.springbootgit.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springbootgit.mapper.UserMapper;
import com.example.springbootgit.service.UserService;
import com.example.springbootgit.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Transactional
public class UserController {


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    // 新增和修改
    @PostMapping
    public boolean save(@RequestBody User user) {
        // 新增或者更新
        return userService.saveUser(user);
    }

    // 查询所有数据
    @GetMapping
    public List<User> findAll() {
        return userService.list();
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return userService.removeById(id);
    }

    // 分页查询
    //  接口路径：/user/page?pageNum=1&pageSize=10
    // @RequestParam接受
//    limit第一个参数 = (pageNum - 1) * pageSize
    // pageSize
//    @GetMapping("/page")
//    public Map<String, Object> findPage(@RequestParam Integer pageNum,
//                                        @RequestParam Integer pageSize,
//                                        @RequestParam String username) {
//        pageNum = (pageNum - 1) * pageSize;
//        username = "%" + username + "%";
//        List<User> data = userMapper.selectPage(pageNum, pageSize, username);
//        Integer total = userMapper.selectTotal(username);
//        Map<String, Object> res = new HashMap<>();
//        res.put("data", data);
//        res.put("total", total);
//        return res;
//    }

    //分页查询，my-batis-plus的方式
    // 分页查询 - mybatis-plus的方式
    @GetMapping("/page")
    public IPage<User> findPage(@RequestParam Integer pageNum,
                                @RequestParam Integer pageSize,
                                @RequestParam(defaultValue = "") String username,
                                @RequestParam(defaultValue = "") String nickname,
                                @RequestParam(defaultValue = "") String address) {
        //defaultValue为参数指定了默认值，因为如果没有输入的话，查询时会报错
        IPage<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //这里是因为，'%%'查询的时候匹配不到(null)值，在他不为空的时候，再传入进去
        if (!"".equals(username)) {
            queryWrapper.like("username", username);
        }
        if (!"".equals(nickname)) {
            queryWrapper.like("nickname", nickname);
        }
        if (!"".equals(address)) {
            queryWrapper.like("address", address);
        }
        return userService.page(page, queryWrapper);
    }

}
