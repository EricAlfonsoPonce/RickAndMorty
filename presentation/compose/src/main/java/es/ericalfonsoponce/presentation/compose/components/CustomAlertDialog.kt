package es.ericalfonsoponce.presentation.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import es.ericalfonsoponce.presentation.compose.R
import es.ericalfonsoponce.presentation.compose.theme.saveButtonColor

@Composable
fun CustomAlertDialog(
    title: String,
    message: String,
    buttonText: String = stringResource(R.string.default_dialog_button),
    isDismissible: Boolean = false,
    onDismiss: () -> Unit = {},
    onClose: () -> Unit = {},
    displayButton: Boolean = true
) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = { if (isDismissible) onDismiss() }) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.White)
            ) {
                Text(
                    text = title.ifEmpty { stringResource(R.string.default_dialog_title) },
                    textAlign = TextAlign.Center,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                Text(
                    text = message.ifEmpty { stringResource(R.string.default_dialog_message) },
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                if (displayButton) {
                    Button(
                        onClick = {
                            onClose()
                        },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(saveButtonColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text(
                            text = buttonText,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black,
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }
    }
}

@Preview(apiLevel = 35)
@Composable
fun CustomAlertDialogPreview() {
    CustomAlertDialog("", "")
}