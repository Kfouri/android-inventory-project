package com.kfouri.inventoryproject.screens.homeScreen.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.kfouri.inventoryproject.screens.component.LoadingScreen
import com.kfouri.inventoryproject.screens.component.TopBar
import com.kfouri.inventoryproject.screens.feedback.Button
import com.kfouri.inventoryproject.screens.feedback.FeedbackModel
import com.kfouri.inventoryproject.screens.homeScreen.data.model.HomeResponse
import com.kfouri.inventoryproject.screens.homeScreen.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateTo: (String) -> Unit,
    onNavigateToFeedback: (FeedbackModel) -> Unit
) {

    val viewState by viewModel.state.collectAsState()

    when (val state = viewState) {
        HomeStateUI.Loading -> {
            LoadingScreen()
        }

        is HomeStateUI.Success -> {
            val data = state.data
            ShowHomeScreen(
                data = data,
                onNavigateTo = onNavigateTo
            )
        }

        is HomeStateUI.Error -> {
            val errorCode = state.status
            val errorMessage = state.message

            val feedbackModel = FeedbackModel(
                hasBackButton = false,
                image = "error",
                title = "Ups...",
                description = "Algo salió mal, por favor vuelve a intentarlo.",
                errorCode = "$errorCode - $errorMessage",
                primaryButton = Button(
                    title = "Reintentar",
                    route = "home"
                ),
                secondaryButton = Button(
                    title = "Logout",
                    route = "logout"
                )
            )
            onNavigateToFeedback(feedbackModel)
        }

        HomeStateUI.Unauthorized -> {
            val feedbackModel = FeedbackModel(
                hasBackButton = false,
                image = "unauthorized",
                title = "Sin Autorización",
                description = "No esta autorizado para ver esta pantalla.",
                errorCode = null,
                primaryButton = Button(
                    title = "Logout",
                    route = "logout"
                )
            )
            onNavigateToFeedback(feedbackModel)
        }
    }
}

@Composable
fun ShowHomeScreen(
    data: HomeResponse,
    onNavigateTo: (String) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = data.title ?: "Menu Principal",
                hasBackButton = false,
                onBack = { }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(
                    top = padding.calculateTopPadding() + 8.dp,
                    bottom = padding.calculateBottomPadding()
                ),
            contentAlignment = Alignment.TopCenter,
        ) {
            val scrollState = rememberScrollState()
            Column(modifier = Modifier.verticalScroll(scrollState)) {
                data.list?.forEach { item ->
                    HomeItem(
                        title = item.title,
                        description = item.description,
                        imageUrl = item.icon
                        //"https://cdn-icons-png.flaticon.com/512/2278/2278984.png"
                    ) {
                        onNavigateTo(item.route)
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeItem(
    title: String,
    description: String,
    imageUrl: String,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface //Color(0xFFD4EDDA) // verde claro
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.padding(16.dp)) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(modifier = Modifier.padding(top = 16.dp, end = 16.dp, bottom = 16.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}