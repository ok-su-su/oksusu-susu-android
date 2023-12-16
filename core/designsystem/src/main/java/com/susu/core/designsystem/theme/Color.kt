package com.susu.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val Gray10 = Color(0xFFFFFFFF)
val Gray15 = Color(0xFFF6F6F6)
val Gray20 = Color(0xFFF3F3F3)
val Gray25 = Color(0xFFECECEC)
val Gray30 = Color(0xFFE7E7E7)
val Gray40 = Color(0xFFD0D0D0)
val Gray50 = Color(0xFFBCBCBC)
val Gray60 = Color(0xFFA0A0A0)
val Gray70 = Color(0xFF828282)
val Gray80 = Color(0xFF575757)
val Gray90 = Color(0xFF333333)
val Gray100 = Color(0xFF242424)

val Blue10 = Color(0xFFF6FAFC)
val Blue20 = Color(0xFFEAF2F8)
val Blue30 = Color(0xFFB7DAFF)
val Blue40 = Color(0xFF88C1FF)
val Blue50 = Color(0xFF419DFF)
val Blue60 = Color(0xFF007BFF)
val Blue70 = Color(0xFF0061C8)
val Blue80 = Color(0xFF004897)
val Blue90 = Color(0xFF003063)
val Blue100 = Color(0xFF041E39)

val Orange5 = Color(0xFFFFFDF8)
val Orange10 = Color(0xFFFFF8EA)
val Orange20 = Color(0xFFFFEECE)
val Orange30 = Color(0xFFFFE1AA)
val Orange40 = Color(0xFFFFD381)
val Orange50 = Color(0xFFFFBD45)
val Orange60 = Color(0xFFFFA500)
val Orange70 = Color(0xFFCC8604)
val Orange80 = Color(0xFF8C5F0D)
val Orange90 = Color(0xFF5D3E06)
val Orange100 = Color(0xFF372505)

val Red10 = Color(0xFFFFD6C9)
val Red20 = Color(0xFFFFB39A)
val Red30 = Color(0xFFFF926F)
val Red40 = Color(0xFFFF7245)
val Red50 = Color(0xFFFF5924)
val Red60 = Color(0xFFFF3D00)
val Red70 = Color(0xFFCF3200)
val Red80 = Color(0xFFA62800)
val Red90 = Color(0xFF761C00)
val Red100 = Color(0xFF4B1200)

internal val LightColorScheme = SusuColorScheme(
    background10 = Gray10,
    background15 = Gray15,
    primary = Orange60,
    accent = Blue60,
    error = Red60,
)

@Immutable
data class SusuColorScheme(
    val background10: Color,
    val background15: Color,
    val primary: Color,
    val accent: Color,
    val error: Color,
)

val LocalColorScheme = staticCompositionLocalOf {
    SusuColorScheme(
        background10 = Color.Unspecified,
        background15 = Color.Unspecified,
        primary = Color.Unspecified,
        accent = Color.Unspecified,
        error = Color.Unspecified,
    )
}
