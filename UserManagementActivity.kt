package com.example.face

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UserManagementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage)

        val listViewUsers = findViewById<ListView>(R.id.listViewUsers)
        val buttonDelete = findViewById<Button>(R.id.buttonDelete)
        val buttonModify = findViewById<Button>(R.id.buttonModify)

        // 사용자 목록을 가져와서 ListView에 표시하는 기능을 구현하세요.
        val users = listOf("사용자1", "사용자2", "사용자3") // 예시로 사용자 목록을 리스트로 만듭니다.
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, users)
        listViewUsers.adapter = adapter

        // 삭제 버튼 클릭 시 처리
        buttonDelete.setOnClickListener {
            val selectedUser = listViewUsers.selectedItem as String?
            if (selectedUser != null) {
                deleteUser(selectedUser)
            } else {
                Toast.makeText(this, "삭제할 사용자를 선택하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 변경 버튼 클릭 시 처리
        buttonModify.setOnClickListener {
            val selectedUser = listViewUsers.selectedItem as String?
            if (selectedUser != null) {
                modifyUser(selectedUser)
            } else {
                Toast.makeText(this, "변경할 사용자를 선택하세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 사용자 삭제 기능을 구현하세요.
    private fun deleteUser(userName: String) {
        // 사용자 삭제 기능을 구현하세요.
        Toast.makeText(this, "$userName 사용자를 삭제했습니다.", Toast.LENGTH_SHORT).show()
    }

    // 사용자 변경 기능을 구현하세요.
    private fun modifyUser(userName: String) {
        // 사용자 변경 기능을 구현하세요.
        Toast.makeText(this, "$userName 사용자 정보를 변경합니다.", Toast.LENGTH_SHORT).show()
    }
}
