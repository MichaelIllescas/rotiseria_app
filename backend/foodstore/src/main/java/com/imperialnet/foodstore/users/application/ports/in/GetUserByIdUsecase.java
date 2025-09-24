package com.imperialnet.foodstore.users.application.ports.in;

import com.imperialnet.foodstore.users.infrastructure.web.dto.UserResponse;

public interface GetUserByIdUsecase {
    UserResponse execute(Long id);
}
