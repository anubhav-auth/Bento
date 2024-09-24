package com.anubhav_auth.bento.authentication

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.TimeUnit

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    private val tag = "AUTH"
    private var storedVerificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState = _authState.asStateFlow()


    init {
        checkAuthStatus()
    }

    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.d(tag, "onVerificationCompleted:$credential")
            signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            Log.w(tag, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                Log.d(tag, "Invalid request")
            } else if (e is FirebaseTooManyRequestsException) {
                Log.d(tag, "The SMS quota for the project has been exceeded")
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                Log.d(tag, "Missing Activity for recaptcha")
            }
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            Log.d(tag, "onCodeSent:$verificationId")

            storedVerificationId = verificationId
            resendToken = token
        }

    }

    fun verifyCode(code: String) {
        val verificationId = storedVerificationId
        if (verificationId != null) {
            _authState.update {
                AuthState.Loading
            }
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            signInWithPhoneAuthCredential(credential)
        } else {
            AuthState.Error("Verification ID is null")
            Log.w(tag, "Verification ID is null")
        }
    }

    fun verifyPhoneNumber(phoneNumber: String, context: Context) {
        val options = PhoneAuthOptions.newBuilder()
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(context as Activity)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        _authState.update {
            AuthState.Loading
        }

        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "signInWithCredential:success")
                    val user = task.result?.user
                    Log.d(tag, user?.phoneNumber.toString())
                    _authState.update {
                        AuthState.Authenticated
                    }
                } else {
                    Log.w(tag, "signInWithCredential:failure", task.exception)
                    _authState.update {
                        AuthState.Error(task.exception?.message ?: "Something went wrong")
                    }
                }
            }
    }

    private fun checkAuthStatus() {
        if (auth.currentUser == null) {
            _authState.update {
                AuthState.Unauthenticated
            }
        } else {
            _authState.update {
                AuthState.Authenticated
            }
        }
    }

}

sealed class AuthState {
    data object Authenticated : AuthState()
    data object Unauthenticated : AuthState()
    data object Loading : AuthState()
    data class Error(val message: String) : AuthState()
}