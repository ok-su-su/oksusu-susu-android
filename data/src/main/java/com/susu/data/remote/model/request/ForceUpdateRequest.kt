package com.susu.data.remote.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ForceUpdateRequest(
    val needForceUpdate: Boolean = false
)
