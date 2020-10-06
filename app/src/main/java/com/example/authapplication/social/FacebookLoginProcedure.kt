package com.example.authapplication.social

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.authapplication.R
import com.example.authapplication.handlers.PreferenceHandler
import com.example.authapplication.ui.UserDashboardActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class FacebookLoginProcedure(private val context: Context) {

    companion object {
        val FACEBOOK_RETURN_CODE = 150
    }

    private val managerCallback : CallbackManager by lazy { CallbackManager.Factory.create() }
    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val readPermissions : Array<String> by lazy { context.resources.getStringArray(R.array.read_permissions) }

    fun setUpFacebookLogin(activity : Activity, progressBar: ProgressBar) {
        LoginManager.getInstance().logInWithReadPermissions(activity, readPermissions.asList())
        LoginManager.getInstance().registerCallback(managerCallback, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                Log.d("Facebooks", "facebook:onSuccess:$result")
                firebaseAuthFacebookLogin(result!!.accessToken, progressBar)
            }

            override fun onCancel() {
                Log.d("Facebooks", "facebook:onCancel")
                Toast.makeText(context, "Sign in cancelled.", Toast.LENGTH_SHORT).show()
            }

            override fun onError(error: FacebookException?) {
                Log.d("Facebooks", "facebook:onError")
                Toast.makeText(context, "Error occured while Signing in.", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun firebaseAuthFacebookLogin(token: AccessToken, progressBar: ProgressBar) {

        val credential = FacebookAuthProvider.getCredential(token.token)
        val preferenceHandler = PreferenceHandler(context)
        preferenceHandler.setUserAuthProvider(PreferenceHandler.FACEBOOK_AUTH_PROVIDER)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Facebooks", "signInWithCredential:success")
                    Toast.makeText(context, "Sign In Successful.", Toast.LENGTH_SHORT).show()
                    val user = firebaseAuth.currentUser
                    updateUI(user!!, progressBar)
                } else {
                    Log.d("Facebooks", "signInWithCredential:failure ${task.exception}")
                    onSignInFailed(progressBar)
                }
            }
    }

    private fun onSignInFailed(progressBar: ProgressBar) {
        Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
        progressBar.visibility = View.GONE
    }

    private fun updateUI(user : FirebaseUser, progressBar: ProgressBar) {
        val callUserDashboardIntent = Intent(context, UserDashboardActivity::class.java)
        callUserDashboardIntent.putExtra(GoogleSignInProcedure.USER_NAME, user.displayName)
        callUserDashboardIntent.putExtra(GoogleSignInProcedure.USER_EMAIL, user.email)
        callUserDashboardIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        progressBar.visibility = View.GONE
        context.startActivity(callUserDashboardIntent)
    }

    fun callOnActivityResult(
        requestCode : Int,
        resultCode : Int,
        data : Intent
    ) {
        managerCallback.onActivityResult(requestCode, resultCode, data)
    }

    fun logOutUser() {
        LoginManager.getInstance().logOut()
    }
}