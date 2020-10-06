package com.example.authapplication.social

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import com.example.authapplication.handlers.PreferenceHandler
import com.example.authapplication.ui.UserDashboardActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*


class TwitterSignInProcedure {

    private val provider : OAuthProvider.Builder? = OAuthProvider.newBuilder("twitter.com")
    private val firebaseAuth : FirebaseAuth = FirebaseAuth.getInstance()

    fun signInWithTwitter(activity: Activity) {
        val pendingResultTask: Task<AuthResult>? = firebaseAuth.pendingAuthResult
        if (pendingResultTask != null) {
            pendingResultTask
                .addOnSuccessListener{authResult ->
                    onSuccessProcess(activity, authResult)
                }
                .addOnFailureListener{
                    onFailureProcess(activity)
                }
        } else {
            startNewSignInFlow(activity)
        }
    }

    private fun startNewSignInFlow(activity: Activity) {
        firebaseAuth
            .startActivityForSignInWithProvider(activity, provider!!.build())
            .addOnSuccessListener { authResult ->
                onSuccessProcess(activity, authResult)
            }
            .addOnFailureListener {
                onFailureProcess(activity)
            }
    }

    private fun onSuccessProcess(activity: Activity, authResult: AuthResult) {
        val preferenceHandler = PreferenceHandler(activity)
        preferenceHandler.setUserAuthProvider(PreferenceHandler.TWITTER_AUTH_PROVIDER)
        val credential = authResult.credential!!
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(activity, "Sign In Successful.", Toast.LENGTH_SHORT).show()
                    val user = firebaseAuth.currentUser
                    updateUI(activity, user!!)
                } else {
                    Toast.makeText(activity, "Authentication Failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun onFailureProcess(activity: Activity) {
        Toast.makeText(activity, "Twitter Sign In Failed.", Toast.LENGTH_SHORT).show()
    }

    private fun updateUI(activity: Activity, user: FirebaseUser) {
        val callUserDashboardIntent = Intent(activity, UserDashboardActivity::class.java)
        callUserDashboardIntent.putExtra(GoogleSignInProcedure.USER_NAME, user.displayName)
        callUserDashboardIntent.putExtra(GoogleSignInProcedure.USER_EMAIL, user.email)
        callUserDashboardIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(callUserDashboardIntent)
    }

}