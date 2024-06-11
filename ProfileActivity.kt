package com.example.face
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val buttonWithdraw = findViewById<Button>(R.id.buttonWithdraw)

        // 회원탈퇴 버튼 클릭 시 처리
        buttonWithdraw.setOnClickListener {
            showWithdrawConfirmationDialog()
        }
    }

    // 확인 다이얼로그 표시
    private fun showWithdrawConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("회원탈퇴")
        builder.setMessage("정말로 탈퇴하시겠습니까?")
        builder.setPositiveButton("확인") { dialogInterface: DialogInterface, i: Int ->
            // 여기에 회원 삭제 코드를 넣으세요
            // 회원 삭제가 완료되면 로그인 화면으로 이동하거나 다른 처리를 하세요
        }
        builder.setNegativeButton("취소", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}
