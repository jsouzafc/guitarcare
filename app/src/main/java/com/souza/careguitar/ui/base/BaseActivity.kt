package com.souza.careguitar.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.souza.careguitar.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBin
        ing.inflate(layoutInflater)
        setContentView(binding.root)
    }
}

