package com.kfouri.inventoryproject.screens.device.addDeviceScreen.ui

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.rememberAsyncImagePainter
import com.kfouri.inventoryproject.screens.component.TopBar
import com.kfouri.inventoryproject.screens.device.addDeviceScreen.ui.model.AddDeviceEvent
import com.kfouri.inventoryproject.screens.device.addDeviceScreen.ui.viewmodel.AddDeviceViewModel
import com.kfouri.inventoryproject.screens.feedback.FeedbackModel
import com.kfouri.inventoryproject.core.data.model.Result

@Composable
fun AddDeviceScreen(
    viewModel: AddDeviceViewModel = hiltViewModel(),
    onNavigateTo: (String) -> Unit,
    onBack: (Boolean) -> Unit,
    onNavigateToFeedback: (FeedbackModel) -> Unit
) {

    val state = viewModel.state
    val context = LocalContext.current

    LaunchedEffect(viewModel, context) {

        viewModel.resultChannel.collect { result ->
            when (result) {
                is Result.Success -> {
                    Toast.makeText(context, "Dispositivo creado correctamente", Toast.LENGTH_LONG)
                        .show()
                    onBack.invoke(true)
                }

                is Result.Unauthorized -> {
                    val feedbackModel = FeedbackModel(
                        hasBackButton = false,
                        image = "unauthorized",
                        title = "Sin Autorización",
                        description = "No esta autorizado para ver esta pantalla.",
                        errorCode = null,
                        primaryButton = com.kfouri.inventoryproject.screens.feedback.Button(
                            title = "Logout",
                            route = "logout"
                        )
                    )
                    onNavigateToFeedback(feedbackModel)
                }

                is Result.Error -> {
                    viewModel.onEvent(AddDeviceEvent.Error("${result.code} - ${result.message}"))
                }
            }
        }

    }

    // Launcher para seleccionar una imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                viewModel.onEvent(AddDeviceEvent.ImageSelected(it))
            }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBar(
                title = null,
                hasBackButton = true,
                onBack = {
                    onBack(false)
                }
            )
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(scrollState)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Dispositivo Nuevo",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black,
                    )
                }

                OutlinedTextField(
                    value = state.code,
                    onValueChange = {
                        viewModel.onEvent(AddDeviceEvent.CodeChanged(it))
                    },
                    label = {
                        Text(
                            text = buildAnnotatedString {
                                append("Código")
                                withStyle(style = SpanStyle(color = Color.Red)) {
                                    append(" *")
                                }
                            }
                        )
                    },
                    placeholder = { Text("Ingresá el código del dispositivo") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    singleLine = true,
                    isError = state.codeError != null,
                )

                OutlinedTextField(
                    value = state.sets,
                    onValueChange = {
                        viewModel.onEvent(AddDeviceEvent.SetsChanged(it))
                    },
                    label = {
                        Text(text = buildAnnotatedString {
                            append("Conjuntos")
                            withStyle(style = SpanStyle(color = Color.Red)) {
                                append(" *")
                            }
                        })
                    },
                    placeholder = { Text("Ingresá el o los Conjuntos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    singleLine = true,
                    isError = state.setsError != null,
                )

                OutlinedTextField(
                    value = state.location,
                    onValueChange = {
                        viewModel.onEvent(AddDeviceEvent.LocationChanged(it))
                    },
                    label = {
                        Text(text = buildAnnotatedString {
                            append("Ubicación")
                            withStyle(style = SpanStyle(color = Color.Red)) {
                                append(" *")
                            }
                        })
                    },
                    placeholder = { Text("Ingresá la ubicación") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    singleLine = true,
                    isError = state.locationError != null,
                )

                OutlinedTextField(
                    value = state.implements,
                    onValueChange = {
                        viewModel.onEvent(AddDeviceEvent.ImplementsChanged(it))
                    },
                    label = {
                        Text(text = buildAnnotatedString {
                            append("Implementos")
                            withStyle(style = SpanStyle(color = Color.Red)) {
                                append(" *")
                            }
                        })
                    },
                    placeholder = { Text("Ingresá el o los Implementos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    singleLine = true,
                    isError = state.implementsError != null,
                )

                if (state.imageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(state.imageUri),
                        contentDescription = "Imagen seleccionada",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp) // Altura de ejemplo
                            .padding(top = 16.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    onClick = {
                        imagePickerLauncher.launch("image/*")
                    }
                ) {
                    Text("Cargar Foto")
                }

                Row {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            onBack(false)
                        }
                    ) {
                        Text("Cancelar")
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        onClick = {
                            viewModel.onEvent(AddDeviceEvent.Create)
                        }
                    ) {
                        Text("Guardar")
                    }
                }

                AnimatedVisibility(visible = state.error != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.error.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}