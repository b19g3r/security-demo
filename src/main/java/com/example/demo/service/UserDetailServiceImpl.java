package com.example.demo.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Users;
import com.example.demo.mapper.UsersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 通过实现 UserDetailService 接口 实现自定义
 */
@Service
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsersMapper usersMapper;

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested.
     * 根据用户名找到用户。 在实际的实现中，搜索可能区分大小写，或者不区分大小写，具体取决于实现实例的配置方式。
     * 在这种情况下，返回的UserDetails对象的用户名可能与实际请求的用户名不同。
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final Users user = usersMapper.selectOne(new QueryWrapper<Users>().eq("username", username));

        if (user == null) {
            log.error("user not found, username={}", username);
            throw new UsernameNotFoundException("username=" + username + " not exists");
        }

        return new User(user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                AuthorityUtils.createAuthorityList("admins"));
    }
}