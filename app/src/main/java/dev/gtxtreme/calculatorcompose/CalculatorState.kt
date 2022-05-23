package dev.gtxtreme.calculatorcompose

data class CalculatorState(
    val number: String = "",
    val number2: String = "",
    val operation: CalculatorAction.CalculatorOperation? = null
)
