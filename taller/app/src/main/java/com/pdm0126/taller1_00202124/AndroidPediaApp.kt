package com.pdm0126.taller1_00202124.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


private val CelesteColor = Color(0xFF64B5F6)

@Composable
fun AndroidPediaApp(modifier: Modifier = Modifier) {
    var currentStep    by rememberSaveable { mutableIntStateOf(0) }
    var score          by rememberSaveable { mutableIntStateOf(0) }
    var selectedAnswer by rememberSaveable { mutableStateOf("") }
    var answered       by rememberSaveable { mutableStateOf(false) }

    when (currentStep) {
        0 -> WelcomeScreen(onStart = { currentStep = 1 })
        1, 2, 3 -> {
            val currentQuestion = quizQuestions[currentStep - 1]
            QuestionScreen(
                question         = currentQuestion,
                currentStep      = currentStep,
                totalQuestions   = quizQuestions.size,
                score            = score,
                selectedAnswer   = selectedAnswer,
                answered         = answered,
                onAnswerSelected = { answer ->
                    selectedAnswer = answer
                    answered = true
                    if (answer == currentQuestion.correctAnswer) score++
                },
                onNext = {
                    currentStep++
                    selectedAnswer = ""
                    answered = false
                }
            )
        }
        4 -> ResultScreen(
            score = score,
            total = quizQuestions.size,
            onRestart = {
                currentStep    = 1
                score          = 0
                selectedAnswer = ""
                answered       = false
            }
        )
    }
}

@Composable
fun WelcomeScreen(onStart: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "AndroidPedia", fontSize = 36.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "¿Cuanto sabes de Android?", fontSize = 18.sp, color = Color.Gray, textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Jaime Antonio Perez Shupan", fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Text(text = "Carnet: 00202124", fontSize = 16.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick  = onStart,
            colors   = ButtonDefaults.buttonColors(containerColor = CelesteColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Comenzar Quiz", fontSize = 16.sp, color = Color.White)
        }
    }
}

@Composable
fun QuestionScreen(
    question: Question,
    currentStep: Int,
    totalQuestions: Int,
    score: Int,
    selectedAnswer: String,
    answered: Boolean,
    onAnswerSelected: (String) -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Pregunta $currentStep de $totalQuestions", fontWeight = FontWeight.Medium, fontSize = 14.sp)
            Text(text = "Puntaje: $score / $totalQuestions",        fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Text(
                text       = question.question,
                modifier   = Modifier.padding(20.dp),
                fontSize   = 17.sp,
                textAlign  = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(20.dp))



        question.options.forEach { option ->
            val buttonColor = when {
                !answered                        -> ButtonDefaults.buttonColors(
                    containerColor         = CelesteColor,
                    disabledContainerColor = CelesteColor
                )
                option == question.correctAnswer -> ButtonDefaults.buttonColors(
                    containerColor         = Color(0xFF4CAF50),
                    disabledContainerColor = Color(0xFF4CAF50)
                )
                else                             -> ButtonDefaults.buttonColors(
                    containerColor         = Color(0xFFF44336),
                    disabledContainerColor = Color(0xFFF44336)
                )
            }
            Button(
                onClick  = { onAnswerSelected(option) },
                enabled  = !answered,
                colors   = buttonColor,
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
            ) {
                Text(text = option, textAlign = TextAlign.Center, color = Color.White)
            }
        }

        if (answered) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors   = CardDefaults.cardColors(containerColor = Color(0xFFFFF9C4))
            ) {
                Text(
                    text     = "Dato curioso: ${question.funFact}",
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp,
                    color    = Color(0xFF5D4037)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick  = onNext,
                colors   = ButtonDefaults.buttonColors(containerColor = CelesteColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text  = if (currentStep < totalQuestions) "Siguiente" else "Ver Resultado",
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ResultScreen(score: Int, total: Int, onRestart: () -> Unit) {
    val message = when (score) {
        3    -> "¡Excelente! Eres un experto en Android."
        2    -> "¡Bien hecho! Conoces bastante sobre Android."
        1    -> "Puedes mejorar. Sigue estudiando la historia de Android."
        else -> "¡Ups! Necesitas repasar la historia de Android desde el inicio."
    }
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Resultado Final", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Obtuviste $score de $total", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = message, fontSize = 16.sp, textAlign = TextAlign.Center, color = Color.Gray)
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick  = onRestart,
            colors   = ButtonDefaults.buttonColors(containerColor = CelesteColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Reiniciar Quiz", fontSize = 16.sp, color = Color.White)
        }
    }
}