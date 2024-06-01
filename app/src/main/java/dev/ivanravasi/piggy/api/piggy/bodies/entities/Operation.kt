package dev.ivanravasi.piggy.api.piggy.bodies.entities

interface Operation {
    fun getOperationId(): Long
    fun type(): OperationType
    fun rawDate(): String
}

enum class OperationType {
    TRANSACTION, TRANSFER
}