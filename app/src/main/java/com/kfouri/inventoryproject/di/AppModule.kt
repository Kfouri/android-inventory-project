package com.kfouri.inventoryproject.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.kfouri.inventoryproject.screens.device.addDeviceScreen.data.AddDeviceRepositoryImpl
import com.kfouri.inventoryproject.screens.device.addDeviceScreen.data.service.AddDeviceService
import com.kfouri.inventoryproject.screens.device.addDeviceScreen.domain.AddDeviceRepository
import com.kfouri.inventoryproject.screens.device.addDeviceScreen.domain.usecase.AddDeviceUseCase
import com.kfouri.inventoryproject.screens.device.deviceScreen.data.DeviceRepositoryImpl
import com.kfouri.inventoryproject.screens.device.deviceScreen.data.service.DeviceService
import com.kfouri.inventoryproject.screens.device.deviceScreen.domain.DeviceRepository
import com.kfouri.inventoryproject.screens.device.deviceScreen.domain.usecase.GetDeviceByIdUseCase
import com.kfouri.inventoryproject.screens.device.deviceScreen.domain.usecase.GetDeviceScreenUseCase
import com.kfouri.inventoryproject.screens.device.deviceScreen.domain.usecase.GetDevicesPaginatedUseCase
import com.kfouri.inventoryproject.screens.device.deviceScreen.domain.usecase.GetDevicesUseCase
import com.kfouri.inventoryproject.screens.homeScreen.data.HomeRepositoryImpl
import com.kfouri.inventoryproject.screens.homeScreen.data.service.HomeService
import com.kfouri.inventoryproject.screens.homeScreen.domain.HomeRepository
import com.kfouri.inventoryproject.screens.homeScreen.domain.usecase.GetHomeUseCase
import com.kfouri.inventoryproject.screens.login.data.LoginRepositoryImpl
import com.kfouri.inventoryproject.screens.login.data.service.LoginService
import com.kfouri.inventoryproject.screens.login.domain.usecase.AuthenticateUseCase
import com.kfouri.inventoryproject.screens.login.domain.LoginRepository
import com.kfouri.inventoryproject.screens.login.domain.usecase.SignInUseCase
import com.kfouri.inventoryproject.screens.login.domain.usecase.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "http://192.168.100.85:8080/"
    const val AUTH_TOKEN_KEY = "jwt"

    @Provides
    @Singleton
    fun provideAuthInterceptor(prefs: SharedPreferences): Interceptor {
        return Interceptor { chain ->
            val token = prefs.getString(AUTH_TOKEN_KEY, null)
            val originalRequest = chain.request()

            val requestBuilder = originalRequest.newBuilder()
            if (token != null) {
                // Si hay un token, agrégalo al encabezado
                requestBuilder.header("Authorization", "Bearer $token")
            }

            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        authInterceptor: Interceptor
    ): Retrofit {

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //Service
    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    @Provides
    @Singleton
    fun provideHomeService(retrofit: Retrofit): HomeService =
        retrofit.create(HomeService::class.java)

    //Repository
    @Provides
    @Singleton
    fun providePostRepository(api: LoginService, prefs: SharedPreferences): LoginRepository =
        LoginRepositoryImpl(api, prefs)

    @Provides
    @Singleton
    fun provideHomeRepository(api: HomeService): HomeRepository = HomeRepositoryImpl(api)

    //UseCase
    @Provides
    fun provideSignUp(repository: LoginRepository) = SignUpUseCase(repository)

    @Provides
    fun provideSignIn(repository: LoginRepository) = SignInUseCase(repository)

    @Provides
    fun provideAuthenticate(repository: LoginRepository) = AuthenticateUseCase(repository)

    @Provides
    fun provideGetHomeData(repository: HomeRepository) = GetHomeUseCase(repository)

    //Others
    @Provides
    @Singleton
    fun provideSharedPreferences(app: Application): SharedPreferences =
        app.getSharedPreferences("prefs", MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideDeviceRepository(api: DeviceService): DeviceRepository = DeviceRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideDeviceService(retrofit: Retrofit): DeviceService =
        retrofit.create(DeviceService::class.java)

    @Provides
    fun provideGetDevicesUseCase(repository: DeviceRepository) = GetDevicesUseCase(repository)

    @Provides
    fun provideGetDeviceByIdUseCase(repository: DeviceRepository) = GetDeviceByIdUseCase(repository)

    @Provides
    fun provideGetDevicesPaginatedUseCase(repository: DeviceRepository) =
        GetDevicesPaginatedUseCase(repository)

    @Provides
    fun provideGetDeviceScreenUseCase(repository: DeviceRepository) =
        GetDeviceScreenUseCase(repository)

    @Provides
    fun provideAddDeviceService(retrofit: Retrofit): AddDeviceService =
        retrofit.create(AddDeviceService::class.java)


    @Provides
    fun provideAddDeviceRepository(
        api: AddDeviceService,
        @ApplicationContext context: Context
    ): AddDeviceRepository =
        AddDeviceRepositoryImpl(
            api, context
        )

    @Provides
    fun provideAddDeviceUseCase(repository: AddDeviceRepository) =
        AddDeviceUseCase(repository)

}