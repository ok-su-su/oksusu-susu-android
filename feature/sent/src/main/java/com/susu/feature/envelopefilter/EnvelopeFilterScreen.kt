package com.susu.feature.envelopefilter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.susu.core.designsystem.component.appbar.SusuDefaultAppBar
import com.susu.core.designsystem.component.appbar.icon.BackIcon
import com.susu.core.designsystem.component.button.FilledButtonColor
import com.susu.core.designsystem.component.button.LinedButtonColor
import com.susu.core.designsystem.component.button.RefreshButton
import com.susu.core.designsystem.component.button.SelectedFilterButton
import com.susu.core.designsystem.component.button.SmallButtonStyle
import com.susu.core.designsystem.component.button.SusuFilledButton
import com.susu.core.designsystem.component.button.SusuLinedButton
import com.susu.core.designsystem.component.button.XSmallButtonStyle
import com.susu.core.designsystem.theme.SusuTheme
import com.susu.core.ui.extension.collectWithLifecycle
import com.susu.feature.envelopefilter.component.MoneySlider
import com.susu.feature.sent.R

@Composable
fun EnvelopeFilterRoute(
    viewModel: EnvelopeFilterViewModel = hiltViewModel(),
    popBackStack: () -> Unit,
    popBackStackWithFilter: (String) -> Unit,
    handleException: (Throwable, () -> Unit) -> Unit,
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    viewModel.sideEffect.collectWithLifecycle { sideEffect ->
        when (sideEffect) {
            is EnvelopeFilterSideEffect.HandleException -> handleException(sideEffect.throwable, sideEffect.retry)
            EnvelopeFilterSideEffect.PopBackStack -> popBackStack()
            is EnvelopeFilterSideEffect.PopBackStackWithFilter -> popBackStackWithFilter(sideEffect.filter)
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.initData()
    }

    EnvelopeFilterScreen(
        uiState = uiState,
        onClickBackIcon = viewModel::popBackStack,
        onClickApplyFilterButton = viewModel::popBackStackWithFilter,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EnvelopeFilterScreen(
    @Suppress("detekt:UnusedParameter")
    uiState: EnvelopeFilterState = EnvelopeFilterState(),
    onClickBackIcon: () -> Unit = {},
    onClickApplyFilterButton: () -> Unit = {},
    onClickRefreshButton: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .background(SusuTheme.colorScheme.background10)
            .fillMaxSize(),
    ) {
        SusuDefaultAppBar(
            leftIcon = {
                BackIcon(onClickBackIcon)
            },
            title = stringResource(id = com.susu.core.ui.R.string.word_filter),
        )

        Column(
            modifier = Modifier.padding(
                top = SusuTheme.spacing.spacing_xl,
                start = SusuTheme.spacing.spacing_m,
                end = SusuTheme.spacing.spacing_m,
                bottom = SusuTheme.spacing.spacing_xxs,
            ),
        ) {
            Text(text = stringResource(R.string.envelope_filter_screen_friend), style = SusuTheme.typography.title_xs)
            Spacer(modifier = Modifier.size(SusuTheme.spacing.spacing_m))
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_xxs),
                verticalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_xxs),
            ) {
                listOf("이진욱", "김철수", "홍길동", "박예은", "박미영", "서한누리", "서한누리").forEach { category ->
                    SusuLinedButton(
                        color = LinedButtonColor.Black,
                        style = XSmallButtonStyle.height28,
                        isActive = true,
                        text = category,
                        onClick = { },
                    )
                }
            }

            Spacer(modifier = Modifier.size(SusuTheme.spacing.spacing_xxxxxxl))
            Text(
                text = stringResource(R.string.envelope_filter_screen_money),
                style = SusuTheme.typography.title_xs,
            )
            Spacer(modifier = Modifier.size(SusuTheme.spacing.spacing_m))

            Text(text = "20,000원~100,000원", style = SusuTheme.typography.title_m)

            Spacer(modifier = Modifier.size(SusuTheme.spacing.spacing_xxs))

            MoneySlider(value = 20_000f..100_000f, onValueChange = {}, valueRange = 0f..100_000f)

            Spacer(modifier = Modifier.weight(1f))

            Column(
                verticalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_m),
            ) {
                FlowRow(
                    verticalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_xxs),
                    horizontalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_xxs),
                ) {
                    SelectedFilterButton(
                        name = "이진욱",
                    )

                    SelectedFilterButton(
                        name = "20,000~10,000",
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_m),
                ) {
                    RefreshButton(onClick = onClickRefreshButton)

                    SusuFilledButton(
                        modifier = Modifier.fillMaxWidth(),
                        color = FilledButtonColor.Black,
                        style = SmallButtonStyle.height48,
                        isActive = true,
                        text = stringResource(com.susu.core.ui.R.string.word_apply_filter),
                        onClick = onClickApplyFilterButton,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun EnvelopeFilterScreenPreview() {
    SusuTheme {
        EnvelopeFilterScreen()
    }
}
