package com.samapp.renttrack.data.repository

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.core.content.FileProvider
import com.samapp.renttrack.data.local.model.PaymentHistory
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.domain.repositories.invoice.RentInvoiceRepository
import com.samapp.renttrack.domain.repositories.invoice.TransactionInvoiceRepository
import com.samapp.renttrack.util.generateInvoiceFileName
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionInvoiceRepositoryImpl(
    private val context: Context
): TransactionInvoiceRepository {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("RentTrackPrefs", Context.MODE_PRIVATE)

    private fun getNextReceiptNumber(): Int {
        val lastNumber = sharedPreferences.getInt("last_receipt_number", 999)
        val newNumber = lastNumber + 1
        sharedPreferences.edit().putInt("last_receipt_number", newNumber).apply()
        return newNumber
    }

    override suspend fun generateInvoice(transactions: List<PaymentHistory>?): File? {
        val receiptNumber = getNextReceiptNumber()
        val formattedDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val fileNameWithoutExtension = generateInvoiceFileName(transactions!!)
        val fileName = "${fileNameWithoutExtension}.pdf"

        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(600, 1200, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        // Header Background
        paint.color = Color.DKGRAY
        canvas.drawRect(0f, 0f, 600f, 80f, paint)

        // Header Text
        paint.color = Color.WHITE
        paint.textSize = 24f
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("Transaction Invoice", 180f, 50f, paint)

        // Column Titles
        paint.color = Color.LTGRAY
        canvas.drawRect(0f, 90f, 600f, 140f, paint)

        paint.color = Color.BLACK
        paint.textSize = 16f
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("DATE", 20f, 130f, paint)
        canvas.drawText("MONTH", 140f, 130f, paint)
        canvas.drawText("TENANT ID", 260f, 130f, paint)
        canvas.drawText("AMOUNT", 400f, 130f, paint)
        canvas.drawText("TYPE", 520f, 130f, paint)

        var yPosition = 160f
        transactions?.forEach { transaction ->
            paint.textSize = 14f
            paint.typeface = Typeface.DEFAULT
            paint.color = Color.BLACK
            canvas.drawText(transaction.paymentDate.toString(), 20f, yPosition, paint)
            canvas.drawText(transaction.paymentForWhichMonth.toString(), 140f, yPosition, paint)
            canvas.drawText(transaction.tenantId.toString(), 260f, yPosition, paint)

            // Amount Formatting
            val amountText = "₹${transaction.paymentAmount}"
            paint.color = if (transaction.paymentAmount > 0) Color.BLUE else Color.RED
            canvas.drawText(amountText, 400f, yPosition, paint)

            // Type Box
            paint.color = if (transaction.paymentType.name.lowercase() == "debt") Color.RED else Color.GREEN
            canvas.drawRect(500f, yPosition - 15f, 580f, yPosition + 10f, paint)

            // Type Text
            paint.color = Color.WHITE
            canvas.drawText(transaction.paymentType.name, 520f, yPosition, paint)


            yPosition += 40f
        }

        pdfDocument.finishPage(page)

        val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName)
        return try {
            val outputStream = FileOutputStream(file)
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            outputStream.close()
            file
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    override fun shareInvoice(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Share Invoice"))
    }

}