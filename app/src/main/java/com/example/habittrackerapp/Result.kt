package com.example.habittrackerapp


sealed class OperationResult {
    data object Success : OperationResult()
    data class Error(val message: String) : OperationResult()
}
