@file:OptIn(ExperimentalMaterial3Api::class)

package com.luukachoo.run.presentation.run_overview

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.luukachoo.core.presentation.designsystem.AnalyticsIcon
import com.luukachoo.core.presentation.designsystem.LogoIcon
import com.luukachoo.core.presentation.designsystem.LogoutIcon
import com.luukachoo.core.presentation.designsystem.RunIcon
import com.luukachoo.core.presentation.designsystem.RuniqueTheme
import com.luukachoo.core.presentation.designsystem.components.RuniqueFloatingActionButton
import com.luukachoo.core.presentation.designsystem.components.RuniqueScaffold
import com.luukachoo.core.presentation.designsystem.components.RuniqueToolbar
import com.luukachoo.core.presentation.designsystem.components.util.DropDownItem
import com.luukachoo.run.presentation.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunOverviewScreenRoot(
    viewModel: RunOverviewViewModel = koinViewModel()
) {
    RunOverviewScreen(
        onAction = viewModel::onAction
    )
}

@Composable
private fun RunOverviewScreen(
    onAction: (RunOverviewAction) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState
    )

    RuniqueScaffold(
        topAppBar = {
            RuniqueToolbar(
                showBackButton = false,
                title = stringResource(id = R.string.runique),
                scrollBehaviour = scrollBehavior,
                menuItems = listOf(
                    DropDownItem(
                        icon = AnalyticsIcon,
                        title = stringResource(id = R.string.analytics)
                    ),
                    DropDownItem(
                        icon = LogoutIcon,
                        title = stringResource(id = R.string.log_out)
                    )
                ),
                startContent = {
                    Icon(
                        imageVector = LogoIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                },
                onMenuItemClick = { index ->
                    when(index) {
                        0 -> onAction(RunOverviewAction.OnAnalyticsClick)
                        1 -> onAction(RunOverviewAction.OnLogOutClick)
                    }
                }
            )
        },
        floatingActionButton = {
            RuniqueFloatingActionButton(
                icon = RunIcon,
                onClick = { onAction(RunOverviewAction.OnStartClick) })
        }
    ) { padding ->

    }
}

@Preview
@Composable
private fun RunOverviewScreenPreview() {
    RuniqueTheme {
        RunOverviewScreen(
            onAction = {}
        )
    }
}