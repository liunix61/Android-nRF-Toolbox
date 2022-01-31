package no.nordicsemi.android.uart.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.theme.view.BackIconAppBar
import no.nordicsemi.android.theme.view.DeviceConnectingView
import no.nordicsemi.android.uart.R
import no.nordicsemi.android.uart.viewmodel.UARTViewModel
import no.nordicsemi.android.utils.exhaustive

@Composable
fun UARTScreen() {
    val viewModel: UARTViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    Column {
        BackIconAppBar(stringResource(id = R.string.uart_title)) {
            viewModel.onEvent(OnDisconnectButtonClick)
        }

        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            when (state) {
                is DisplayDataState -> UARTContentView(state.data) { viewModel.onEvent(it) }
                LoadingState -> DeviceConnectingView()
            }.exhaustive
        }
    }
}