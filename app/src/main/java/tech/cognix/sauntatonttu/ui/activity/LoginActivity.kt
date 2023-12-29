package tech.cognix.sauntatonttu.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import tech.cognix.sauntatonttu.R
import tech.cognix.sauntatonttu.models.User
import tech.cognix.sauntatonttu.network.Status
import tech.cognix.sauntatonttu.ui.viewmodels.AuthViewModel
import tech.cognix.sauntatonttu.utils.NetworkUtils
import tech.cognix.sauntatonttu.utils.ProgressBarUtils
import tech.cognix.sauntatonttu.utils.SharedPreferencesApp

class LoginActivity : AppCompatActivity() {
    var authViewModel: AuthViewModel? = null
    var user: User? = null

    private lateinit var tvSignup: TextView
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var loginBtn: Button
    private lateinit var progressBarUtils: ProgressBarUtils
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferencesApp: SharedPreferencesApp
    private var networkUtils: NetworkUtils = NetworkUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvSignup = findViewById(R.id.tvSignup)
        progressBar = findViewById(R.id.progressBar)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        loginBtn = findViewById(R.id.btnLogin)

        progressBarUtils = ProgressBarUtils(this, progressBar)
        authViewModel = AuthViewModel().initial(this)
        sharedPreferencesApp = SharedPreferencesApp(this)


        tvSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        loginBtn.setOnClickListener {
            if (validate()) {
                user = User()
                user!!.email = editTextEmail.text.toString()
                user!!.password = editTextPassword.text.toString()
                progressBarUtils.showProgressBar()
                authViewModel!!.login(user!!).observe(this) {
                    it?.let { resource ->
                        when (resource.status) {
                            Status.SUCCESS -> {
                                progressBarUtils.hideProgressBar()
                                if (networkUtils.isSuccess(it.data!!.body()!!.code)) {
                                    sharedPreferencesApp.saveToken(it.data.body()!!.data!!.token)
                                    val intent = Intent(this, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
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

    private fun validate(): Boolean {
        if (editTextEmail.text.isNullOrEmpty()) {
            editTextEmail.error = "Email is required"
            return false
        }
        if (editTextPassword.text.isNullOrEmpty()) {
            editTextPassword.error = "Password is required"
            return false
        }
        return true
    }
}