package com.example.face

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfRect
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class UserRegistrationActivity : AppCompatActivity() {

    private var faceCascade: CascadeClassifier? = null
    private lateinit var cameraPreview: FrameLayout
    private lateinit var imageViewPreview: ImageView
    private lateinit var buttonTakePhoto: Button
    private lateinit var dialogNameInput: AlertDialog
    private lateinit var editTextNameDialog: EditText
    private lateinit var buttonConfirmDialog: Button

    private var currentBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        cameraPreview = findViewById(R.id.camera_preview)
        imageViewPreview = findViewById(R.id.image_view_preview)
        buttonTakePhoto = findViewById(R.id.button_take_photo)

        val loaderCallback: BaseLoaderCallback = object : BaseLoaderCallback(this) {
            override fun onManagerConnected(status: Int) {
                when (status) {
                    LoaderCallbackInterface.SUCCESS -> {
                        Log.i(TAG, "OpenCV loaded successfully")
                        loadCascade()

                        buttonTakePhoto.setOnClickListener { takePhoto() }
                    }
                    else -> {
                        super.onManagerConnected(status)
                    }
                }
            }
        }
        OpenCVLoader.initDebug()
        loaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS)
    }

    private fun loadCascade() {
        try {
            val inputStream: InputStream = resources.openRawResource(R.raw.haarcascade_frontalface_alt_tree)
            val cascadeDir: File = getDir("cascade", Activity.MODE_PRIVATE)
            val cascadeFile = File(cascadeDir, "raw/haarcascade_frontalface_alt_tree.xml")
            val os = FileOutputStream(cascadeFile)

            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                os.write(buffer, 0, bytesRead)
            }
            inputStream.close()
            os.close()

            faceCascade = CascadeClassifier(cascadeFile.absolutePath)
        } catch (e: Exception) {
            Log.e(TAG, "Error loading cascade", e)
        }
    }

    private fun takePhoto() {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val image = data?.extras?.get("data") as? Bitmap
            image?.let {
                imageViewPreview.setImageBitmap(it)
                currentBitmap = it
                recognizeFace(it)
            }
        }
    }

    private fun recognizeFace(image: Bitmap) {
        val grayImage = Mat()
        Imgproc.cvtColor(Mat(image.height, image.width, CvType.CV_8UC4), grayImage, Imgproc.COLOR_RGBA2GRAY)

        val faces = MatOfRect()
        faceCascade?.detectMultiScale(grayImage, faces)

        if (faces.toArray().isNotEmpty()) {
            // 얼굴 인식 성공
            showNameInputDialog()
        } else {
            // 얼굴 인식 실패
            showRetryPopup()
        }
    }

    private fun showNameInputDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogView = layoutInflater.inflate(R.layout.dialog_name_input, null)
        builder.setView(dialogView)

        editTextNameDialog = dialogView.findViewById(R.id.edit_text_name_dialog)
        buttonConfirmDialog = dialogView.findViewById(R.id.button_confirm_dialog)

        dialogNameInput = builder.create()

        buttonConfirmDialog.setOnClickListener {
            val name = editTextNameDialog.text.toString()
            if (name.isNotBlank()) {
                saveFaceInfo(name)
                dialogNameInput.dismiss()
            } else {
                editTextNameDialog.error = "이름을 입력하세요."
            }
        }

        dialogNameInput.show()
    }

    private fun showRetryPopup() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("얼굴 인식 실패")
        builder.setMessage("얼굴이 인식되지 않았습니다. 다시 시도하시겠습니까?")

        builder.setPositiveButton("다시 시도") { _, _ ->
            takePhoto()
        }
        builder.setNegativeButton("취소", null)

        builder.create().show()
    }

    private fun saveFaceInfo(name: String) {
        // 이미지와 이름을 저장하는 기능을 구현하세요.
        Log.d(TAG, "Face saved with name: $name")
    }

    companion object {
        private const val TAG = "UserRegistrationActivity"
        private const val REQUEST_IMAGE_CAPTURE = 1
    }
}
