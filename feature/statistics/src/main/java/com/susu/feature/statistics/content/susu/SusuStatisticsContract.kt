package com.susu.feature.statistics.content.susu

import com.susu.core.model.Category
import com.susu.core.model.Relationship
import com.susu.core.model.SusuStatistics
import com.susu.core.ui.base.SideEffect
import com.susu.core.ui.base.UiState
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import java.util.Calendar
import java.util.Date

sealed interface SusuStatisticsEffect : SideEffect {
    data object ShowAdditionalInfoDialog : SusuStatisticsEffect
    data object ShowNoDataSnackbar : SusuStatisticsEffect
    data class HandleException(val throwable: Throwable, val retry: () -> Unit) : SusuStatisticsEffect
    data class LogAgeOption(val age: StatisticsAge) : SusuStatisticsEffect
    data class LogRelationshipOption(val relationship: String) : SusuStatisticsEffect
    data class LogCategoryOption(val category: String) : SusuStatisticsEffect
}

data class SusuStatisticsState(
    val isLoading: Boolean = false,
    val isBlind: Boolean = true,
    val age: StatisticsAge = StatisticsAge.TWENTY,
    val relationship: Relationship = Relationship(),
    val category: Category = Category(),
    val categoryConfig: PersistentList<Category> = persistentListOf(),
    val relationshipConfig: PersistentList<Relationship> = persistentListOf(),
    val isAgeSheetOpen: Boolean = false,
    val isRelationshipSheetOpen: Boolean = false,
    val isCategorySheetOpen: Boolean = false,
    val susuStatistics: SusuStatistics = SusuStatistics(),
) : UiState

enum class StatisticsAge(val num: Int) {
    TEN(10), TWENTY(20), THIRTY(30), FOURTY(40), FIFTY(50), SIXTY(60), SEVENTY(70)
}

fun Int.toStatisticsAge(): StatisticsAge? {
    val age = currentYear - this + 1
    return when {
        age in 0..19 -> StatisticsAge.TEN
        age in 20..29 -> StatisticsAge.TWENTY
        age in 30..39 -> StatisticsAge.THIRTY
        age in 40..49 -> StatisticsAge.FOURTY
        age in 50..59 -> StatisticsAge.FIFTY
        age in 60..69 -> StatisticsAge.SIXTY
        age >= 70 -> StatisticsAge.SEVENTY
        else -> null
    }
}

val currentYear = Calendar.getInstance().get(Calendar.YEAR)
