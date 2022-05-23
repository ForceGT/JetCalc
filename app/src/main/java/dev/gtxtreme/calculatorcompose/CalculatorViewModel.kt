package dev.gtxtreme.calculatorcompose

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {

    var state by mutableStateOf(CalculatorState())
        private set


    fun onAction(action: CalculatorAction) {
        when (action) {
            is CalculatorAction.Number -> enterNumber(action.number)
            is CalculatorAction.Decimal -> enterDecimal()
            is CalculatorAction.Clear -> state = CalculatorState()
            is CalculatorAction.CalculatorOperation -> enterOperation(action)
            is CalculatorAction.Calculate -> performCalculation()
            is CalculatorAction.Delete -> performDeletion()
        }
    }

    private fun performDeletion() {
        when {
            state.number2.isNotBlank() -> state = state.copy(number2 = state.number2.dropLast(1))
            state.operation != null -> state = state.copy(operation = null)
            state.number.isNotBlank() -> state = state.copy(number = state.number.dropLast(1))
        }
    }

    private fun performCalculation() {
        val number = state.number.toDoubleOrNull()
        val number2 = state.number2.toDoubleOrNull()
        if (number != null && number2 != null) {

            val operation = when (state.operation) {
                is CalculatorAction.CalculatorOperation.Add -> number + number2
                is CalculatorAction.CalculatorOperation.Subtract -> number - number2
                is CalculatorAction.CalculatorOperation.Multiply -> number * number2
                is CalculatorAction.CalculatorOperation.Divide -> number / number2
                null -> return
            }
            state =
                state.copy(
                    number = operation.toString().take(15),
                    number2 = "",
                    operation = null
                )
        }

    }

    private fun enterOperation(operation: CalculatorAction.CalculatorOperation) {
        if (state.number.isNotBlank()) {
            state = state.copy(operation = operation)
        }
    }

    private fun enterDecimal() {
        if (state.operation == null && !state.number.contains(".") && state.number.isNotBlank()) {
            state = state.copy(number = state.number + ".")
            return
        }
        if (!state.number2.contains(".") && state.number2.isNotBlank()) {
            state = state.copy(number2 = state.number2 + ".")
        }
    }

    private fun enterNumber(number: Int) {
        if (state.operation == null) {
            if (state.number.length >= MAX_NUM_LENGTH) {
                return
            }
            state = state.copy(number = state.number + number)
            return
        }
        if (state.number2.length >= MAX_NUM_LENGTH) {
            return
        }
        state = state.copy(number2 = state.number2 + number)
    }

    companion object {
        private const val MAX_NUM_LENGTH = 8
    }
}