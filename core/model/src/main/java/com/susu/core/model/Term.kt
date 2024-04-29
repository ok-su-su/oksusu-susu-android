package com.susu.core.model

data class Term(
    val id: Int,
    val title: String,
    val isEssential: Boolean,
    val canRead: Boolean = true,
)
