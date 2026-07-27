package com.vga.spinwheel.ui.nav

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.vga.spinwheel.ui.screen.team.TeamDetailScreen
import com.vga.spinwheel.ui.screen.team.TeamHomeScreen
import com.vga.spinwheel.ui.screen.team.TeamPreviewScreen
import com.vga.spinwheel.ui.screen.team.TeamSettingsScreen
import com.vga.spinwheel.ui.screen.team.TeamViewModel
import com.vga.spinwheel.ui.screen.wheel.WheelAddEditScreen
import com.vga.spinwheel.ui.screen.wheel.WheelViewModel

fun NavGraphBuilder.teamNavGraph(
    navController: NavController,
    onBack: () -> Unit,
) {
    composable(Screen.Team.route) { backStackEntry ->
        val teamViewModel: TeamViewModel = hiltViewModel(backStackEntry)
        val wheelFormViewModel: WheelViewModel = hiltViewModel(backStackEntry)

        TeamHomeScreen(
            viewModel = teamViewModel,
            wheelFormViewModel = wheelFormViewModel,
            onBack = onBack,
            onAddList = {
                wheelFormViewModel.prepareNewForm()
                navController.navigate(Screen.TeamAdd.route)
            },
            onOpenPreparedForm = {
                navController.navigate(Screen.TeamAdd.route)
            },
            onEditList = { listId ->
                wheelFormViewModel.prepareEditForm(listId)
                navController.navigate(Screen.teamEdit(listId))
            },
            onOpenList = { listId ->
                teamViewModel.openList(listId)
                navController.navigate(Screen.teamDetail(listId))
            },
        )
    }

    composable(Screen.TeamAdd.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Team.route)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)

        WheelAddEditScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onSaveSuccess = { navController.popBackStack() },
        )
    }

    composable(
        route = Screen.TeamEdit.route,
        arguments = listOf(
            navArgument(Screen.ARG_LIST_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Team.route)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)

        WheelAddEditScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onSaveSuccess = { navController.popBackStack() },
        )
    }

    composable(
        route = Screen.TeamDetail.route,
        arguments = listOf(
            navArgument(Screen.ARG_LIST_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Team.route)
        }
        val viewModel: TeamViewModel = hiltViewModel(parentEntry)
        val listId = backStackEntry.arguments?.getString(Screen.ARG_LIST_ID) ?: ""

        TeamDetailScreen(
            listId = listId,
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onOpenSettings = { navController.navigate(Screen.teamSettings(listId)) },
            onPreview = { navController.navigate(Screen.teamPreview(listId)) },
        )
    }

    composable(
        route = Screen.TeamSettings.route,
        arguments = listOf(
            navArgument(Screen.ARG_LIST_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Team.route)
        }
        val viewModel: TeamViewModel = hiltViewModel(parentEntry)

        TeamSettingsScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
        )
    }

    composable(
        route = Screen.TeamPreview.route,
        arguments = listOf(
            navArgument(Screen.ARG_LIST_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(Screen.Team.route)
        }
        val viewModel: TeamViewModel = hiltViewModel(parentEntry)
        val listId = backStackEntry.arguments?.getString(Screen.ARG_LIST_ID) ?: ""

        TeamPreviewScreen(
            viewModel = viewModel,
            onHome = {
                navController.popBackStack(Screen.Home.route, inclusive = false)
            },
            onRetry = {
                viewModel.retryMatching()
                if (!navController.popBackStack()) {
                    navController.navigate(Screen.teamDetail(listId))
                }
            },
        )
    }
}
