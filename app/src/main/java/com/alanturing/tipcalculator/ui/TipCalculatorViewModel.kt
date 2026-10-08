package com.alanturing.tipcalculator.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

class TipCalculatorViewModel(): ViewModel() {

    private val _state = MutableStateFlow(TipCaculatorScreenState())
    val state: StateFlow<TipCaculatorScreenState>
        get() = _state.asStateFlow()

    fun changeAmount(newAmount: String) {
        val oldState = state.value
        _state.value = oldState.copy(
            amount = newAmount,
            isCalculateEnabled = isCalculateEnabled(newAmount, oldState.guests)
        )
    }

    fun changeGuests(newGuests: String) {
        val oldState = state.value
        _state.value = oldState.copy(
            guests = newGuests,
            isCalculateEnabled = isCalculateEnabled(oldState.amount, newGuests)
        )
    }

    fun changeTip(isEnabled: Boolean) {
        val oldState = state.value
        _state.value = oldState.copy(
            tip = isEnabled
        )
    }

    fun changeTipAmount(newTipAmount: Float) {
        val oldState = state.value
        _state.value = oldState.copy(
            tipAmount = newTipAmount
        )
    }

    fun calculateSplit() {
        val currentState = state.value
        val guestNumber = currentState.guests.toIntOrNull()
        val totalAmount = currentState.amount.toDoubleOrNull()

        if (guestNumber != null && totalAmount != null && guestNumber > 0 && totalAmount > 0.0) {
            val tipMultiplier = if (currentState.tip) {
                1.0 + (currentState.tipAmount * 0.05)
            } else {
                1.0
            }
            val totalWithTip = totalAmount * tipMultiplier
            val splitResult = totalWithTip / guestNumber
            val resultString = String.format(Locale.US, "%.2f", splitResult)

            _state.value = currentState.copy(
                result = resultString
            )
        }
    }

    private fun isCalculateEnabled(amount: String, guests: String): Boolean {
        val guestNumber = guests.toIntOrNull()
        val totalAmount = amount.toDoubleOrNull()
        return guestNumber != null && totalAmount != null && guestNumber > 0 && totalAmount > 0.0
    }
}

data class TipCaculatorScreenState(
    val guests: String = "0",
    val amount: String = "0.00",
    val tip: Boolean = false,
    val tipAmount: Float = 0.0F,
    val result: String = "",
    val isCalculateEnabled: Boolean = false
)