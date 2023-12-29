package tech.cognix.sauntatonttu.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import tech.cognix.sauntatonttu.R
import tech.cognix.sauntatonttu.utils.SharedPreferencesApp

class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPreferencesApp: SharedPreferencesApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPreferencesApp = SharedPreferencesApp(this)

        Handler().postDelayed({
            if (sharedPreferencesApp.getToken() != null) {
                var intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                var intent = Intent(this, SignupActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, 1500)
    }
}