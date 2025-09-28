package com.imperialnet.foodstore.users.application.ports.in;

import com.imperialnet.foodstore.users.infrastructure.web.dto.ChangePasswordRequest;
import com.imperialnet.foodstore.users.infrastructure.web.dto.UpdateUserRequest;
import com.imperialnet.foodstore.users.infrastructure.web.dto.UserResponse;

public interface UpdateUserUsecase {

    UserResponse execute(Long id, UpdateUserRequest request, String updatedBy);

    void desactivate(Long id, String updatedBy);

    void activate (Long id, String updatedBy);

    void changePassword(Long id, ChangePasswordRequest request, String updatedBy);
}
