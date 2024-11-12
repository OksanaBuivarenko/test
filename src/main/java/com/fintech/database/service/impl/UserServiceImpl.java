package com.fintech.database.service.impl;

import com.fintech.database.dto.request.CreateUserRq;
import com.fintech.database.dto.response.PageRs;
import com.fintech.database.dto.response.UserRs;
import com.fintech.database.entity.User;
import com.fintech.database.entity.UserRole;
import com.fintech.database.exception.AlreadyExistsException;
import com.fintech.database.exception.ObjectNotFoundException;
import com.fintech.database.mapper.UserMapper;
import com.fintech.database.repository.UserRepository;
import com.fintech.database.security.AppUserDetails;
import com.fintech.database.service.UserRoleService;
import com.fintech.database.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRoleService roleService;

    private final UserMapper userMapper;

    @Value("${admin.password}")
    private String password;

    @Override
    public void createUser(CreateUserRq createUserRq) {
        if (!isUserExists(createUserRq)) {
            User user = new User();
            user.setName(createUserRq.getUsername());
            user.setEmail(createUserRq.getEmail());
            user.setPassword(passwordEncoder.encode(createUserRq.getPassword()));
            user.getRoles().add(roleService.getRole("ROLE_USER"));
            userRepository.save(user);
        }
    }

    public boolean isUserExists(CreateUserRq createUserRq) {
        if (userRepository.existsByName(createUserRq.getUsername())) {
            throw new AlreadyExistsException("Username already exists!");
        }
        if (userRepository.existsByEmail(createUserRq.getEmail())) {
            throw new AlreadyExistsException("Email already exists!");
        }
        return false;
    }

    @Override
    public void createDefaultAdmin() {
        if (!userRepository.existsByName("Admin")) {
            User user = new User();
            user.setName("Admin");
            user.setEmail("admin@mail.ru");
            user.setPassword(passwordEncoder.encode(password));
            user.getRoles().add(roleService.getRole("ROLE_ADMIN"));
            userRepository.save(user);
        }
    }

    @Override
    public void changePassword(String password) {
        User user = getUserByCurrentPrincipalId();
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    public User getUserByCurrentPrincipalId() {
        var currentPrincipal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (currentPrincipal instanceof AppUserDetails userDetails) {
            Long id = userDetails.getId();
            return getUserById(id);
        }
        return null;
    }

    @Override
    public PageRs<List<UserRs>> getAllUserRs() {
        return PageRs.<List<UserRs>>builder()
                .data(userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList()))
                .build();
    }

    @Override
    public PageRs<UserRs> getUserRsById(Long id) {
        return PageRs.<UserRs>builder()
                .data(userMapper.toDto(getUserById(id)))
                .build();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("User", id));
    }

    @Override
    public PageRs<String> deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
        return PageRs.<String>builder()
                .data("User with id " + id + " delete.")
                .build();
    }

    @Override
    public PageRs<String> helloUser() {
        return PageRs.<String>builder()
                .data("Hello user, " + SecurityContextHolder.getContext().getAuthentication().getName() + "!")
                .build();
    }

    @Override
    public PageRs<String> addNewRoleById(Long id, String role) {
        User user = getUserById(id);
        UserRole userRole = roleService.getRole(role);
        user.getRoles().add(userRole);
        userRepository.save(user);

        return PageRs.<String>builder()
                .data("Пользователю с id " + id + " добавлена новая роль - " + role)
                .build();
    }
}