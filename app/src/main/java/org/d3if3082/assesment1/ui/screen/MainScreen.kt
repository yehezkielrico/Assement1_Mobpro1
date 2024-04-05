package org.d3if3082.assesment1.ui.screen

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3026.mobpro1.navigation.Screen
import org.d3if3082.assesment1.R
import org.d3if3082.assesment1.ui.theme.Assesment1Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name_project))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color(0xFFF5C851),
                    titleContentColor = Color.Black
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.About.route)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = stringResource(id = R.string.tentang_aplikasi),
                            tint = Color.Black
                        )
                    }
                }
            )
        }
    ) { padding ->
        ScreenContent(Modifier.padding(padding))
    }
}


@Composable
fun ScreenContent(modifier: Modifier) {

    var payementValue by rememberSaveable {
        mutableStateOf("")
    }
    var payementValueError by rememberSaveable {
        mutableStateOf(false)
    }

    val radioOptions = listOf(
        stringResource(R.string.bronzo),
        stringResource(R.string.silver),
        stringResource(R.string.gold),
    )

    var grade by rememberSaveable {
        mutableStateOf(radioOptions[0])
    }

    var tip by rememberSaveable {
        mutableFloatStateOf(0f)
    }
    var totalBill by rememberSaveable {
        mutableFloatStateOf(0f)
    }

    val context = LocalContext.current

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
           ,
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = Color(0xFFF5C851),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Image(
                painter = painterResource(id = R.drawable.th),
                contentDescription = "",
                modifier = Modifier
                    .size(160.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.size(15.dp))

            OutlinedTextField(
                value = payementValue,
                onValueChange = { payementValue = it },
                label = { Text(text = stringResource(R.string.payment_bill)) },
                isError = payementValueError,
                leadingIcon = { IconPicker(payementValueError, "Rp.") },
                supportingText = { ErrorHint(isError = payementValueError) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFFFFD882),
                    focusedContainerColor = Color(0xFFFFD882),
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                    cursorColor = Color.Black,
                    errorCursorColor = Color.Black,
                ),
                shape = RoundedCornerShape(8.dp)
            )

            Spacer(modifier = Modifier.size(15.dp))

            Text(
                text = "Chooese Grade",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            ) {
                radioOptions.forEach { text ->
                    GradeOption(
                        label = text,
                        isSelected = grade == text,
                        modifier = Modifier
                            .selectable(
                                selected = grade == text,
                                onClick = { grade = text },
                                role = Role.RadioButton
                            )
                            .padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.size(15.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = {
                        payementValueError = (payementValue == "" || payementValue == "0" || payementValue.isEmpty())
                        if(payementValueError) return@Button
                        totalBill = getTotalBill(payementValue.toFloat(), calculateGrade(grade, payementValue.toFloat()))
                        tip = calculateGrade(grade, payementValue.toFloat())
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Calculate")
                }
                Button(
                    onClick = {
                         payementValue = ""
                        payementValueError = false
                        grade = radioOptions[0]
                        tip = 0f
                        totalBill = 0f                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Text(text = "Reset")
                }
            }
        }

        Spacer(modifier = Modifier.size(15.dp))

        if(totalBill != 0f && tip != 0f) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .background(
                        color = Color(0xFFF5C851),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Result",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )

                    IconButton(
                        onClick = {
                            shareData(context,
                                context.getString(
                                    R.string.share_template,
                                    payementValue,
                                    grade,
                                    tip.toString(),
                                    totalBill.toString()
                                ))
                        },
                        modifier = Modifier.weight(1f),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share",
                            tint = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = stringResource(R.string.total_tip, tip),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = stringResource(R.string.total_bill, totalBill),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


fun calculateGrade(grade: String, payment: Float): Float {
    return when (grade) {
        "Bronze" -> payment * 5 / 100
        "Silver" -> payment * 10 / 100
        "Gold" -> payment * 15 / 100
        else -> 0f
    }
}


fun getTotalBill(payment: Float, tip: Float): Float {
    return payment + tip
}

private fun shareData(context: Context, message: String){
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, message)
    }
    if(shareIntent.resolveActivity(context.packageManager) != null){
        context.startActivity(shareIntent)
    }
}


@Composable
fun GradeOption(label: String, isSelected: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = null)
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}


@Composable
fun IconPicker(isError: Boolean, unit: String){
    if(isError){
        Icon(imageVector = Icons.Filled.Warning, contentDescription = null)
    } else {
        Text(text = unit)
    }
}

@Composable
fun ErrorHint(isError: Boolean){
    if(isError){
        Text(text = stringResource(id = R.string.input_invalid))
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    Assesment1Theme {
        MainScreen(rememberNavController())
    }
}