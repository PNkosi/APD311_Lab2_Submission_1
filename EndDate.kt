package com.perfectnkosi.apd311_lab2

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.widget.DatePicker
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalStdlibApi::class)
@Composable
fun EndDateTextField(endDate: (Long) -> Unit) {
    Text(
        text = stringResource(id = R.string.end_date),
        style = MaterialTheme.typography.bodyLarge
    )

    val interactionResource = remember { MutableInteractionSource() }
    val isPressed: Boolean by interactionResource.collectIsPressedAsState()

     val currentDate = Date().toFormattedString()
    var selectedDate by remember { mutableStateOf("") }

    val context = LocalContext.current

    val calender = Calendar.getInstance()
    val year: Int = calender.get(Calendar.YEAR)
    val month: Int = calender.get(Calendar.MONTH)
    val day: Int = calender.get(Calendar.DAY_OF_MONTH)
    calender.time = Date()

    val datePickerDialog =
        DatePickerDialog(context, { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val newDate = Calendar.getInstance()
            newDate.set(year, month, dayOfMonth)
            selectedDate = "${month.toMonthName()} $dayOfMonth $year"
            endDate(newDate.timeInMillis)
        }, year, month, day)

    TextField(
        modifier = Modifier.fillMaxWidth(),
        readOnly = true,
        value = selectedDate,
        onValueChange = {},
        trailingIcon = { Icons.Default.DateRange },
        interactionSource = interactionResource,
    )

    if (isPressed) {
        datePickerDialog.show()
    }
}

fun Int.toMonthName(): String {
    return DateFormatSymbols().months[this]
}

fun Date.toFormattedString(): String {
    val simpleDateFormat = SimpleDateFormat("LLLL dd, yyyy", Locale.getDefault())
    return simpleDateFormat.format(this)
}