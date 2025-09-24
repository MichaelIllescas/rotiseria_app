package com.imperialnet.foodstore.users.application.ports.in;

import com.imperialnet.foodstore.users.infrastructure.web.dto.CreateUserRequest;
import com.imperialnet.foodstore.users.infrastructure.web.dto.CreateUserResponse;

public interface CreateUserUsecase {

     CreateUserResponse execute(CreateUserRequest req, String createdBy);
}
