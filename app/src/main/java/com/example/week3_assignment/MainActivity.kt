package com.example.week3_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.week3_assignment.ui.dashboard.DashboardRoute
import com.example.week3_assignment.ui.theme.SmartHomeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartHomeTheme {
                DashboardRoute()
            }
        }
    }
}
