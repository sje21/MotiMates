package com.example.motimates

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.motimates.EditProfileActivity.Companion.EDIT_PROFILE_REQUEST_CODE
import com.example.motimates.MyGarden
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var profileImageView: ImageView
    private lateinit var greetingTextView: TextView
    private lateinit var greetingTextView2: TextView
    private lateinit var waterDropCountTextView: TextView
    private lateinit var flowerCountTextView: TextView
    private lateinit var goalCountTextView: TextView
    private lateinit var addGoalButton: Button
    private lateinit var viewGoalsButton: Button
    private lateinit var viewFlowerGardenButton: Button
    private lateinit var editProfileButton: Button

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var toolbar: Toolbar

        // 목표 정보 설정 (가상의 데이터로 예시)
        binding.waterDropCountTextView.text = "5"
        binding.flowerCountTextView.text = "10"
        binding.goalCountTextView.text = "3"

        // 목표 추가하기 버튼 클릭 이벤트
        binding.addGoalButton.setOnClickListener {
            // 목표 추가하기 버튼 클릭 시 처리
            val intent = Intent(this, AddPurpose::class.java)
            startActivity(intent)
        }

        // 달성 중인 목표 보기 버튼 클릭 이벤트
        binding.viewGoalsButton.setOnClickListener {
            // 달성 중인 목표 보기 버튼 클릭 시 처리
            val intent = Intent(this, AchievementMain::class.java)
            startActivity(intent)
        }

        // 나의 꽃밭 보기 버튼 클릭 이벤트
        binding.viewFlowerGardenButton.setOnClickListener {
            // 나의 꽃밭 보기 버튼 클릭 시 처리
            val intent = Intent(this, MyGarden::class.java)
            startActivity(intent)
        }

        // 회원 정보 수정 버튼 클릭 이벤트
        binding.editProfileButton.setOnClickListener {
            // 회원 정보 수정 버튼 클릭 시 처리
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
            
    override fun onResume() {
        super.onResume()
        // EditProfileActivity에서 돌아왔을 때, 데이터 확인
        val newName = intent.getStringExtra("newName")
        if (!newName.isNullOrBlank()) {
            greetingTextView2.text = newName+"님!"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profileImageView = findViewById(R.id.profileImageView)
        greetingTextView = findViewById(R.id.greetingTextView)
        greetingTextView2 = findViewById(R.id.greetingTextView2)
        waterDropCountTextView = findViewById(R.id.waterDropCountTextView)
        flowerCountTextView = findViewById(R.id.flowerCountTextView)
        goalCountTextView = findViewById(R.id.goalCountTextView)
        addGoalButton = findViewById(R.id.addGoalButton)
        viewGoalsButton = findViewById(R.id.viewGoalsButton)
        viewFlowerGardenButton = findViewById(R.id.viewFlowerGardenButton)
        editProfileButton = findViewById(R.id.editProfileButton)

        addGoalButton.setOnClickListener {
            startActivity(Intent(this, AddPurpose::class.java))
        }

        viewGoalsButton.setOnClickListener {
            startActivity(Intent(this, GoalListActivity::class.java))
        }

        viewFlowerGardenButton.setOnClickListener {
            startActivity(Intent(this, MyGarden::class.java))
        }

        editProfileButton.setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        drawerLayout = findViewById(R.id.drawer_layout)
        toolbar = findViewById(R.id.toolbar)
        navView = findViewById(R.id.nav_view)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        // 네비게이션 아이템 클릭 리스너 설정
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.main_page -> {
                    // 홈 화면으로 이동
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.add_ach -> {
                    // 목표 추가 화면으로 이동
                    startActivity(Intent(this, AddPurpose::class.java))
                    true
                }
                R.id.look_ach -> {
                    // 목표 보기 화면으로 이동
                    startActivity(Intent(this, GoalListActivity::class.java))
                    true
                }
                R.id.look_garden-> {
                    // 꽃밭 보기 화면으로 이동
                    startActivity(Intent(this, MyGarden::class.java))
                    true
                }
                R.id.member_info-> {
                    // 회원 정보 수정 화면으로 이동
                    startActivity(Intent(this, EditProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }
}