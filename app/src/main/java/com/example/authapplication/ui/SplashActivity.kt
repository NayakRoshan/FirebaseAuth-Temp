package com.example.authapplication.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.authapplication.R
import com.example.authapplication.networkOperation.FirebaseAuthOperation
import com.example.authapplication.repository.UserRepository
import com.example.authapplication.usecase.GetCurrentUserUseCase
import com.example.authapplication.usecase.GetCurrentUserUseCaseImpl
import com.example.authapplication.usecase.GetLoginStatusUseCase
import com.example.authapplication.usecase.GetLoginStatusUseCaseImpl
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY_TIME : Long = 2000
    private val USER_NAME = "User Name"
    private val USER_EMAIL = "User Email"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val loginStatus : GetLoginStatusUseCase =
                GetLoginStatusUseCaseImpl(UserRepository(FirebaseAuthOperation()))
            val intent = if (loginStatus.loginStatus()) {
                val callUserDashboardActivity = Intent(this@SplashActivity, UserDashboardActivity::class.java)
                val user : GetCurrentUserUseCase = GetCurrentUserUseCaseImpl(UserRepository(FirebaseAuthOperation()))
                val currentUser = user.getCurrentUser()
                callUserDashboardActivity.putExtra(USER_NAME, currentUser.displayName)
                callUserDashboardActivity.putExtra(USER_EMAIL, currentUser.email)
                callUserDashboardActivity
            } else {
                val callSignUpActivity = Intent(this@SplashActivity, SignUpActivity::class.java)
                callSignUpActivity
            }
            splashProgress.visibility = View.GONE
            startActivity(intent)
            finish()
        }, SPLASH_DELAY_TIME)
    }
}