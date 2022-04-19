package com.laoyitiao.auth.config;

import com.laoyitiao.auth.entities.*;
import com.laoyitiao.auth.repositories.AccountStatusRepository;
import com.laoyitiao.auth.repositories.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class JdbcUserDetailsService implements UserDetailsService{

    @Resource
    private UserRepository userRepository;

    @Resource
    private AccountStatusRepository accountStatusRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Optional<User> userOptional = userRepository.findById(username);
        if (!userOptional.isPresent()){
            throw new BadCredentialsException("用户名或密码错误!");
        }
        User user = userOptional.get();
        AccountStatus accountStatus = user.getAccountStatus();

        if (accountStatus!=null){
            if (accountStatus.getAccountIsLocked() != null) {
                Instant accountIsLocked = accountStatus.getAccountIsLocked();
                if(accountIsLocked.isAfter(Instant.now())){
                    throw new LockedException("您的账户因["+accountStatus.getActionNotes()+"],已被锁定至： "+ accountIsLocked.atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss")));
                }else {
                    // 锁定时间已过自动解除账户锁定,并清除锁定原因
                    AccountStatus newStatus = new AccountStatus(user.getId(),accountStatus.getAccountIsExpired(),null,accountStatus.getCredentialsIsExpired(),null);
                    accountStatusRepository.save(newStatus);
                }
            }
            if (accountStatus.getAccountIsExpired() != null) {
                if(accountStatus.getAccountIsExpired().isBefore(Instant.now())){
                    throw new AccountExpiredException("您的账户已于: "+accountStatus.getAccountIsExpired().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"))+"过期！");
                }
            }
            if (accountStatus.getCredentialsIsExpired() != null) {
                if(accountStatus.getCredentialsIsExpired().isBefore(Instant.now())){
                    throw new CredentialsExpiredException("您的账户凭证已于： "+accountStatus.getCredentialsIsExpired().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm:ss"))+" 过期！");
                }
            }
        }

        HashSet<GrantedAuthority> authoritySet = new HashSet<>();

        // 获取特权信息
        Set<Authority> authorities = user.getAuthorities();
        if (authorities.size()!=0){
            for (Authority authority : authorities) {
                SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
                System.out.println(grantedAuthority.getAuthority());
                authoritySet.add(grantedAuthority);
            }
        }


        try {
            // 获取用户组权限信息
            Set<GroupAuthority> groupAuthorities = user.getGroupMember().getGroup().getGroupAuthorities();
            // 防止token转换失败
            for (GroupAuthority groupAuthority : groupAuthorities) {
                SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(groupAuthority.getAuthority());
                System.out.println("grantedAuthority = " + grantedAuthority);
                authoritySet.add(grantedAuthority);
            }
        }catch (Exception e){
            throw new AccessDeniedException("无用户组权限，禁止登录！");
        }

        System.out.println("authoritySet = " + authoritySet);

        return new org.springframework.security.core.userdetails.User(
                user.getId(),
                user.getPassword(),
                authoritySet);
    }
}
