package com.kfouri.inventoryproject.screens.feedback

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.kfouri.inventoryproject.R

@Composable
fun FeedbackScreen(
    feedbackModel: FeedbackModel,
    onBack: () -> Unit,
    onNavigateTo: (String) -> Unit,
) {

    BackHandler(enabled = feedbackModel.hasBackButton, onBack = { onBack() })

    val animation = getPairValue(feedbackModel.image)
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animation.first))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = animation.second
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    top = padding.calculateTopPadding() + 16.dp,
                    bottom = padding.calculateBottomPadding(),
                    start = 16.dp,
                    end = 16.dp
                ),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(0.44f)
                    .padding(top = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(200.dp)
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = feedbackModel.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = feedbackModel.description,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )

                feedbackModel.errorCode?.let {
                    Text(
                        text = "Error Code: ${feedbackModel.errorCode}",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = {
                        onNavigateTo(feedbackModel.primaryButton.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    Text(
                        text = feedbackModel.primaryButton.title,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                feedbackModel.secondaryButton?.let { secondaryButton ->
                    Button(
                        onClick = {
                            onNavigateTo(secondaryButton.route)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                    ) {
                        Text(
                            text = secondaryButton.title,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

private const val success = "success"
private const val error = "error"
private const val unauthorized = "unauthorized"

private val default = Pair(R.raw.lottie_success, 1)

private val animations = hashMapOf(
    success to Pair(R.raw.lottie_success, 1),
    error to Pair(R.raw.lottie_error, 1),
    unauthorized to Pair(R.raw.lottie_unauthotized, 1)
)

private fun getPairValue(key: String): Pair<Int, Int> =
    animations.getOrElse(key) { default }