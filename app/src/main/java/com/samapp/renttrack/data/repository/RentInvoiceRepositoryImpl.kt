package com.samapp.renttrack.data.repository

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Environment
import android.util.Log
import androidx.core.content.FileProvider
import com.itextpdf.forms.PdfAcroForm
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfReader
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.samapp.renttrack.data.local.model.Tenant
import com.samapp.renttrack.domain.repositories.invoice.RentInvoiceRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale

class RentInvoiceRepositoryImpl(private val context: Context) : RentInvoiceRepository {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("RentTrackPrefs", Context.MODE_PRIVATE)

    private fun getNextReceiptNumber(): Int {
        val lastNumber = sharedPreferences.getInt("last_receipt_number", 999)
        val newNumber = lastNumber + 1
        sharedPreferences.edit().putInt("last_receipt_number", newNumber).apply()
        return newNumber
    }
//    override suspend fun generateInvoice(tenant: Tenant,amount: String, rentDate: String): File?{
//        try {
//            // Load the blank template from assets
//            val assetManager = context.assets
//            val templateInputStream = assetManager.open("fillable_invoice.pdf")
//            val assetFiles = context.assets.list("")?.toList()
//            Log.d("Assets", "Files: $assetFiles")
//
//            // Create output file
//            val outputDir = File(context.getExternalFilesDir(null), "Invoices")
//            if (!outputDir.exists()) outputDir.mkdirs()
//            val outputFile = File(outputDir, "Invoice_${System.currentTimeMillis()}.pdf")
//
//            val pdfReader = PdfReader(templateInputStream)
//            val pdfWriter = PdfWriter(FileOutputStream(outputFile))
//            val pdfDocument = PdfDocument(pdfReader, pdfWriter)
//
////            val document = Document(pdfDocument)
////            val invoiceNo = getNextReceiptNumber()
////            // Read the entire PDF text and replace placeholders
////            val pdfContent = StringBuilder()
////            val strategy = SimpleTextExtractionStrategy()
////            for (i in 1..pdfDocument.numberOfPages) {
////                pdfContent.append(PdfTextExtractor.getTextFromPage(pdfDocument.getPage(i), strategy))
////            }
//
//            val form = PdfAcroForm.getAcroForm(pdfDocument, true)
//            if (form != null) {
//                form.getField("TenantName")?.setValue(tenant.name)
//                form.getField("invoiceNo")?.setValue(getNextReceiptNumber().toString())
//                form.getField("RentAmount")?.setValue(tenant.monthlyRent.toString())
//                form.getField("DueAmount")?.setValue(tenant.outstandingDebt.toString())
//                form.getField("DepositAmount")?.setValue(tenant.deposit.toString())
//                form.getField("InvoiceDate")?.setValue(LocalDate.now().toString())
//
//                val total = tenant.monthlyRent!! - tenant.outstandingDebt!! + tenant.deposit!!
//                form.getField("TOTAL")?.setValue(total.toString())
//            }
//            form.flattenFields()
//            pdfDocument.close()
//            return outputFile
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return null
//        }
//    }
    override suspend fun generateInvoice(tenant: Tenant,amount: String, rentDate: String): File? {
        Log.d("Invoice", "Generating invoice for ${tenant.name} with amount ₹$amount on $rentDate")

        val receiptNumber = getNextReceiptNumber()
        val formattedDate = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Date())
        val fileName = "${tenant.name}_${formattedDate}.pdf"

        val pdfDocument = android.graphics.pdf.PdfDocument()
        val pageInfo = android.graphics.pdf.PdfDocument.PageInfo.Builder(600, 800, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        paint.textSize = 24f
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("RentTrack", 220f, 50f, paint)

        paint.textSize = 14f
        paint.typeface = Typeface.DEFAULT
        canvas.drawText("Payment Receipt", 230f, 80f, paint)

        paint.textSize = 12f
        canvas.drawText("Tenant: ${tenant.name}", 50f, 130f, paint)
        canvas.drawText("Bill Date: ${LocalDate.now()}", 50f, 160f, paint)
        canvas.drawText("Receipt No: ${receiptNumber}", 50f, 190f, paint)

        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("Description", 50f, 230f, paint)
        canvas.drawText("Amount", 400f, 230f, paint)

        paint.typeface = Typeface.DEFAULT
        canvas.drawText("Paid Amount", 50f, 290f, paint)
        canvas.drawText("₹$amount", 400f, 290f, paint)

        canvas.drawText("Debt", 50f, 320f, paint)
        canvas.drawText("₹${tenant.outstandingDebt}", 400f, 320f, paint)

        canvas.drawText("Deposit", 50f, 350f, paint)
        canvas.drawText("₹${tenant.deposit}", 400f, 350f, paint)

        val remainingAmount = tenant.deposit?.minus((tenant.outstandingDebt ?: 0.0))
        paint.typeface = Typeface.DEFAULT_BOLD
        canvas.drawText("Remaining Amount", 50f, 400f, paint)
        canvas.drawText("₹${
            remainingAmount
        }", 400f, 400f, paint)

        canvas.drawText("Mode: CASH", 50f, 450f, paint)
        canvas.drawText("Collected By: RentTrack", 50f, 480f, paint)

        paint.textSize = 10f
        paint.typeface = Typeface.MONOSPACE
        canvas.drawText("Generated by RentTrack", 200f, 550f, paint)
        canvas.drawText(
            "This is a computer-generated receipt and does not require any signature/stamp.",
            100f,
            570f,
            paint
        )

        pdfDocument.finishPage(page)

        val file =
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "${fileName}")
        try {
            val outputStream = FileOutputStream(file)
            pdfDocument.writeTo(outputStream)
            pdfDocument.close()
            outputStream.close()
            Log.d("Invoice", "Invoice saved at: ${file.absolutePath}")
            return file
        } catch (e: IOException) {
            e.printStackTrace()
            Log.d("Invoice", "Invoice failed : ${file.absolutePath}")
            return null
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
