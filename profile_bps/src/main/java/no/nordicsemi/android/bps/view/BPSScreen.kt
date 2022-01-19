package no.nordicsemi.android.bps.view

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import no.nordicsemi.android.bps.R
import no.nordicsemi.android.bps.viewmodel.BPSViewModel
import no.nordicsemi.android.theme.view.BackIconAppBar
import no.nordicsemi.android.theme.view.DeviceConnectingView
import no.nordicsemi.android.utils.exhaustive

@Composable
fun BPSScreen() {
    val viewModel: BPSViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.connectDevice()
    }

    BPSView(state) { viewModel.onEvent(it) }
}

@Composable
private fun BPSView(state: BPSViewState, onEvent: (BPSScreenViewEvent) -> Unit) {
    Column {
        BackIconAppBar(stringResource(id = R.string.bps_title)) {
            onEvent(DisconnectEvent)
        }

        when (state) {
            is DisplayDataState -> BPSContentView(state.data) { onEvent(it) }
            LoadingState -> DeviceConnectingView()
        }.exhaustive
    }
}
