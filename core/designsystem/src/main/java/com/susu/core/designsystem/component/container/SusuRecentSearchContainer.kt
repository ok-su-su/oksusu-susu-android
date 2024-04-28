package com.susu.core.designsystem.component.container

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.susu.core.designsystem.R
import com.susu.core.designsystem.theme.Gray40
import com.susu.core.designsystem.theme.Orange60
import com.susu.core.designsystem.theme.SusuTheme
import com.susu.core.ui.extension.susuClickable
import com.susu.core.ui.util.to_yyyy_dot_MM_dot_dd
import java.time.LocalDateTime

@Composable
fun SusuRecentSearchContainer(
    modifier: Modifier = Modifier,
    @DrawableRes typeIconId: Int? = null,
    tint: Color = Orange60,
    typeIconContentDescription: String? = null,
    name: String = "",
    category: String? = null,
    startDate: LocalDateTime? = null,
    endDate: LocalDateTime? = null,
    onClickCloseIcon: () -> Unit = {},
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .susuClickable(onClick = onClick),
        horizontalArrangement = Arrangement.spacedBy(SusuTheme.spacing.spacing_m),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (typeIconId != null) {
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painterResource(id = typeIconId),
                contentDescription = typeIconContentDescription,
                tint = tint,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = name,
                style = SusuTheme.typography.title_s,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            if (category != null && startDate != null && endDate != null) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = category,
                        style = SusuTheme.typography.title_xxs,
                        color = Gray40,
                    )

                    val (startDateFormatted, endDateFormatted) = (startDate.to_yyyy_dot_MM_dot_dd() to endDate.to_yyyy_dot_MM_dot_dd())

                    if (startDateFormatted == endDateFormatted) {
                        Text(
                            text = startDateFormatted,
                            style = SusuTheme.typography.text_xxs,
                            color = Gray40,
                        )
                    } else {
                        Text(
                            text = "$startDateFormatted-$endDateFormatted",
                            style = SusuTheme.typography.text_xxs,
                            color = Gray40,
                        )
                    }
                }
            }
        }

        if (typeIconId == null) {
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .susuClickable(onClick = onClickCloseIcon),
                painter = painterResource(id = R.drawable.ic_recent_search_close),
                contentDescription = stringResource(com.susu.core.ui.R.string.content_description_close_icon),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SusuRecentSearchContainerPreview() {
    SusuTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            SusuRecentSearchContainer(
                name = "나의 결혼식나의 결혼식나의 결혼식나의 결혼식나의 결혼식나의 결혼식",
                typeIconId = R.drawable.ic_clear,
                typeIconContentDescription = "",
            )

            SusuRecentSearchContainer(
                name = "나의 결혼식나의 결혼식나의 결혼식나의 결혼식나의 결혼식나의 결혼식",
                typeIconId = R.drawable.ic_clear,
                typeIconContentDescription = "",
                category = "결혼식",
                startDate = LocalDateTime.now(),
                endDate = LocalDateTime.now(),
            )
        }
    }
}
