package com.icefruit.courseteachingsystem.contoller;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.icefruit.courseteachingsystem.api.DataResponse;
import com.icefruit.courseteachingsystem.api.DtoList;
import com.icefruit.courseteachingsystem.api.ListResponse;
import com.icefruit.courseteachingsystem.api.Response;
import com.icefruit.courseteachingsystem.auth.AuthConstant;
import com.icefruit.courseteachingsystem.auth.Authorize;
import com.icefruit.courseteachingsystem.dto.*;
import com.icefruit.courseteachingsystem.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    static ILogger logger = SLoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public ListResponse<UserDto> getUsers(@RequestParam int offset, @RequestParam @Min(0) int limit){
        DtoList<UserDto> list = userService.list(offset, limit);
        return new ListResponse<>(list);

    }

    @GetMapping("/{userId}")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public DataResponse<UserDto> getUser(@PathVariable long userId){
        final UserDto userDto = userService.get(userId);
        return new DataResponse<>(userDto);
    }

    @PostMapping
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public DataResponse<UserDto> create(@RequestBody @Valid CreateUserRequest request){
        UserDto userDto = userService.create(request.getType(), request.getPhoneNumber(),
                request.getName(), request.getPassword());
        return new DataResponse<>(userDto);
    }

    @PostMapping("/verify_password")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER
    })
    public DataResponse<UserDto> verifyPassword(@RequestBody @Valid VerifyPasswordRequest request){
        final UserDto userDto = userService.verifyPassword(request.getPhoneNumber(), request.getPassword());
        return new DataResponse<>(userDto);
    }

    @PutMapping("/{id}")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public DataResponse<UserDto> update(@PathVariable long id,
                                          @RequestBody @Valid UpdateUserRequest request){
        final UserDto userDto = userService.update(id, request.getType(),
                request.getPhoneNumber(), request.getName(), request.getPassword());
        return new DataResponse<>(userDto);
    }

    @DeleteMapping("/{id}")
    @Authorize(value = {
            AuthConstant.AUTHORIZATION_SUPPORT_USER,
            AuthConstant.AUTHORIZATION_AUTHENTICATED_USER
    })
    public Response delete(@PathVariable long id){
        userService.delete(id);
        return new Response();
    }
}
