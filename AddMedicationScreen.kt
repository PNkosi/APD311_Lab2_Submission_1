package com.perfectnkosi.apd311_lab2

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import java.util.Date


@SuppressLint("StringFormatInvalid")
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true, backgroundColor = 0xffffff)
@Composable
fun AddMedicationScreen() {

    var medicationName by rememberSaveable { mutableStateOf("") }
    var numberOfDosages by rememberSaveable { mutableStateOf("1") }
    var recurrence by rememberSaveable{ mutableStateOf(Recurrence.Daily.name) }
    var endDate by rememberSaveable { mutableLongStateOf(Date().time) }

    var isMorningSelected by rememberSaveable { mutableStateOf(false) }
    var isAfternoonSelected by rememberSaveable { mutableStateOf(false) }
    var isEveningSelected by rememberSaveable { mutableStateOf(false) }
    var isNightSelected by rememberSaveable { mutableStateOf(false) }

    Column (
        modifier = Modifier
            .padding(16.dp, 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        Text(
            text = stringResource(id = R.string.add_medication),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Text(
            text = stringResource(id = R.string.medication_name),
            style = MaterialTheme.typography.bodyLarge
        )

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = medicationName,
            onValueChange = { medicationName = it },
            placeholder = { Text(text = "e.g. Hexamine") },
        )

        // Implementing the drop down menu for the occurrence
        Spacer(modifier = Modifier.padding(4.dp))

        var isMaxDoseError by rememberSaveable { mutableStateOf(false) }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val maxDose = 3
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.dosage),
                    style = MaterialTheme.typography.bodyLarge
                )
                TextField(
                    modifier = Modifier.width(128.dp),
                    value = numberOfDosages,
                    onValueChange = {
                                    if (it.length < maxDose) {
                                        isMaxDoseError = false
                                        numberOfDosages = it
                                    } else {
                                        isMaxDoseError = true
                                    }
                    },
                    trailingIcon = {
                        if (isMaxDoseError) {
                            Icon(
                                imageVector = Icons.Filled.Info,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    placeholder = { Text(text = "e.g. 1") },
                    isError = isMaxDoseError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

            }
            RecurrenceDropdownMenu({ recurrence = it })
        }

        // Displaying error message if the user has entered more than 99 dosages per day
        if (isMaxDoseError) {
            Text(
                text = "You cannot have more than 99 dosages per day",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        // Implementing the date picker
        Spacer(modifier = Modifier.padding(4.dp))
        EndDateTextField{ endDate = it }

        // Implementing the time picker
        Spacer(modifier = Modifier.padding(4.dp))
        Text(
            text = stringResource(id = R.string.time_of_day),
            style = MaterialTheme.typography.bodyLarge
        )

        var selectionCount by rememberSaveable { mutableStateOf(0) }
        var context = LocalContext.current
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                selected = isMorningSelected,
                onClick = { 
                          handleSelection(
                              isSelected = isMorningSelected,
                              selectionCount = selectionCount,
                              canSelectMoreTimesOfDay = canSelectMoreTimesOfDay(
                                  selectionCount,
                                  numberOfDosages.toIntOrNull() ?: 0
                              ),
                              onStateChange = { count: Int, selected: Boolean ->
                                  isMorningSelected = selected
                                  selectionCount = count
                              },
                              onShowMaxSelectionError = {
                                  showMaxSelectionToast(numberOfDosages, context)
                              }
                          )
                },
                label = { Text(text = TimesOfDay.Morning.name) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Selected"
                    )
                }
            )

            FilterChip(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                selected = isAfternoonSelected,
                onClick = {
                    handleSelection(
                        isSelected = isAfternoonSelected,
                        selectionCount = selectionCount,
                        canSelectMoreTimesOfDay = canSelectMoreTimesOfDay(
                            selectionCount,
                            numberOfDosages.toIntOrNull() ?: 0
                        ),
                        onStateChange = { count: Int, selected: Boolean ->
                            isMorningSelected = selected
                            selectionCount = count
                        },
                        onShowMaxSelectionError = {
                            showMaxSelectionToast(numberOfDosages, context)
                        }
                    )
                },
                label = { Text(text = TimesOfDay.Afternoon.name) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Selected"
                    )
                }
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            FilterChip(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                selected = isEveningSelected,
                onClick = {
                    handleSelection(
                        isSelected = isEveningSelected,
                        selectionCount = selectionCount,
                        canSelectMoreTimesOfDay = canSelectMoreTimesOfDay(
                            selectionCount,
                            numberOfDosages.toIntOrNull() ?: 0
                        ),
                        onStateChange = { count: Int, selected: Boolean ->
                            isMorningSelected = selected
                            selectionCount = count
                        },
                        onShowMaxSelectionError = {
                            showMaxSelectionToast(numberOfDosages, context)
                        }
                    )
                },
                label = { Text(text = TimesOfDay.Evening.name) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Selected"
                    )
                }
            )
            FilterChip(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                selected = isNightSelected,
                onClick = {
                    handleSelection(
                        isSelected = isNightSelected,
                        selectionCount = selectionCount,
                        canSelectMoreTimesOfDay = canSelectMoreTimesOfDay(
                            selectionCount,
                            numberOfDosages.toIntOrNull() ?: 0
                        ),
                        onStateChange = { count: Int, selected: Boolean ->
                            isMorningSelected = selected
                            selectionCount = count
                        },
                        onShowMaxSelectionError = {
                            showMaxSelectionToast(numberOfDosages, context)
                        }
                    )
                },
                label = { Text(text = TimesOfDay.Night.name) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Selected"
                    )
                }
            )
        }

        // Implementing a Save button
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            onClick = {
                validateMedication(
                    name = medicationName,
                    dosage = numberOfDosages.toIntOrNull() ?: 0,
                    recurrence = recurrence,
                    endDate = endDate,
                    morningSelection = isMorningSelected,
                    afternoonSelection = isAfternoonSelected,
                    eveningSelection = isEveningSelected,
                    nightSelection = isNightSelected,
                    onInvalidate = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.value_is_empty, context.getString(it)),
                            Toast.LENGTH_LONG
                        ).show()
                    },
                    onValidate = {
                        // TODO("Navigate to next screen /  Store medication info")
                        Toast.makeText(
                            context,
                            context.getString(R.string.success),
                            Toast.LENGTH_LONG,
                        ).show()
                    }
                )
            },
            shape = MaterialTheme.shapes.extraLarge
        ) {
            Text(
                text = stringResource(id = R.string.save),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

fun canSelectMoreTimesOfDay(selectionCount: Any, i: Int): Any {
    TODO("Not yet implemented")
}

fun handleSelection(
    isSelected: Boolean,
    selectionCount: Int,
    canSelectMoreTimesOfDay: Any,
    onStateChange: Any,
    onShowMaxSelectionError: () -> Unit
) {
    TODO("Not yet implemented")
}

private fun showMaxSelectionToast(numberOfDosage: String, context: Context) {
    Toast.makeText(
        context,
        "You're selecting ${(numberOfDosage.toIntOrNull()?: 0)} dosages, which is more than the maximum of 3",
        Toast.LENGTH_LONG
    ).show()

}

private fun validateMedication(
    name: String,
    dosage: Int,
    recurrence: String,
    endDate: Long,
    morningSelection: Boolean,
    afternoonSelection: Boolean,
    eveningSelection: Boolean,
    nightSelection: Boolean,
    onInvalidate: (Int) -> Unit,
    onValidate: (Int) -> Unit,
) {
    if (name.isEmpty()) {
        onInvalidate(R.string.medication_name)
        return
    }

    if (dosage < 1) {
        onInvalidate(R.string.dosage)
        return
    }

    if (endDate < 1) {
        onInvalidate(R.string.end_date)
        return
    }

    if (!morningSelection &&!afternoonSelection &&!eveningSelection &&!nightSelection) {
        onInvalidate(R.string.times_of_day)
        return
    }

    val timesOfDay = mutableListOf<TimesOfDay>()
    if (morningSelection) timesOfDay.add(TimesOfDay.Morning)
    if (afternoonSelection) timesOfDay.add(TimesOfDay.Afternoon)
    if (eveningSelection) timesOfDay.add(TimesOfDay.Evening)
    if (nightSelection) timesOfDay.add(TimesOfDay.Night)

//    onValidate()
}