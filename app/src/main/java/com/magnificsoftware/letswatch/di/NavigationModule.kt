package com.magnificsoftware.letswatch.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import com.magnificsoftware.letswatch.manager.navigation.AppNavigator
import com.magnificsoftware.letswatch.manager.navigation.AppNavigatorImpl

@InstallIn(ActivityComponent::class)
@Module
abstract class NavigationModule {
    @Binds
    abstract fun bindNavigator(impl: AppNavigatorImpl): AppNavigator
}