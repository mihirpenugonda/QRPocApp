package com.mhirrr.qrapppoc

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mhirrr.qrapppoc.databinding.ActivityEnterBinding

class EnterActivity : AppCompatActivity() {

    lateinit var binding: ActivityEnterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.generateButton.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val upiId = binding.upiInput.text.toString()

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("name", name)
            intent.putExtra("upi", upiId)

            startActivity(intent)
        }
    }
}