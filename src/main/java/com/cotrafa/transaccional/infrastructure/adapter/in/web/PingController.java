package com.cotrafa.transaccional.infrastructure.adapter.in.web;

import com.cotrafa.transaccional.infrastructure.adapter.in.web.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PingController {

    @GetMapping("/ping")
    public ApiResponse<String> ping() {
        return ApiResponse.success("Pong!", "Backend is running correctly.");
    }
}
