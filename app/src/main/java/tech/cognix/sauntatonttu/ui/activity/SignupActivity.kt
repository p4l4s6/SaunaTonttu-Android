package tech.cognix.sauntatonttu.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import tech.cognix.sauntatonttu.R
import tech.cognix.sauntatonttu.models.User
import tech.cognix.sauntatonttu.network.Status
import tech.cognix.sauntatonttu.ui.viewmodels.AuthViewModel
import tech.cognix.sauntatonttu.utils.Constants
import tech.cognix.sauntatonttu.utils.NetworkUtils
import tech.cognix.sauntatonttu.utils.ProgressBarUtils

class SignupActivity : AppCompatActivity() {
    var authViewModel: AuthViewModel? = null
    var user: User? = null
    lateinit var editTextFirstName: EditText
    lateinit var editTextLastName: EditText
    lateinit var editTextEmail: EditText
    lateinit var editTextPassword: EditText
    lateinit var editTextConfirmPassword: EditText
    lateinit var tvLogin: TextView
    lateinit var btnSignup: Button
    private lateinit var progressBarUtils: ProgressBarUtils
    private lateinit var progressBar: ProgressBar
    private var networkUtils: NetworkUtils = NetworkUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Initialize the EditText variables
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword)
        btnSignup = findViewById(R.id.btnSignup)
        tvLogin = findViewById(R.id.tvLogin)
        tvLogin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        progressBar = findViewById(R.id.progressBar)
        progressBarUtils = ProgressBarUtils(this, progressBar)
        authViewModel = AuthViewModel().initial(this)

        btnSignup.setOnClickListener {
            if (validate()) {
                user = User()
                user!!.first_name = editTextFirstName.text.toString()
                user!!.last_name = editTextLastName.text.toString()
                user!!.email = editTextEmail.text.toString()
                user!!.password = editTextPassword.text.toString()
                user!!.confirm_password = editTextConfirmPassword.text.toString()
                progressBarUtils.showProgressBar()
                authViewModel!!.signup(user!!).observe(this) {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                progressBarUtils.hideProgressBar()
                                if (networkUtils.isSuccess(it.data!!.body()!!.code)) {
                                    Toast.makeText(
                                        this,
                                        "Signup completed successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }

                            Status.ERROR -> {
                                progressBarUtils.hideProgressBar()
                                Toast.makeText(this, "Error! try again", Toast.LENGTH_SHORT).show()
                            }

                            Status.LOADING -> {
                                progressBarUtils.showProgressBar()
                            }
                        }
                    }

                }
            }
        }
    }

    fun validate(): Boolean {
        if (editTextFirstName.text.isNullOrEmpty()) {
            editTextFirstName.error = "First name is required"
            return false
        }
        if (editTextLastName.text.isNullOrEmpty()) {
            editTextLastName.error = "Last name is required"
            return false
        }
        if (editTextEmail.text.isNullOrEmpty()) {
            editTextEmail.error = "Email name is required"
            return false
        }
        if (editTextPassword.text.isNullOrEmpty()) {
            editTextPassword.error = "Password name is required"
            return false
        }
        if (editTextConfirmPassword.text.isNullOrEmpty()) {
            editTextConfirmPassword.error = "Confirm Password is required"
            return false
        }
        return true
    }
}