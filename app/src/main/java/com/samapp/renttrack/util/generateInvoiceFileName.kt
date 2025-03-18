package com.samapp.renttrack.util

import com.samapp.renttrack.data.local.model.PaymentHistory

fun generateInvoiceFileName(transactions: List<PaymentHistory>): String {
    val transactionHash = transactions.hashCode().toString() // Hash based on transaction data
    return "invoice_$transactionHash"
}
