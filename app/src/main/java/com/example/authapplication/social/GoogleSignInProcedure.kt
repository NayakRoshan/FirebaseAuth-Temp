package com.example.authapplication.social

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.example.authapplication.R
import com.example.authapplication.handlers.PreferenceHandler
import com.example.authapplication.ui.UserDashboardActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class GoogleSignInProcedure(private val context: Context) {

    companion object {
        val GOOGLE_RETURN_CODE = 100
        val USER_NAME = "User Name"
        val USER_EMAIL = "User Email"
    }

    private lateinit var googleClient : GoogleSignInClient
    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun setGoogleSignInConfig() : GoogleSignInOptions {
        return GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(context.getString(R.string.web_client_id))
            .build()
    }

    fun setGoogleSignInClient() {
        googleClient = GoogleSignIn.getClient(context, setGoogleSignInConfig())
    }

    fun getGoogleSignInClient() : GoogleSignInClient = googleClient

    fun getSignInIntent() : Intent = googleClient.signInIntent

    fun getGoogleSignInTask(intent : Intent?)
            : Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(intent)

    fun getSignedInAccount(task : Task<GoogleSignInAccount>)
            : GoogleSignInAccount = task.getResult(ApiException::class.java)!!

    fun firebaseAuthGoogleSignIn(account : GoogleSignInAccount, progressBar: ProgressBar) {
        val accountCredentials = GoogleAuthProvider.getCredential(account.idToken!!, null)
        val preferenceHandler = PreferenceHandler(context)
        preferenceHandler.setUserAuthProvider(PreferenceHandler.GOOGLE_AUTH_PROVIDER)
        firebaseAuth.signInWithCredential(accountCredentials)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Sign In Successful.", Toast.LENGTH_SHORT).show()
                    val user = firebaseAuth.currentUser
                    updateUI(user!!, progressBar)
                } else {
                    onSignInFailed(progressBar)
                }
            }
    }

    fun onSignInFailed(progressBar: ProgressBar) {
        Toast.makeText(context, "Google Sign In Failed.", Toast.LENGTH_SHORT).show()
        progressBar.visibility = View.GONE
    }

    private fun updateUI(user : FirebaseUser, progressBar: ProgressBar) {
        val callUserDashboardIntent = Intent(context, UserDashboardActivity::class.java)
        callUserDashboardIntent.putExtra(USER_NAME, user.displayName)
        callUserDashboardIntent.putExtra(USER_EMAIL, user.email)
        callUserDashboardIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        progressBar.visibility = View.GONE
        context.startActivity(callUserDashboardIntent)
    }

}