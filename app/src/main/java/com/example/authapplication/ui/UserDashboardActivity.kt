package com.example.authapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.authapplication.R
import com.example.authapplication.handlers.PreferenceHandler
import com.example.authapplication.networkOperation.FirebaseAuthOperation
import com.example.authapplication.repository.UserRepository
import com.example.authapplication.social.GoogleSignInProcedure
import com.example.authapplication.usecase.SignOutUserUseCase
import com.example.authapplication.usecase.SignOutUserUseCaseImpl
import kotlinx.android.synthetic.main.activity_user_dashboard.*

class UserDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_dashboard)
        setUpUi()
        setSignOutButton()
    }

    private fun setUpUi() {
        userName.text = intent.getStringExtra(GoogleSignInProcedure.USER_NAME)
        userEmail.text = intent.getStringExtra(GoogleSignInProcedure.USER_EMAIL)
    }

    private fun setSignOutButton() {
        signOutButton.setOnClickListener {
            val signOutUserUseCase : SignOutUserUseCase =
                SignOutUserUseCaseImpl(this, UserRepository(FirebaseAuthOperation()), PreferenceHandler(this))
            val status : Boolean = signOutUserUseCase.signOutUser()
            if (status) {
                val callSignInActivity = Intent(this, SignUpActivity::class.java)
                startActivity(callSignInActivity)
                finish()
            }
        }
    }
}