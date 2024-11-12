package com.fintech.database.service;

import com.fintech.database.dto.request.CreateUserRq;
import com.fintech.database.dto.response.PageRs;
import com.fintech.database.dto.response.UserRs;

import java.util.List;

public interface UserService {

    void createUser(CreateUserRq createUserRq);

    void createDefaultAdmin();

    void changePassword(String password);

    PageRs<List<UserRs>> getAllUserRs();

    PageRs<UserRs> getUserRsById(Long id);

    PageRs<String> deleteUser(Long id);

    PageRs<String> helloUser();

    PageRs<String> addNewRoleById(Long id, String role);
}