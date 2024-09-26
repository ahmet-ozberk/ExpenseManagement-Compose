package tosbik.ao.parayonetimi.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import tosbik.ao.parayonetimi.R
import tosbik.ao.parayonetimi.ui.theme.AppColor

@Composable
fun AppTextField(
    value: MutableState<String>? = null,
    textValue: String? = null,
    onValueChange: (String) -> Unit,
    leading: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
    onSubmitted: ((String) -> Unit)? = null,
    hintText: String = "",
    isFocused: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
    isPassword: Boolean = false,
    enabled: Boolean = true,
    stringLength: Int? = null,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    onError: @Composable ((String) -> Unit)? = null
) {
    var isFocusedState by remember { mutableStateOf(isFocused) }
    var isPass by remember { mutableStateOf(true) }
    val localFocusManager = LocalFocusManager.current


    Column {
        Box(modifier = modifier
            .fillMaxWidth()
            .drawBehind {
                if (isFocusedState) {
                    drawRect(
                        color = AppColor.Primary,
                        topLeft = Offset(0f, size.height - 1.dp.toPx()),
                        size = Size(size.width, 1.dp.toPx())
                    )
                } else {
                    drawRect(
                        color = Color.Gray,
                        topLeft = Offset(0f, size.height - 0.4.dp.toPx()),
                        size = Size(size.width, 0.4.dp.toPx())
                    )
                }
            }
            //.clickable(onClick = onTap ?: {})
            .padding(start = 8.dp, end = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                if (leading != null) {
                    Box(modifier = Modifier.height(17.dp)) { leading() }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                BasicTextField(value = textValue ?: value?.value ?: "",
                    onValueChange = {
                        if (stringLength == null || it.length <= stringLength) {
                            onValueChange(it)
                        }
                    },
                    singleLine = true,
                    enabled = enabled,
                    readOnly = enabled.not(),
                    textStyle = TextStyle(
                        fontSize = 18.sp, color = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = keyboardType, imeAction = imeAction
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        onSubmitted?.invoke(value?.value ?: "")
                        localFocusManager.clearFocus()
                    }),
                    cursorBrush = SolidColor(AppColor.Primary),
                    maxLines = maxLines,
                    decorationBox = { innerTextField ->
                        Box(
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (value?.value?.isEmpty() == true) {
                                AppText(
                                    text = hintText,
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        color = Color.Gray,
                                    ),
                                )
                            }
                            Box(modifier = Modifier.padding(vertical = 12.dp)) {
                                innerTextField()
                            }
                        }
                    },
                    visualTransformation = if (isPassword) if (!isPass) VisualTransformation.None else PasswordVisualTransformation() else VisualTransformation.None,
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { focusState ->
                            isFocusedState = focusState.isFocused
                        })
                if (trailing != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Box(modifier = Modifier.height(17.dp)) { trailing() }
                }
                if (isPassword) {
                    if (trailing != null) {
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    IconButton(onClick = { isPass = !isPass }, modifier = Modifier.height(17.dp)) {
                        if (!isPass) {
                            Image(
                                painter = painterResource(id = R.drawable.eye_off_sharp),
                                modifier = Modifier.size(20.dp),
                                contentScale = ContentScale.Inside,
                                contentDescription = null
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.eye_sharp),
                                modifier = Modifier.size(20.dp),
                                contentScale = ContentScale.Inside,
                                contentDescription = null
                            )
                        }
                    }
                }

            }
        }
        if (onError != null && value?.value?.isNotEmpty() == true) {
            onError(value.value)
        }
    }
}