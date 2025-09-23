package com.imperialnet.foodstore.users.application.ports.in;

public interface ForgotPasswordUseCase {
    void execute(String email);
}