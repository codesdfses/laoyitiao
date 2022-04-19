package com.laoyitiao.auth.service;

import com.laoyitiao.auth.entities.*;
import com.laoyitiao.auth.repositories.UserAccountRepository;
import com.laoyitiao.auth.repositories.UserRepository;
import com.laoyitiao.auth.utils.AccountValidatorUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.ConnectException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class RegisterService {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private UserRepository userRepository;

    @Resource
    private UserAccountRepository userAccountRepository;

    @Resource
    private RedisTemplate<String, Long> redisTemplate;

    public Object register(String username, String password, String nickname, String emailAddress, String phoneNumber){
        HashMap<String, Object> map = new HashMap<>();
        try {
            if (nickname==null || username==null || password==null) throw new IllegalArgumentException("用户名，密码，昵称不能为空！");
            if (!AccountValidatorUtil.isUsername(username) || !AccountValidatorUtil.isPassword(password)){
                throw new IllegalArgumentException("用户名和密码只能是6-16个字符！");
            }
            nickname = nickname.replaceAll(" ", "");
            if (nickname.length()>8) throw new IllegalArgumentException("昵称长度不能大于8个字符！");
            UserAccount nicknameExists = userAccountRepository.findByAccountNickname(nickname);
            if (nicknameExists!=null) throw new IllegalArgumentException("昵称已存在");
            if (emailAddress!=null){
                if (!AccountValidatorUtil.isEmail(emailAddress)) throw new IllegalArgumentException("邮箱地址格式不正确！");
            }
            if (phoneNumber!=null){
                if (!AccountValidatorUtil.isMobile(phoneNumber)) throw new IllegalArgumentException("手机号格式不正确！");
            }
            if (username.equals(password)){
                throw new IllegalArgumentException("用户名和密码不能相同！");
            }
            Optional<User> admin = userRepository.findById(username.trim());
            if (admin.isPresent()){
                throw new IllegalArgumentException("用户已存在！");
            }
            // 构造用户
            com.laoyitiao.auth.entities.User user = new com.laoyitiao.auth.entities.User(
                    username,
                    passwordEncoder.encode(password),
                    true,null,null,null,null);

            // 构造用户对应的账户详情
            UserAccount userAccount = new UserAccount(
                    user.getId(),
                    nickname,
                    "默认头像地址",
                    "初学乍练",
                    0L,
                    emailAddress,
                    phoneNumber,
                    "默认",
                    new Timestamp(System.currentTimeMillis()).toInstant());

            // 构造新用户特权
            Authority once_blog_push = new Authority(null,user.getId(),"once_blog_push");
            Authority once_file_down = new Authority(null,user.getId(),"once_file_down");
            Set<Authority> set = new HashSet<>();
            set.add(once_blog_push);
            set.add(once_file_down);

            // 构造用户所属的用户组
            GroupMember groupMember = new GroupMember(
                    user.getId(),
                    new Group(1L,"默认用户",null,null));

            // 给用户实体填充账户、权限和用户组
            user.setUserAccount(userAccount);
            user.setAuthorities(set);
            user.setGroupMember(groupMember);

            // 级联保存
            userRepository.save(user);

            map.put("status",true);
            map.put("msg","注册成功！");
            try {
                redisTemplate.opsForValue().setIfAbsent("registrationStatisticsToday",0L);
                redisTemplate.opsForValue().increment("registrationStatisticsToday");
            }catch (Exception e){
                System.out.println("redis统计今日下载时异常");
                e.printStackTrace();
            }
        }catch (IllegalArgumentException e){
            map.put("status",false);
            map.put("msg","注册失败！"+e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
            map.put("status",false);
            map.put("msg","注册失败!服务器异常，请稍后再试！");
        }
        return map;
    }
}
