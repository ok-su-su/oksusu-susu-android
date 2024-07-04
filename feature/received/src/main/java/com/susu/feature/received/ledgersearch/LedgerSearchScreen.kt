package com.susu.feature.received.ledgersearch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.analytics.FirebaseAnalytics
import com.susu.core.designsystem.component.appbar.SusuDefaultAppBar
import com.susu.core.designsystem.component.appbar.icon.BackIcon
import com.susu.core.designsystem.component.container.SusuRecentSearchContainer
import com.susu.core.designsystem.component.searchbar.SusuSearchBar
import com.susu.core.designsystem.theme.Gray60
import com.susu.core.designsystem.theme.Gray80
import com.susu.core.designsystem.theme.SusuTheme
import com.susu.core.model.Ledger
import com.susu.core.ui.extension.collectWithLifecycle
import com.susu.feature.received.R
import kotlinx.collections.immutable.PersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.datetime.toJavaLocalDateTime

@OptIn(FlowPreview::class)
@Composable
fun LedgerSearchRoute(
    viewModel: LedgerSearchViewModel = hiltViewModel(),
    popBackStack: () -> Unit,
    navigateLedgerDetail: (Ledger) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    viewModel.sideEffect.collectWithLifecycle { sideEffect ->
        when (sideEffect) {
            LedgerSearchSideEffect.PopBackStack -> popBackStack()
            is LedgerSearchSideEffect.NavigateLedgerDetail -> navigateLedgerDetail(sideEffect.ledger)
            LedgerSearchSideEffect.FocusClear -> focusManager.clearFocus()
            LedgerSearchSideEffect.LogBackClickEvent -> scope.launch {
                FirebaseAnalytics.getInstance(context).logEvent(
                    FirebaseAnalytics.Event.SELECT_CONTENT,
                    bundleOf(
                        FirebaseAnalytics.Param.CONTENT_TYPE to "ledger_search_screen_back",
                    ),
                )
            }
            LedgerSearchSideEffect.LogSearchResultClickEvent -> scope.launch {
                FirebaseAnalytics.getInstance(context).logEvent(
                    FirebaseAnalytics.Event.SELECT_CONTENT,
                    bundleOf(
                        FirebaseAnalytics.Param.CONTENT_TYPE to "ledger_search_screen_search_result",
                    ),
                )
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getLedgerRecentSearchList()
    }

    LaunchedEffect(key1 = uiState.searchKeyword) {
        snapshotFlow { uiState.searchKeyword }
            .debounce(100L)
            .collect(viewModel::getLedgerList)
    }

    LedgerSearchScreen(
        uiState = uiState,
        focusRequester = focusRequester,
        onClickBackIcon = {
            viewModel.popBackStack()
            viewModel.logBackClickEvent()
        },
        onValueChangeSearchBar = viewModel::updateSearch,
        onClickSearchClearIcon = { viewModel.updateSearch("") },
        onClickRecentSearchContainer = { search ->
            viewModel.clearFocus()
            viewModel.hideSearchResultEmpty()
            viewModel.updateSearch(search)
            viewModel.upsertLedgerRecentSearch(search)
        },
        onClickRecentSearchContainerCloseIcon = viewModel::deleteLedgerRecentSearch,
        onClickSearchResultContainer = { ledger ->
            viewModel.logSearchResultClickEvent()
            viewModel.upsertLedgerRecentSearch(ledger.title)
            viewModel.navigateLedgerDetail(ledger)
        },
    )
}

@Composable
fun LedgerSearchScreen(
    uiState: LedgerSearchState = LedgerSearchState(),
    focusRequester: FocusRequester = remember { FocusRequester() },
    onClickBackIcon: () -> Unit = {},
    onClickSearchClearIcon: () -> Unit = {},
    onValueChangeSearchBar: (String) -> Unit = {},
    onClickRecentSearchContainer: (String) -> Unit = {},
    onClickRecentSearchContainerCloseIcon: (String) -> Unit = {},
    onClickSearchResultContainer: (Ledger) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .background(SusuTheme.colorScheme.background10)
            .fillMaxSize(),
    ) {
        Column {
            SusuDefaultAppBar(
                leftIcon = {
                    BackIcon(onClickBackIcon)
                },
            )

            Column(
                modifier = Modifier.padding(
                    top = SusuTheme.spacing.spacing_xxs,
                    start = SusuTheme.spacing.spacing_m,
                    end = SusuTheme.spacing.spacing_m,
                ),
            ) {
                SusuSearchBar(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = uiState.searchKeyword,
                    onValueChange = onValueChangeSearchBar,
                    onClickClearIcon = onClickSearchClearIcon,
                    placeholder = stringResource(R.string.ledger_search_screen_search_placeholder),
                )

                if (uiState.searchKeyword.isEmpty()) {
                    RecentSearchColumn(
                        recentSearchList = uiState.recentSearchKeywordList,
                        onClickItem = onClickRecentSearchContainer,
                        onClickCloseIcon = onClickRecentSearchContainerCloseIcon,
                    )
                } else {
                    SearchResultColumn(
                        showSearchResultEmpty = uiState.showSearchResultEmpty,
                        ledgerList = uiState.ledgerList,
                        onClickItem = onClickSearchResultContainer,
                    )
                }
            }
        }
    }
}

@Composable
private fun ResultEmptyColumn(
    title: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 136.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_xxxxs),
    ) {
        Text(
            text = title,
            style = SusuTheme.typography.title_xs,
            color = Gray80,
        )
        Text(
            text = stringResource(R.string.ledger_search_screen_empty_search_description),
            style = SusuTheme.typography.text_xxs,
            textAlign = TextAlign.Center,
            color = Gray80,
        )
    }
}

