package com.imperialnet.foodstore.users.application.ports.in;

import com.imperialnet.foodstore.users.infrastructure.web.dto.UpdateUserRequest;
import com.imperialnet.foodstore.users.infrastructure.web.dto.UserResponse;

public interface UpdateUserUsecase {

    UserResponse execute(Long id, UpdateUserRequest request, String updatedBy);
}
