package com.susu.feature.envelopeadd.content.category

import androidx.lifecycle.viewModelScope
import com.susu.core.model.Category
import com.susu.core.ui.USER_INPUT_REGEX_INCLUDE_NUMBER
import com.susu.core.ui.base.BaseViewModel
import com.susu.domain.usecase.categoryconfig.GetCategoryConfigUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val getCategoryConfigUseCase: GetCategoryConfigUseCase,
) : BaseViewModel<CategoryState, CategoryEffect>(CategoryState()) {

    private val parentSelectedCategory
        get() = with(currentState) {
            if (selectedCategory == customCategory &&
                (customCategory.customCategory.isNullOrEmpty() || isSavedCustomCategory.not())
            ) {
                null
            } else {
                selectedCategory
            }
        }

    fun getCategoryConfig() = viewModelScope.launch {
        if (currentState.categoryConfig.isNotEmpty()) return@launch

        getCategoryConfigUseCase().onSuccess {
            intent {
                copy(
                    categoryConfig = it.filter { !it.isCustom }.toPersistentList(),
                )
            }
        }
    }

    fun selectCategory(category: Category) = intent { copy(selectedCategory = category) }

    fun selectCustomCategory() = intent {
        postSideEffect(CategoryEffect.FocusCustomCategory)
        copy(selectedCategory = customCategory)
    }

    fun updateCustomCategoryText(text: String) {
        if (!USER_INPUT_REGEX_INCLUDE_NUMBER.matches(text)) { // 한글, 영문 0~10 글자
            if (text.length > 10) { // 길이 넘친 경우
                postSideEffect(CategoryEffect.ShowNotValidSnackbar)
            }
            return // 특수문자는 입력 안 됨
        }

        intent {
            copy(
                selectedCategory = customCategory.copy(name = text, customCategory = text),
                customCategory = customCategory.copy(name = text, customCategory = text),
            )
        }
    }

    fun showCustomCategoryTextField() = intent {
        copy(
            showTextFieldButton = true,
            selectedCategory = customCategory,
        )
    }

    fun hideCustomCategoryTextField() = intent {
        copy(
            isSavedCustomCategory = false,
            showTextFieldButton = false,
            selectedCategory = if (isCustomCategorySelected) null else selectedCategory,
            customCategory = customCategory.copy(name = ""),
        )
    }

    fun toggleTextFieldSaved() = intent {
        if (isSavedCustomCategory) {
            copy(
                isSavedCustomCategory = false,
            )
        } else {
            copy(
                isSavedCustomCategory = true,
                customCategory = customCategory.copy(
                    name = customCategory.name.trim(),
                    customCategory = customCategory.customCategory?.trim(),
                ),
            )
        }
    }

    fun updateParentCategory(category: Category? = parentSelectedCategory) {
        postSideEffect(CategoryEffect.UpdateParentCategory(category))
    }
}
