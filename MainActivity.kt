package com.example.face

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextUsername = findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textViewSignup = findViewById<TextView>(R.id.buttonsign)

        // 로그인 버튼 클릭 시 처리
        fun navigateToLobby() {
            val intent = Intent(this, LobbyActivity::class.java)
            startActivity(intent)
            finish() // 로그인 액티비티 종료
        }
        fun navigateToSign() {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

// 로그인 버튼 클릭 시 로비 화면으로 이동
        buttonLogin.setOnClickListener {
            navigateToLobby()
        }

        // 회원가입 텍스트 클릭 시 처리
        textViewSignup.setOnClickListener {
            navigateToSign()
        }
    }
}