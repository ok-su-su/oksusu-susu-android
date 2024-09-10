package com.susu.data.remote.model.response

import com.susu.core.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class CategoryConfigResponse(
    val id: Int,
    val seq: Int,
    val name: String,
    val style: String,
    val isActive: Boolean,
    val isCustom: Boolean,
    val isMiscCategory: Boolean
)

internal fun CategoryConfigResponse.toModel() = Category(
    id = id,
    seq = seq,
    name = name,
    style = style,
    isCustom = isCustom,
    isActive = isActive
)
