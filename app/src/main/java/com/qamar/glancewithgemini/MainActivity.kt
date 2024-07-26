package com.qamar.glancewithgemini

import android.app.AlertDialog
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_MUTABLE
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import com.qamar.glancewithgemini.glance.MyAppWidget
import com.qamar.glancewithgemini.ui.theme.GlanceWithGeminiTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GlanceWithGeminiTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    showWidgetInstructionsDialog(this)
                }
            }
        }
    }
    private fun showWidgetInstructionsDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add Our Widget")
        builder.setMessage("To add our widget to your home screen, long-press on your home screen, select 'Widgets', and find our app's widget.")
        builder.setPositiveButton("Got it") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }
}

