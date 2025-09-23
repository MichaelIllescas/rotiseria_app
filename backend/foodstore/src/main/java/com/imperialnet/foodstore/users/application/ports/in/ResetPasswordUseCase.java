package com.imperialnet.foodstore.users.application.ports.in;

public interface ResetPasswordUseCase {
    void execute(String token, String newPassword);
}