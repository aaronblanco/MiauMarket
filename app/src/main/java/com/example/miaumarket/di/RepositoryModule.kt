package com.example.miaumarket.di

import com.example.miaumarket.data.repository.AuthRepositoryImpl
import com.example.miaumarket.data.repository.ProductRepositoryImpl
import com.example.miaumarket.domain.repository.AuthRepository
import com.example.miaumarket.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository
}
