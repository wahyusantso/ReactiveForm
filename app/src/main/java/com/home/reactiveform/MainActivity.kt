package com.home.reactiveform

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.home.reactiveform.databinding.ActivityMainBinding
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import io.reactivex.functions.Function3

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val text = "hello"
        val emailStream = RxTextView.textChanges(activityMainBinding.edEmail)
            .skipInitialValue()
            .map { email ->
                !Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        emailStream.subscribe {
            showEmailExitsAlert(it)
        }

        val passwordStream = RxTextView.textChanges(activityMainBinding.edPassword)
            .skipInitialValue()
            .map { password ->
                password.length < 6
            }
        passwordStream.subscribe {
            showPasswordMinimalAlert(it)
        }

        val passwordConfirmationStream = Observable.merge(
            RxTextView.textChanges(activityMainBinding.edPassword)
                .map { password ->
                    password.toString() != activityMainBinding.edConfirmPassword.text.toString()
                },
            RxTextView.textChanges(activityMainBinding.edConfirmPassword)
                .map { confirmPassword ->
                    confirmPassword.toString() != activityMainBinding.edPassword.text.toString()
                }
        )
        passwordConfirmationStream.subscribe {
            showPasswordConfirmatonAlert(it)
        }

        val invalidFieldStream = Observable.combineLatest(
            emailStream,
            passwordStream,
            passwordConfirmationStream,
            Function3 { emailInvalid: Boolean, passwordInvalid: Boolean, passwordConfirmationInvalid: Boolean ->
                !emailInvalid && !passwordInvalid && !passwordConfirmationInvalid
            }
        )
        invalidFieldStream.subscribe { isvalid ->
            if (isvalid) {
                activityMainBinding.btnRegister.isEnabled = true
                activityMainBinding.btnRegister.setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.purple_500))
                activityMainBinding.btnRegister.setOnClickListener {
                    Toast.makeText(this, "Reactive Running", Toast.LENGTH_LONG).show()
                }
            } else {
                activityMainBinding.btnRegister.isEnabled = false
                activityMainBinding.btnRegister.setBackgroundColor(ContextCompat.getColor(this@MainActivity, android.R.color.darker_gray))
            }
        }
    }

    private fun showEmailExitsAlert(isNotValid: Boolean) {
        activityMainBinding.edEmail.error = if (isNotValid) getString(R.string.email_not_valid) else null
    }
    private fun showPasswordMinimalAlert(isNotValid: Boolean) {
        activityMainBinding.edPassword.error = if (isNotValid) getString(R.string.password_not_valid) else null
    }
    private fun showPasswordConfirmatonAlert(isNotValid: Boolean) {
        activityMainBinding.edConfirmPassword.error = if (isNotValid) getString(R.string.password_not_same) else null
    }
}
/*
- RxTextView.textChanges(activityMainBinding.edEmail) = membaca setiap perubahan pada EditText dan mengubahnya menjadi data stream.
- skipInitialValue() untuk menghiraukan input awal. Hal ini bertujuan supaya aplikasi tidak langsung menampilkan eror pada saat pertama kali dijalankan.
- operator map akan memeriksa apakah format valid. Jika format tidak valid maka ia akan mengembalikan nilai TRUE.
- operator combineLatest menggabungkan data dan mengubah data di dalamnya
- perator merge hanya menggabungkan datanya saja
 */