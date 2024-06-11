package com.example.face

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val editTextUsername = findViewById<EditText>(R.id.editTextUsernameSignup)
        val editTextPassword = findViewById<EditText>(R.id.editTextPasswordSignup)
        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextPhoneNumber = findViewById<EditText>(R.id.editTextPhoneNumber)
        val editTextModelNumber = findViewById<EditText>(R.id.editTextModelNumber)
        val buttonSignup = findViewById<Button>(R.id.buttonSignup)

        // 회원가입 버튼 클릭 시 처리
        buttonSignup.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            val name = editTextName.text.toString()
            val phoneNumber = editTextPhoneNumber.text.toString()
            val modelNumber = editTextModelNumber.text.toString()

            if (username.isEmpty()) {
                editTextUsername.error = "아이디를 입력하세요"
                editTextUsername.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                editTextPassword.error = "비밀번호를 입력하세요"
                editTextPassword.requestFocus()
                return@setOnClickListener
            }

            if (name.isEmpty()) {
                editTextName.error = "이름을 입력하세요"
                editTextName.requestFocus()
                return@setOnClickListener
            }

            if (phoneNumber.isEmpty()) {
                editTextPhoneNumber.error = "전화번호를 입력하세요"
                editTextPhoneNumber.requestFocus()
                return@setOnClickListener
            }

            if (modelNumber.isEmpty()) {
                editTextModelNumber.error = "모델번호를 입력하세요"
                editTextModelNumber.requestFocus()
                return@setOnClickListener
            }

            // 여기에 회원가입 처리 로직을 추가하세요

            // 회원가입 성공 시 로그인 화면으로 이동
            finish()
        }
    }
}
