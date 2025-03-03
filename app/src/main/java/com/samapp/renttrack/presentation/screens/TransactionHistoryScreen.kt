package com.samapp.renttrack.presentation.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.samapp.renttrack.data.local.model.PaymentHistory
import com.samapp.renttrack.data.local.model.PaymentType
import com.samapp.renttrack.data.local.model.Result
import com.samapp.renttrack.presentation.viewmodels.InvoiceViewModel
import com.samapp.renttrack.presentation.viewmodels.PaymentHistoryViewModel
import com.samapp.renttrack.util.openPdf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter

data class Sample(
    val paymentDate: String,
    val paymentAmount: String,
    val paymentType: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionHistoryScreen(
    tenantId: Int,
    onBack: () -> Unit,
    viewModel: PaymentHistoryViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        delay(500)
        viewModel.getPaymentHistoryForTenant(tenantId)
    }

    val transactionsState by viewModel.paymentHistoryState.collectAsState()
    val context = LocalContext.current
    val invoiceViewModel:InvoiceViewModel = hiltViewModel()
    val transactions = (transactionsState as? Result.Success<List<PaymentHistory>>)?.data
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Transaction History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    //TODO-trying to implement opening transaction invoice
                    CoroutineScope(Dispatchers.Main).launch{
                        val file = invoiceViewModel.generateTransactionInvoice(transactions = transactions)

                        if (file != null) {
                            Log.d(
                                "Invoice",
                                "Invoice successfully generated: ${file.absolutePath}"
                            )
                            openPdf(
                                context,
                                file
                            )
                        } else {
                            Log.e("Invoice", "Invoice generation failed!")
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (transactionsState) {
                is Result.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                is Result.Success -> {
                    val transactions = (transactionsState as Result.Success<List<PaymentHistory>>).data
                    val mappedTransactions = transactions?.map {
                        Sample(
                            paymentDate = it.paymentDate.format(DateTimeFormatter.ofPattern("dd-MMM-yy")),
                            paymentAmount = if (it.paymentType == PaymentType.DEBT)
                                "(-) ₹${it.paymentAmount}" else "(+) ₹${it.paymentAmount}",
                            paymentType = when (it.paymentType) {
                                PaymentType.RENT -> "RENT"
                                PaymentType.DEBT -> "DEBT"
                                PaymentType.DEPOSIT -> "DEPOSIT"
                            }
                        )
                    }

                    TransactionTableHeader()

                    LazyColumn {
                        mappedTransactions?.let {
                            items(it) { transaction ->
                                TransactionTableRow(transaction)
                            }
                        }
                    }
                }

                is Result.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error: ${(transactionsState as Result.Error).message}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun TransactionTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("DATE", fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 8.dp) )
        Text("AMOUNT", fontWeight = FontWeight.Bold,)
        Text("TYPE", fontWeight = FontWeight.Bold,modifier = Modifier.padding(end = 8.dp) )
    }
}

@Composable
fun TransactionTableRow(transaction: Sample) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(4.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = transaction.paymentDate,
            modifier = Modifier.weight(2f),
            fontSize = 14.sp
        )

        Text(
            text = transaction.paymentAmount,
            color = if (transaction.paymentAmount.contains("(-)")) Color.Red else MaterialTheme.colorScheme.primary,
            fontSize = 14.sp,
            modifier = Modifier.weight(2f)
        )

        Box(
            modifier = Modifier
                .background(
                    if (transaction.paymentType == "DEPOSIT" || transaction.paymentType == "RENT") Color(0xFF4CAF50) else Color.Red,
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(4.dp)
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = transaction.paymentType,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}
