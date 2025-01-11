package com.susu.feature.received.envelopeedit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.susu.core.model.Envelope
import com.susu.core.model.Ledger
import com.susu.core.model.Relationship
import com.susu.core.ui.PHONE_NUM_REGEX
import com.susu.core.ui.base.BaseViewModel
import com.susu.core.ui.extension.decodeFromUri
import com.susu.domain.usecase.envelope.EditReceivedEnvelopeUseCase
import com.susu.domain.usecase.envelope.GetRelationShipConfigListUseCase
import com.susu.feature.received.navigation.ReceivedRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ReceivedEnvelopeEditViewModel @Inject constructor(
    private val getRelationShipConfigListUseCase: GetRelationShipConfigListUseCase,
    private val editReceivedEnvelopeUseCase: EditReceivedEnvelopeUseCase,
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<ReceivedEnvelopeEditState, ReceivedEnvelopeEditSideEffect>(
    ReceivedEnvelopeEditState(),
) {
    private val argument = savedStateHandle.get<String>(ReceivedRoute.ENVELOPE_ARGUMENT_NAME)!!
    private val ledger = run {
        Json.decodeFromUri<Ledger>(savedStateHandle.get<String>(ReceivedRoute.LEDGER_ARGUMENT_NAME)!!)
    }

    private var isFirstVisited: Boolean = true

    fun initData() = viewModelScope.launch {
        if (isFirstVisited.not()) return@launch
        isFirstVisited = false

        val envelope = Json.decodeFromUri<Envelope>(argument)

        getRelationShipConfigListUseCase()
            .onSuccess {
                intent {
                    copy(
                        envelope = envelope,
                        relationshipConfig = it.map {
                            if (it.id == envelope.relationship.id) {
                                it.copy(customRelation = envelope.relationship.customRelation)
                            } else {
                                it
                            }
                        }.toPersistentList(),
                        showCustomRelationButton = it.last().id == envelope.relationship.id,
                        isRelationSaved = it.last().id == envelope.relationship.id,
                    )
                }
            }
    }

    fun editReceivedEnvelope() = viewModelScope.launch {
        editReceivedEnvelopeUseCase(
            param = with(currentState) {
                EditReceivedEnvelopeUseCase.Param(
                    envelopeId = envelope.id,
                    friendId = envelope.friend.id,
                    friendName = envelope.friend.name.trim(),
                    categoryId = ledger.category.id.toLong(),
                    customCategory = ledger.category.customCategory?.trim(),
                    phoneNumber = envelope.friend.phoneNumber.ifEmpty { null },
                    relationshipId = envelope.relationship.id,
                    customRelation = envelope.relationship.customRelation?.trim(),
                    ledgerId = ledger.id,
                    amount = envelope.amount,
                    gift = envelope.gift?.trim(),
                    memo = envelope.memo?.trim(),
                    handedOverAt = envelope.handedOverAt,
                    hasVisited = envelope.hasVisited,
                )
            },
        ).onSuccess {
            popBackStack()
        }.onFailure {
            postSideEffect(ReceivedEnvelopeEditSideEffect.HandleException(it, ::editReceivedEnvelope))
        }
    }

    fun popBackStack() = postSideEffect(ReceivedEnvelopeEditSideEffect.PopBackStack)

    fun updateMoney(money: String) {
        if (money.length > 10) {
            postSideEffect(ReceivedEnvelopeEditSideEffect.ShowMoneyNotValidSnackbar)
            return
        }

        intent {
            copy(
                envelope = envelope.copy(amount = money.toLongOrNull() ?: 0L),
            )
        }
    }

    fun updateName(name: String) {
        if (name.length > 10) {
            postSideEffect(ReceivedEnvelopeEditSideEffect.ShowNameNotValidSnackbar)
            return
        }

        intent {
            copy(
                envelope = envelope.copy(friend = envelope.friend.copy(name = name)),
            )
        }
    }

    fun updateRelation(relationship: Relationship) = intent {
        copy(
            envelope = envelope.copy(relationship = relationship),
        )
    }

    fun updateCustomRelation(customRelation: String?) {
        if (customRelation != null) {
            if (customRelation.length > 10) {
                postSideEffect(ReceivedEnvelopeEditSideEffect.ShowRelationshipNotValidSnackbar)
                return
            }
        }

        intent {
            copy(
                envelope = envelope.copy(relationship = envelope.relationship.copy(customRelation = customRelation)),
                relationshipConfig = relationshipConfig.map {
                    if (it.id == envelope.relationship.id) {
                        it.copy(customRelation = customRelation)
                    } else {
                        it
                    }
                }.toPersistentList(),
            )
        }
    }

    fun closeCustomRelation() = intent {
        copy(
            envelope = if (envelope.relationship.id == relationshipConfig.last().id) {
                envelope.copy(relationship = relationshipConfig.first())
            } else {
                envelope
            },
            relationshipConfig = relationshipConfig.map {
                it.copy(customRelation = null)
            }.toPersistentList(),
            isRelationSaved = false,
            showCustomRelationButton = false,
        )
    }

    fun toggleRelationSaved() = intent {
        copy(
            isRelationSaved = !isRelationSaved,
        )
    }

    fun showCustomRelation() = intent {
        postSideEffect(ReceivedEnvelopeEditSideEffect.FocusCustomRelation)
        copy(
            isRelationSaved = false,
            showCustomRelationButton = true,
            envelope = envelope.copy(
                relationship = relationshipConfig.last(),
            ),
        )
    }

    fun showDateBottomSheet() = intent {
        copy(
            showDateBottomSheet = true,
        )
    }

    fun updateHasVisited(visited: Boolean) = intent {
        copy(
            envelope = envelope.copy(
                hasVisited = if (visited == envelope.hasVisited) null else visited,
            ),
        )
    }

    fun updateGift(gift: String) {
        if (gift.length > 30) {
            postSideEffect(ReceivedEnvelopeEditSideEffect.ShowPresentNotValidSnackbar)
            return
        }

        intent {
            copy(
                envelope = envelope.copy(
                    gift = gift.ifEmpty { null },
                ),
            )
        }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        if (phoneNumber.length > 11) {
            postSideEffect(ReceivedEnvelopeEditSideEffect.ShowPhoneNotValidSnackbar)
            return
        }
        if (!PHONE_NUM_REGEX.matches(phoneNumber)) return

        intent {
            copy(
                envelope = envelope.copy(
                    friend = envelope.friend.copy(
                        phoneNumber = phoneNumber,
                    ),
                ),
            )
        }
    }

    fun updateMemo(memo: String) {
        if (memo != null && memo.length > 30) {
            postSideEffect(ReceivedEnvelopeEditSideEffect.ShowMemoNotValidSnackbar)
            return
        }

        intent {
            copy(
                envelope = envelope.copy(
                    memo = memo.ifEmpty { null },
                ),
            )
        }
    }

    fun hideDateBottomSheet(year: Int, month: Int, day: Int) = intent {
        copy(
            envelope = envelope.copy(
                handedOverAt = LocalDateTime.of(year, month, day, 0, 0).toKotlinLocalDateTime(),
            ),
            showDateBottomSheet = false,
        )
    }

    fun updateDate(year: Int, month: Int, day: Int) = intent {
        copy(
            envelope = envelope.copy(
                handedOverAt = LocalDateTime.of(year, month, day, 0, 0).toKotlinLocalDateTime(),
            ),
        )
    }
}
