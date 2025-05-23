package com.yangdai.opennote.presentation.component.setting

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Http
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yangdai.opennote.R
import com.yangdai.opennote.presentation.util.WebDavClient
import kotlinx.coroutines.launch

@Composable
fun CloudPane() {

    val webDAVUrlState = rememberTextFieldState()
    val webDAVAccountState = rememberTextFieldState()
    val webDAVPasswordState = rememberTextFieldState()
    var showWebDAVPassword by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // State for WebDAV connection results
    var isLoading by remember { mutableStateOf(false) }
    var connectionResult by remember { mutableStateOf<Result<List<String>>?>(null) }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.WarningAmber,
                    contentDescription = "Notification",
                    tint = MaterialTheme.colorScheme.error
                )
            },
            headlineContent = { Text(text = stringResource(R.string.the_feature_is_still_under_construction)) },
            colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.errorContainer)
        )

        SettingsHeader(text = "WebDAV")

        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.Http,
                    contentDescription = stringResource(R.string.url)
                )
            },
            headlineContent = {
                BasicTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = webDAVUrlState,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = true,
                        keyboardType = KeyboardType.Uri,
                        imeAction = ImeAction.Done
                    ),
                    onKeyboardAction = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    decorator = { innerTextField ->
                        Box {
                            if (webDAVUrlState.text.isEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.url),
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            })

        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    contentDescription = stringResource(R.string.username)
                )
            },
            headlineContent = {
                BasicTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = webDAVAccountState,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = true,
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    onKeyboardAction = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    decorator = { innerTextField ->
                        Box {
                            if (webDAVAccountState.text.isEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.username),
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            })

        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.Password,
                    contentDescription = stringResource(id = R.string.pass)
                )
            },
            headlineContent = {
                BasicSecureTextField(
                    modifier = Modifier.fillMaxWidth(),
                    state = webDAVPasswordState,
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = true,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    onKeyboardAction = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    },
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface),
                    decorator = { innerTextField ->
                        Box {
                            if (webDAVPasswordState.text.isEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.pass),
                                    style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                            innerTextField()
                        }
                    },
                    textObfuscationMode = if (showWebDAVPassword) TextObfuscationMode.Visible else TextObfuscationMode.RevealLastTyped
                )
            },
            trailingContent = {
                IconButton(
                    onClick = { showWebDAVPassword = !showWebDAVPassword }
                ) {
                    Icon(
                        imageVector = if (showWebDAVPassword) Icons.Outlined.VisibilityOff else Icons.Outlined.Visibility,
                        contentDescription = "Visibility toggle"
                    )
                }
            })

        val scope = rememberCoroutineScope()

        TextButton(
            enabled = !isLoading && webDAVUrlState.text.isNotEmpty() &&
                    webDAVAccountState.text.isNotEmpty() &&
                    webDAVPasswordState.text.isNotEmpty(),
            colors = ButtonDefaults.textButtonColors()
                .copy(containerColor = MaterialTheme.colorScheme.secondaryContainer),
            onClick = {
                scope.launch {
                    isLoading = true
                    keyboardController?.hide()
                    focusManager.clearFocus()
                    connectionResult = null
                    errorMessage = ""

                    try {
                        val client = WebDavClient(
                            baseUrl = webDAVUrlState.text.toString(),
                            username1 = webDAVAccountState.text.toString(),
                            password1 = webDAVPasswordState.text.toString()
                        )

                        val result = client.testConnection()
                        result.fold(
                            onSuccess = {
                                // If connection is successful, try to list files
                                val filesList = client.listDirectory("")
                                connectionResult = filesList
                            },
                            onFailure = { exception ->
                                errorMessage = "Connection failed: ${exception.message}"
                            }
                        )
                        client.close()
                    } catch (e: Exception) {
                        errorMessage = "Error: ${e.message}"
                    } finally {
                        isLoading = false
                    }
                }
            }
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(end = 8.dp),
                    strokeWidth = 2.dp
                )
                Text(text = "Connecting...")
            } else {
                Text(text = "Test connection")
            }
        }
        
        // Display connection results
        if (isLoading) {
            // Show loading indicator
        } else if (errorMessage.isNotEmpty()) {
            // Show error message
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        } else if (connectionResult != null) {
            // Show connection success and file list
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Connection successful!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    
                    connectionResult?.fold(
                        onSuccess = { files ->
                            Text(
                                text = "Files in WebDAV:",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                            )
                            if (files.isEmpty()) {
                                Text(text = "No files found")
                            } else {
                                Column {
                                    files.forEach { file ->
                                        Text(
                                            text = "• ${file.substringAfterLast("/")}",
                                            modifier = Modifier.padding(vertical = 2.dp)
                                        )
                                    }
                                }
                            }
                        },
                        onFailure = { error ->
                            Text(
                                text = "Could not list files: ${error.message}",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    )
                }
            }
        }
    }
}
