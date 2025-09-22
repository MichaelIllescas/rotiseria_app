package com.imperialnet.foodstore.users.application.ports.in;

import com.imperialnet.foodstore.users.infrastructure.web.dto.UserResponse;

import java.util.List;

public interface GetAllUsersUsecase {

    public List<UserResponse> execute();
}
