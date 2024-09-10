package com.susu.data.data.di

import com.susu.data.data.repository.BlockRepositoryImpl
import com.susu.data.data.repository.CategoryConfigRepositoryImpl
import com.susu.data.data.repository.EnvelopeRecentSearchRepositoryImpl
import com.susu.data.data.repository.EnvelopesRepositoryImpl
import com.susu.data.data.repository.ExcelRepositoryImpl
import com.susu.data.data.repository.FriendRepositoryImpl
import com.susu.data.data.repository.LedgerRecentSearchRepositoryImpl
import com.susu.data.data.repository.LedgerRepositoryImpl
import com.susu.data.data.repository.LoginRepositoryImpl
import com.susu.data.data.repository.OnboardRepositoryImpl
import com.susu.data.data.repository.ReportRepositoryImpl
import com.susu.data.data.repository.SignUpRepositoryImpl
import com.susu.data.data.repository.StatisticsRepositoryImpl
import com.susu.data.data.repository.TermRepositoryImpl
import com.susu.data.data.repository.TokenRepositoryImpl
import com.susu.data.data.repository.UserRepositoryImpl
import com.susu.data.data.repository.VersionRepositoryImpl
import com.susu.data.data.repository.VoteRecentSearchRepositoryImpl
import com.susu.data.data.repository.VoteRepositoryImpl
import com.susu.domain.repository.BlockRepository
import com.susu.domain.repository.CategoryConfigRepository
import com.susu.domain.repository.EnvelopeRecentSearchRepository
import com.susu.domain.repository.EnvelopesRepository
import com.susu.domain.repository.ExcelRepository
import com.susu.domain.repository.FriendRepository
import com.susu.domain.repository.LedgerRecentSearchRepository
import com.susu.domain.repository.LedgerRepository
import com.susu.domain.repository.LoginRepository
import com.susu.domain.repository.OnboardRepository
import com.susu.domain.repository.ReportRepository
import com.susu.domain.repository.SignUpRepository
import com.susu.domain.repository.StatisticsRepository
import com.susu.domain.repository.TermRepository
import com.susu.domain.repository.TokenRepository
import com.susu.domain.repository.UserRepository
import com.susu.domain.repository.VersionRepository
import com.susu.domain.repository.VoteRecentSearchRepository
import com.susu.domain.repository.VoteRepository
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

    @Binds
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl,
    ): UserRepository

    @Binds
    abstract fun bindExcelRepository(
        excelRepositoryImpl: ExcelRepositoryImpl,
    ): ExcelRepository

    @Binds
    abstract fun bindEnvelopesRepository(
        envelopesRepositoryImpl: EnvelopesRepositoryImpl,
    ): EnvelopesRepository

    @Binds
    abstract fun bindFriendRepository(
        friendRepositoryImpl: FriendRepositoryImpl,
    ): FriendRepository

    @Binds
    abstract fun bindStatisticsRepository(
        statisticsRepositoryImpl: StatisticsRepositoryImpl,
    ): StatisticsRepository

    @Binds
    abstract fun bindVoteRepository(
        voteRepositoryImpl: VoteRepositoryImpl,
    ): VoteRepository

    @Binds
    abstract fun bindEnvelopeRecentSearchRepository(
        envelopeRecentSearchRepositoryImpl: EnvelopeRecentSearchRepositoryImpl,
    ): EnvelopeRecentSearchRepository

    @Binds
    abstract fun bindVoteRecentSearchRepository(
        voteRecentSearchRepositoryImpl: VoteRecentSearchRepositoryImpl,
    ): VoteRecentSearchRepository

    @Binds
    abstract fun bindBlockRepository(
        blockRepositoryImpl: BlockRepositoryImpl,
    ): BlockRepository

    @Binds
    abstract fun bindReportRepository(
        reportRepositoryImpl: ReportRepositoryImpl,
    ): ReportRepository

    @Binds
    abstract fun bindOnboardRepository(
        onboardRepositoryImpl: OnboardRepositoryImpl,
    ): OnboardRepository

    @Binds
    abstract fun bindVersionRepository(
        versionRepositoryImpl: VersionRepositoryImpl,
    ): VersionRepository
}
