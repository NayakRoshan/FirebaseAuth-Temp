package com.example.authapplication.networkOperation

import android.util.Log
import com.firebase.client.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class FirebaseAuthOperation {

    private val firebaseAuth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    fun getCurrentUser() : FirebaseUser? = firebaseAuth.currentUser

    fun isLoggedIn() : Boolean = firebaseAuth.currentUser != null

    fun signOutUser() {
        firebaseAuth.signOut()
    }

}