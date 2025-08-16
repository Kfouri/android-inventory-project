package com.kfouri.inventoryproject.screens.mainScreen

sealed class NavDestination(val title: String, val route: String) {
    object SignInScreen : NavDestination("Sign In", "signIn")
    object SignUpScreen : NavDestination("Sign Up", "signUp")
    object HomeScreen : NavDestination("Home", "home")
    object FeedbackScreen : NavDestination("Feedback", "feedback")

    object DeviceScreen : NavDestination("Devices", "devices")
    object AddDeviceScreen : NavDestination("Add Devices", "addDevice")

}