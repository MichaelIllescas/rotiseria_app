package com.imperialnet.foodstore.users.application.usecase;

import com.imperialnet.foodstore.users.application.ports.in.DeleteUserUseCase;
import com.imperialnet.foodstore.users.infrastructure.persistence.adapter.UserRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteUser implements DeleteUserUseCase {

    private final UserRepositoryAdapter userRepositoryAdapter;

    @Override
    public void execute(Long userId) {
        userRepositoryAdapter.deleteById(userId);
    }
}
