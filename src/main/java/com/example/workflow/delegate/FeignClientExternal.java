package com.example.workflow.delegate;

import com.example.workflow.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "client", url = "${application.external.url}")
public interface FeignClientExternal {

    @GetMapping(path = "/by-login/{login}")
    UserDto getUser(@PathVariable("login") String login);
}
