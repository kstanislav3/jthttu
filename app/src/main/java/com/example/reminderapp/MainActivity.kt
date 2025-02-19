package com.example.reminderapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.work.*
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        NotificationHelper.createNotificationChannel(this)

        setContent {
            ReminderScreen { message, time ->
                scheduleReminder(message, time)
            }
        }
    }

    private fun scheduleReminder(message: String, delayMinutes: Int) {
        val workRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMinutes.toLong(), TimeUnit.MINUTES)
            .setInputData(workDataOf("message" to message))
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        { Text("Напоминание! ;)", color = Color.White) },
        colors = TopAppBarDefaults.topAppBarColors().copy(
containerColor = Color(0xFF6650a4),
        )
    )
}

@Composable
fun MainScreen() {
    Scaffold(
        topBar = { TopBar() }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Text("Главный экран")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
    MainScreen()
}


@Composable
fun ReminderScreen(onSetReminder: (String, Int) -> Unit) {
    var message by remember { mutableStateOf("") }
    var sliderValue by remember { mutableStateOf(5f) }

    Scaffold(
        topBar = { TopBar() } // Добавляем верхнюю шторку
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {}
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Введите напоминание:", fontSize = 18.sp)
            TextField(
                value = message,
                onValueChange = { message = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Через сколько по времени вас осведомить?", fontSize = 18.sp)
            Slider(
                value = sliderValue,
                onValueChange = { sliderValue = it },
                valueRange = 1f..60f,
                steps = 58
            )
            Text(text = "${sliderValue.toInt()} минут", fontSize = 16.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onSetReminder(message, sliderValue.toInt()) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.run { buttonColors(Color(0xFF6650a4)) } // Фиолетовый цвет
            ) {
                Text("Установить данное напоминание")
            }
        }
    }
}