package com.example.authapplication.usecase

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.authapplication.handlers.PreferenceHandler
import com.example.authapplication.repository.UserRepository
import com.example.authapplication.social.FacebookLoginProcedure
import com.example.authapplication.social.GoogleSignInProcedure
import com.example.authapplication.social.TwitterSignInProcedure
import com.facebook.AccessToken
import java.lang.Exception

interface SignOutUserUseCase {
    fun signOutUser() : Boolean
}

class SignOutUserUseCaseImpl(
    private val context: Context,
    private val userRepository: UserRepository,
    private val preferenceHandler: PreferenceHandler
) : SignOutUserUseCase {
    override fun signOutUser() : Boolean {
        return try {
            if (preferenceHandler.getUserAuthProvider() == PreferenceHandler.GOOGLE_AUTH_PROVIDER) {
                val googleSignInProcedure = GoogleSignInProcedure(context)
                googleSignInProcedure.setGoogleSignInClient()
                userRepository.signOutUser()
                googleSignInProcedure.getGoogleSignInClient().signOut()
            } else if (preferenceHandler.getUserAuthProvider() == PreferenceHandler.FACEBOOK_AUTH_PROVIDER) {
                val facebookLoginProcedure = FacebookLoginProcedure(context)
                userRepository.signOutUser()
                facebookLoginProcedure.logOutUser()
            } else if (preferenceHandler.getUserAuthProvider() == PreferenceHandler.TWITTER_AUTH_PROVIDER) {
                userRepository.signOutUser()
            }
            Toast.makeText(context, "Signed Out Successfully.", Toast.LENGTH_SHORT).show()
            true
        } catch (error : Exception) {
            Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            false
        }
    }
}