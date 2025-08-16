package com.kfouri.inventoryproject.screens.device.deviceScreen.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.kfouri.inventoryproject.screens.component.LoadingScreen
import com.kfouri.inventoryproject.screens.component.SearchWidget
import com.kfouri.inventoryproject.screens.device.deviceScreen.ui.model.DeviceModel
import com.kfouri.inventoryproject.screens.device.deviceScreen.ui.viewmodel.DeviceViewModel
import com.kfouri.inventoryproject.screens.feedback.Button
import com.kfouri.inventoryproject.screens.feedback.FeedbackModel
import kotlinx.coroutines.launch

@Composable
fun DeviceScreen(
    navController: NavHostController,
    viewModel: DeviceViewModel = hiltViewModel(),
    onNavigateTo: (String) -> Unit,
    onBack: () -> Unit,
    onNavigateToFeedback: (FeedbackModel) -> Unit
) {
    val searchedDevices = viewModel.searchedDevices.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsState()

    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val currentBackStackEntry = navController.currentBackStackEntry ?: return@LaunchedEffect
        currentBackStackEntry.savedStateHandle.getStateFlow("device_created", false)
            .collect { deviceWasCreated ->
                if (deviceWasCreated) {
                    viewModel.searchDevices("")
                    currentBackStackEntry.savedStateHandle["device_created"] = false
                }
            }
    }

    LaunchedEffect(searchedDevices.loadState) {
        if (searchedDevices.loadState.refresh is LoadState.NotLoading && searchedDevices.itemCount > 0) {
            if (listState.firstVisibleItemIndex > 0 || listState.firstVisibleItemScrollOffset > 0) {
                scope.launch {
                    listState.animateScrollToItem(index = 0)
                }
            }
        }
    }

    when {
        searchedDevices.loadState.refresh is LoadState.Loading && searchedDevices.itemCount == 0 -> {
            LoadingScreen()
        }

        searchedDevices.loadState.refresh is LoadState.Error -> {
            val errorState = searchedDevices.loadState.refresh as LoadState.Error
            val throwable = errorState.error

            val errorCode = throwable::class.java.simpleName
            val errorMessage = throwable.message ?: "Error desconocido"

            val feedbackModel = FeedbackModel(
                hasBackButton = false,
                image = "error",
                title = "Ups...",
                description = "Algo salió mal, por favor vuelve a intentarlo.",
                errorCode = "$errorCode - $errorMessage",
                primaryButton = Button(
                    title = "Reintentar",
                    route = "devices"
                ),
                secondaryButton = Button(
                    title = "Menu Principal",
                    route = "home"
                )
            )
            onNavigateToFeedback(feedbackModel)
        }

        else -> {
            DeviceScreenContent(
                listState = listState,
                uiState = uiState,
                viewModel = viewModel,
                searchedDevices = searchedDevices,
                onBack = {
                    onBack.invoke()
                },
                onFabClicked = {
                    onNavigateTo("addDevice")
                }
            )

            if (searchedDevices.loadState.append is LoadState.Loading) {
                LoadingScreen()
            }
        }
    }

    when (uiState.screenStatus) {
        ScreenStatus.LOADING -> {

        }

        ScreenStatus.ERROR -> {
            val errorCode = uiState.error?.code
            val errorMessage = uiState.error?.message

            val feedbackModel = FeedbackModel(
                hasBackButton = false,
                image = "error",
                title = "Ups...",
                description = "Algo salió mal, por favor vuelve a intentarlo.",
                errorCode = "$errorCode - $errorMessage",
                primaryButton = Button(
                    title = "Reintentar",
                    route = "devices"
                ),
                secondaryButton = Button(
                    title = "Menu Principal",
                    route = "home"
                )
            )
            onNavigateToFeedback(feedbackModel)
        }

        ScreenStatus.SUCCESS -> {

        }

        ScreenStatus.UNAUTHORIZED -> {
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
fun DeviceScreenContent(
    uiState: DeviceUiState,
    viewModel: DeviceViewModel,
    searchedDevices: LazyPagingItems<DeviceModel>,
    listState: LazyListState,
    onBack: () -> Unit,
    onFabClicked: () -> Unit
) {
    val searchQuery by viewModel.searchQuery.collectAsState()

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .safeContentPadding(),
        topBar = {
            SearchWidget(
                text = searchQuery,
                hasBack = uiState.screenData?.hasBack == true,
                onTextChange = { newQuery ->
                    viewModel.updateSearchQuery(query = newQuery)
                },
                onSearchClicked = { query ->
                    viewModel.searchDevices(query = query)
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                onCloseClicked = {
                    viewModel.searchDevices(query = "")
                    keyboardController?.hide()
                    focusManager.clearFocus()
                },
                onBack = {
                    onBack.invoke()
                }
            )
        },
        floatingActionButton = {
            if (uiState.screenData?.access == "A") {
                FloatingActionButton(
                    onClick = {
                        onFabClicked()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Añadir"
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { paddingValues ->

        Column(Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues)
                    .padding(top = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${uiState.screenData?.title}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black,
                )
            }

            if (searchedDevices.itemCount == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No hay dispositivos disponibles")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    state = listState
                ) {
                    items(
                        count = searchedDevices.itemCount,
                        key = searchedDevices.itemKey { it.id }) { index ->
                        searchedDevices[index]?.let { item ->
                            DeviceItem(item)
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun DeviceItem(
    item: DeviceModel,
    onClick: () -> Unit = {}
) {
    val placeholderPainter = rememberVectorPainter(image = Icons.Filled.Image)
    val errorPainter = rememberVectorPainter(image = Icons.Filled.BrokenImage)

    Card(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = "http://${item.imageUrl}",
                    contentDescription = null,
                    placeholder = placeholderPainter,
                    error = errorPainter,
                    modifier = Modifier
                        .size(80.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = item.code ?: "", style = MaterialTheme.typography.titleMedium)
                    Text(text = item.location ?: "", style = MaterialTheme.typography.bodyMedium)
                }
                Text(
                    modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                    text = item.sets ?: "", style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}