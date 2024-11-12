package com.fintech.database.service.impl;

import com.fintech.database.dto.request.ChangePasswordRq;
import com.fintech.database.dto.request.CreateUserRq;
import com.fintech.database.dto.request.LoginRq;
import com.fintech.database.dto.response.AuthRs;
import com.fintech.database.dto.response.PageRs;
import com.fintech.database.security.AppUserDetails;
import com.fintech.database.security.jwt.JwtUtils;
import com.fintech.database.service.MailService;
import com.fintech.database.service.SecurityService;
import com.fintech.database.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final UserService userService;

    private final MailService mailService;

    @Override
    public PageRs<AuthRs> authenticate(LoginRq loginRq) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRq.getUsername(),
                loginRq.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AppUserDetails userDetails = (AppUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        AuthRs authRs = AuthRs.builder()
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .username(userDetails.getUsername())
                .roles(roles)
                .token(jwtUtils.generateJwtToken(userDetails, loginRq.getRememberMe()))
                .build();
        return PageRs.<AuthRs>builder()
                .data(authRs)
                .build();
    }

    @Override
    public PageRs<String> register(CreateUserRq createUserRq) {
        userService.createUser(createUserRq);
        return PageRs.<String>builder()
                .data("User create successfully!")
                .build();
    }

    @Override
    public PageRs<String> getConfirmationCodeForChangePasswordByEmail() {
        mailService.send();
        return PageRs.<String>builder()
                .data("Confirmation code for change password send to your email!")
                .build();
    }

    @Override
    public PageRs<String> changePasswordByEmail(ChangePasswordRq changePasswordRq) {
        String message;
        if (changePasswordRq.getCode().equals("0000")) {
            userService.changePassword(changePasswordRq.getNewPassword());
            message = "Password changed successfully!";
        } else message = "Code is not valid!";
        return PageRs.<String>builder()
                .data(message)
                .build();
    }

    @Override
    public PageRs<String> logout() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            Long userId = userDetails.getId();
            jwtUtils.deleteTokenFromDb(userId);
        }
        return PageRs.<String>builder()
                .data("User logged out!")
                .build();
    }
}