package com.library.loan.client;

import com.library.loan.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    UserDTO findById(@PathVariable Long id);

    @GetMapping("/api/users/username/{username}")
    UserDTO findByUsername(@PathVariable String username);
}