package com.susu.data.data.di

import com.susu.data.data.repository.CategoryConfigRepositoryImpl
import com.susu.data.data.repository.LedgerRecentSearchRepositoryImpl
import com.susu.data.data.repository.LedgerRepositoryImpl
import com.susu.data.data.repository.LoginRepositoryImpl
import com.susu.data.data.repository.SignUpRepositoryImpl
import com.susu.data.data.repository.TermRepositoryImpl
import com.susu.data.data.repository.TokenRepositoryImpl
import com.susu.domain.repository.CategoryConfigRepository
import com.susu.domain.repository.LedgerRecentSearchRepository
import com.susu.domain.repository.LedgerRepository
import com.susu.domain.repository.LoginRepository
import com.susu.domain.repository.SignUpRepository
import com.susu.domain.repository.TermRepository
import com.susu.domain.repository.TokenRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindTokenRepository(
        tokenRepositoryImpl: TokenRepositoryImpl,
    ): TokenRepository

    @Binds
    abstract fun bindSignUpRepository(
        signUpRepositoryImpl: SignUpRepositoryImpl,
    ): SignUpRepository

    @Binds
    abstract fun bindLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl,
    ): LoginRepository

    @Binds
    abstract fun bindLedgerRecentSearchRepository(
        ledgerRecentSearchRepositoryImpl: LedgerRecentSearchRepositoryImpl,
    ): LedgerRecentSearchRepository

    @Binds
    abstract fun bindTermRepository(
        termRepositoryImpl: TermRepositoryImpl,
    ): TermRepository

    @Binds
    abstract fun bindLedgerRepository(
        ledgerRepositoryImpl: LedgerRepositoryImpl,
    ): LedgerRepository

    @Binds
    abstract fun bindCategoryConfigRepository(
        categoryConfigRepositoryImpl: CategoryConfigRepositoryImpl,
    ): CategoryConfigRepository
}