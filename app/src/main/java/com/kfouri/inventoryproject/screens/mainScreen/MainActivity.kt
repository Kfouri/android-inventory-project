package com.kfouri.inventoryproject.screens.mainScreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.content.edit
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.kfouri.inventoryproject.di.AppModule.AUTH_TOKEN_KEY
import com.kfouri.inventoryproject.screens.device.addDeviceScreen.ui.AddDeviceScreen
import com.kfouri.inventoryproject.screens.device.deviceScreen.ui.DeviceScreen
import com.kfouri.inventoryproject.screens.feedback.FeedbackScreen
import com.kfouri.inventoryproject.screens.homeScreen.ui.HomeScreen
import com.kfouri.inventoryproject.screens.feedback.FeedbackModel
import com.kfouri.inventoryproject.screens.login.ui.SignInScreen
import com.kfouri.inventoryproject.screens.login.ui.SignUpScreen
import com.kfouri.inventoryproject.ui.theme.InventoryProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InventoryProjectTheme(dynamicColor = false) {

                val navController = rememberNavController()
                val prefs = getSharedPreferences("prefs", MODE_PRIVATE)

                val performLogout = {
                    prefs.edit {
                        putString(AUTH_TOKEN_KEY, "")
                    }

                    navController.navigate(NavDestination.SignInScreen.route) {
                        popUpTo(NavDestination.FeedbackScreen.route + "/{data}") {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }


                NavHost(
                    navController = navController,
                    startDestination = NavDestination.SignInScreen.route
                ) {
                    composable(route = NavDestination.SignInScreen.route) {
                        SignInScreen(
                            onNavigateToHome = {
                                navController.navigate(NavDestination.HomeScreen.route) {
                                    popUpTo(NavDestination.SignInScreen.route) {
                                        inclusive = true
                                    }
                                }
                            },
                            onNavigateToSignUp = {
                                navController.navigate(NavDestination.SignUpScreen.route)
                            }
                        )
                    }

                    composable(route = NavDestination.SignUpScreen.route) {
                        SignUpScreen(
                            onBack = {
                                navController.navigateUp()
                            },
                            onNavigateToFeedback = { feedbackModel ->
                                val dataJson = Gson().toJson(feedbackModel)
                                val encodedData =
                                    URLEncoder.encode(dataJson, StandardCharsets.UTF_8.toString())
                                navController.navigate(NavDestination.FeedbackScreen.route + "/$encodedData")
                            }
                        )
                    }

                    composable(route = NavDestination.HomeScreen.route) {
                        HomeScreen(
                            onNavigateTo = { route ->
                                navController.navigate(route)
                            },
                            onNavigateToFeedback = { feedbackModel ->
                                val dataJson = Gson().toJson(feedbackModel)
                                val encodedData =
                                    URLEncoder.encode(dataJson, StandardCharsets.UTF_8.toString())
                                navController.navigate(NavDestination.FeedbackScreen.route + "/$encodedData")
                            }
                        )
                    }

                    composable(
                        route = NavDestination.FeedbackScreen.route + "/{data}",
                        arguments = listOf(navArgument("data") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val dataJson = backStackEntry.arguments?.getString("data")
                        val decodedData =
                            URLDecoder.decode(dataJson, StandardCharsets.UTF_8.toString())
                        val feedbackModel = Gson().fromJson(decodedData, FeedbackModel::class.java)

                        FeedbackScreen(
                            feedbackModel,
                            onBack = {
                                navController.navigateUp()
                            },
                            onNavigateTo = { route ->
                                if (route == "logout") {
                                    performLogout()
                                } else {
                                    navController.navigate(route) {
                                        if (route == "signIn") {
                                            popUpTo(navController.graph.startDestinationId) {
                                                inclusive = true
                                            }
                                        } else {
                                            popUpTo(NavDestination.FeedbackScreen.route + "/{data}") {
                                                inclusive = true
                                            }
                                        }
                                        launchSingleTop = true
                                    }
                                }

                            }
                        )
                    }

                    composable(route = NavDestination.DeviceScreen.route) {
                        DeviceScreen(
                            navController = navController,
                            onNavigateTo = { route ->
                                navController.navigate(route)
                            },
                            onNavigateToFeedback = { feedbackModel ->
                                val dataJson = Gson().toJson(feedbackModel)
                                val encodedData =
                                    URLEncoder.encode(dataJson, StandardCharsets.UTF_8.toString())
                                navController.navigate(NavDestination.FeedbackScreen.route + "/$encodedData")
                            },
                            onBack = {
                                navController.navigateUp()
                            }
                        )
                    }

                    composable(route = NavDestination.AddDeviceScreen.route) {
                        AddDeviceScreen(
                            onNavigateTo = { route ->
                                navController.navigate(route)
                            },
                            onNavigateToFeedback = { feedbackModel ->
                                val dataJson = Gson().toJson(feedbackModel)
                                val encodedData =
                                    URLEncoder.encode(dataJson, StandardCharsets.UTF_8.toString())
                                navController.navigate(NavDestination.FeedbackScreen.route + "/$encodedData")
                            },
                            onBack = {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("device_created", true)
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}