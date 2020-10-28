package com.sg.blogcms.service;

import com.sg.blogcms.entity.Role;
import com.sg.blogcms.entity.User;
import com.sg.blogcms.model.UserDao;
import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserDao users;

    /**
     * Secures an enabled User obj
     *
     * @param username {String} a valid, activated username
     * @return {UserDetails} a secured account by Spring
     * @throws UsernameNotFoundException if username doesn't exist or us
     *                                   disabled
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = users.readEnabledUserByUsername(username);

        if (user != null) {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

            for (Role role : user.getRoles()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole()));
            }

            return new org.springframework.security.core.userdetails.User(user.getUsername(),
                    user.getPassword(), grantedAuthorities);
        }

        return null;
    }

}
