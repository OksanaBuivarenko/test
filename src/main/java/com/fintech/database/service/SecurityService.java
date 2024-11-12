package com.fintech.database.service;

import com.fintech.database.dto.request.ChangePasswordRq;
import com.fintech.database.dto.request.CreateUserRq;
import com.fintech.database.dto.request.LoginRq;
import com.fintech.database.dto.response.AuthRs;
import com.fintech.database.dto.response.PageRs;

import java.security.Principal;

public interface SecurityService {
    
    PageRs<AuthRs> authenticate(LoginRq loginRq);

    PageRs<String> register(CreateUserRq createUserRq);

    PageRs<String> changePasswordByEmail(ChangePasswordRq changePasswordRq);

    PageRs<String> logout();

    PageRs<String> getConfirmationCodeForChangePasswordByEmail();
}