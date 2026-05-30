package com.sunnah.app.ui.dhikr

import android.content.Context
import android.os.Vibrator
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sunnah.app.viewmodel.MainViewModel

@Composable
fun DhikrScreen(viewModel: MainViewModel) {
    val dhikrRecords by viewModel.dhikrRecords.collectAsState(initial = emptyList())
    val phrases = listOf("Subhan'Allah", "Alhamdulillah", "Allahu Akbar")
    var selectedPhrase by remember { mutableStateOf(phrases[0]) }
    
    val currentRecord = dhikrRecords.find { it.phrase == selectedPhrase }
    val count = currentRecord?.count ?: 0
    val target = currentRecord?.target ?: 33

    val context = LocalContext.current
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Digital Subhah",
            style = MaterialTheme.typography.titleLarge
        )
        
        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier
                .size(300.dp)
                .clickable {
                    vibrator.vibrate(50)
                    val newCount = if (count + 1 > target) 1 else count + 1
                    viewModel.updateDhikrCount(selectedPhrase, newCount, target)
                },
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawArc(
                    color = Color.LightGray.copy(alpha = 0.3f),
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 12.dp.toPx())
                )
                drawArc(
                    color = Color(0xFF386B1D),
                    startAngle = -90f,
                    sweepAngle = (count.toFloat() / target) * 360f,
                    useCenter = false,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = count.toString(),
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = "Goal: $target",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        PhraseSelector(phrases, selectedPhrase) { 
            selectedPhrase = it 
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { viewModel.updateDhikrCount(selectedPhrase, 0, target) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
        ) {
            Text("Reset Count")
        }
    }
}

@Composable
fun PhraseSelector(phrases: List<String>, selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(
            onClick = { expanded = true },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.width(200.dp)
        ) {
            Text(selected)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            phrases.forEach { phrase ->
                DropdownMenuItem(
                    text = { Text(phrase) },
                    onClick = {
                        onSelect(phrase)
                        expanded = false
                    }
                )
            }
        }
    }
}
