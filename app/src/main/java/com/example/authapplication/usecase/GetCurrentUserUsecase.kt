package com.example.authapplication.usecase

import com.example.authapplication.repository.UserRepository
import com.google.firebase.auth.FirebaseUser

interface GetCurrentUserUseCase {
    fun getCurrentUser() : FirebaseUser
}

class GetCurrentUserUseCaseImpl(private val userRepository: UserRepository) : GetCurrentUserUseCase {
    override fun getCurrentUser(): FirebaseUser = userRepository.getCurrentUser()!!
}