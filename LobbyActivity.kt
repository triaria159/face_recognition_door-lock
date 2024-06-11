package com.example.face

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView

class LobbyActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lobby_activity)

        // 사용자 아이콘

        val imageViewUserIcon = findViewById<ImageView>(R.id.imageViewUser)
        val ManageUser = findViewById<Button>(R.id.buttonManageUsers)
        val RegisterUser = findViewById<Button>(R.id.buttonRegisterUser)

        // 로비 액티비티에서 개인정보 액티비티로 이동하는 함수
        fun navigateToProfile() {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        // 사용자 아이콘 클릭 시 개인정보 화면으로 이동
        imageViewUserIcon.setOnClickListener {
            navigateToProfile()
        }
        // 로비 액티비티에서 사용자 등록 액티비티로 이동하는 함수
        fun navigateToUserRegistration() {
            val intent = Intent(this, UserRegistrationActivity::class.java)
            startActivity(intent)
        }

        // 사용자 등록 버튼 클릭 시 사용자 등록 화면으로 이동
        RegisterUser.setOnClickListener {
            navigateToUserRegistration()
        }
        // 로비 액티비티에서 사용자 관리 액티비티로 이동하는 함수
        fun navigateToUserManagement() {
            val intent = Intent(this, UserManagementActivity::class.java)
            startActivity(intent)
        }

        // 사용자 관리 버튼 클릭 시 사용자 관리 화면으로 이동
        ManageUser.setOnClickListener {
            navigateToUserManagement()
        }

    }
}