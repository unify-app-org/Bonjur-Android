package com.bonjur.app.fcm

import com.bonjur.app.fcm.data.DeviceDataSource
import com.bonjur.app.fcm.data.DeviceDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DeviceModule {

    @Binds
    @Singleton
    abstract fun bindDeviceDataSource(
        impl: DeviceDataSourceImpl,
    ): DeviceDataSource
}
