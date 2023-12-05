package com.example.motimates

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.motimates.databinding.ActivityAchievementBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class Achievement : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding = ActivityAchievementBinding.inflate(layoutInflater) //oncreate 외부에서도 뷰바인딩 사용
        lateinit var filePath: String

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_achievement)
        setContentView(binding.root)

        //갤러리 앱 연동
        val requestGalleryLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            try {
                val calRatio= calculateInSampleSize(it.data?.data!!,
                    resources.getDimensionPixelSize(R.dimen.imgSize),
                    resources.getDimensionPixelSize(R.dimen.imgSize))
                val option= BitmapFactory.Options()
                option.inSampleSize= calRatio

                //이미지 로딩
                var inputStream= contentResolver.openInputStream(it.data?.data!!)
                val bitmap= BitmapFactory.decodeStream(inputStream, null, option)
                inputStream?.close()
                inputStream = null

                bitmap?.let {
                    binding.proofImage.setImageBitmap(bitmap)
                }?: let{
                    Log.d("로그처리", "이미지 로딩 실패")
                }
            }catch (e: Exception){
                e.printStackTrace()
            } // 예외 발생시 throw
        }

        //카메라 앱 연동
        val requestCameraFileLauncher= registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val calRatio = calculateInSampleSize(
                Uri.fromFile(File(filePath)),
                resources.getDimensionPixelSize(R.dimen.imgSize),
                resources.getDimensionPixelSize(R.dimen.imgSize)
            )
            val option = BitmapFactory.Options()
            option.inSampleSize = calRatio
            val bitmap = BitmapFactory.decodeFile(filePath, option)
            bitmap?.let {
                binding.proofImage.setImageBitmap(bitmap)
            }
        }

        //카메라 버튼
        binding.cameraButton.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    Log.d("tag", "권한 설정 완료")
                    try {

                        Log.d("로그처리", "카메라 선택")
                        val timeStamp: String =
                            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                        val file = File.createTempFile(
                            "JPEG_${timeStamp}_",
                            ".jpg",
                            storageDir
                        )
                        filePath = file.absolutePath
                        val photoURI: Uri = FileProvider.getUriForFile(
                            this,
                            "com.example.motimates.fileprovider",
                            file
                        )
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        requestCameraFileLauncher.launch(intent)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                else{
                    Log.d("tag", "권한 설정 요청")
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(
                            android.Manifest.permission.CAMERA,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        1
                    )
                }
            }
        }

        //갤러리 버튼
        binding.galleryButton.setOnClickListener {
            Log.d("로그처리", "갤러리 선택")
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            intent.type = "image/*"
            requestGalleryLauncher.launch(intent)
        }

        //툴바, 네비게이션 뷰 예시(액티비티로 구현했기 때문에 각각 drawer와 toolbar를 include 해줘야 함)
        val drawer = findViewById<DrawerLayout>(R.id.achievement_drawer)
        val menuBtn = findViewById<ImageView>(R.id.menuButton)
        menuBtn.setOnClickListener {
            drawer.openDrawer(GravityCompat.START)
        }
    }

    //이미지 크기 계산 함수
    private fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int) :Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream?.close()
            inputStream = null
        } catch (e: Exception) {
            e.printStackTrace()
        }// 예외 발생시 throw
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1 //이미지 크기를 줄이기 위한 변수
        if (height > reqHeight || width > reqWidth) {
            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}