//package com.example.DATN_WebFiveTus.filter;
//
//import com.example.DATN_WebFiveTus.entity.Account;
//import com.example.DATN_WebFiveTus.repository.AccountRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//
//public class CustomUserDetailService implements UserDetailsService {
//    private AccountRepository accountRepository;
//
//    @Autowired
//    public CustomUserDetailService(AccountRepository accountRepository) {
//        this.accountRepository = accountRepository;
//    }
//
//    @Override
//    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
//
//        Account account = accountRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
//                .orElseThrow(() -> new UsernameNotFoundException("User not exists by Username or Email"));
//
//        Set<GrantedAuthority> authorities = account.getRoles().stream()
//                .map((role) -> new SimpleGrantedAuthority(role.getAuthority()))
//                .collect(Collectors.toSet());
//
//        return new org.springframework.security.core.userdetails.User(
//                usernameOrEmail,
//                account.getPasswords(),
//                authorities
//        );
//    }
//}