@Composable
private fun RecentSearchColumn(
    recentSearchList: PersistentList<String>,
    onClickItem: (String) -> Unit,
    onClickCloseIcon: (String) -> Unit,
) {
    if (recentSearchList.isEmpty()) {
        ResultEmptyColumn(
            title = stringResource(R.string.ledger_search_screen_empty_recent_search_title),
        )
    } else {
        Column(
            modifier = Modifier.padding(top = SusuTheme.spacing.spacing_xxl),
            verticalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_m),
        ) {
            Text(
                text = stringResource(com.susu.core.ui.R.string.word_recent_search),
                style = SusuTheme.typography.title_xxs,
                color = Gray60,
            )
            recentSearchList.forEach { name ->
                SusuRecentSearchContainer(
                    name = name,
                    onClick = { onClickItem(name) },
                    onClickCloseIcon = { onClickCloseIcon(name) },
                )
            }
        }
    }
}

@Composable
private fun SearchResultColumn(
    showSearchResultEmpty: Boolean,
    ledgerList: PersistentList<Ledger>,
    onClickItem: (Ledger) -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = SusuTheme.spacing.spacing_xxl),
        verticalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_m),
    ) {
        Text(
            text = stringResource(com.susu.core.ui.R.string.word_search_result),
            style = SusuTheme.typography.title_xxs,
            color = Gray60,
        )

        if (showSearchResultEmpty) {
            ResultEmptyColumn(
                title = stringResource(R.string.ledger_search_screen_empty_search_result_title),
            )
        }

        ledgerList.forEach { ledger ->
            SusuRecentSearchContainer(
                typeIconId = R.drawable.ic_ledger,
                name = ledger.title,
                category = ledger.category.name,
                startDate = ledger.startAt.toJavaLocalDateTime(),
                endDate = ledger.endAt.toJavaLocalDateTime(),
                onClick = { onClickItem(ledger) },
            )
        }
    }
}

@Preview
@Composable
fun LedgerSearchScreenPreview() {
    SusuTheme {
        LedgerSearchScreen()
    }
}
