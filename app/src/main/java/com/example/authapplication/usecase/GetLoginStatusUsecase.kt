package com.example.authapplication.usecase

import com.example.authapplication.repository.UserRepository

interface GetLoginStatusUseCase {
    fun loginStatus() : Boolean
}

class GetLoginStatusUseCaseImpl(private val userRepository: UserRepository) : GetLoginStatusUseCase {
    override fun loginStatus(): Boolean = userRepository.isLoggedIn()
}