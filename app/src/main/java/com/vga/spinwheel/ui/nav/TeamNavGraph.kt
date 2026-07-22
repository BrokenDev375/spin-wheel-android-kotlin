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
    composable(TeamRoutes.HOME) { backStackEntry ->
        val teamViewModel: TeamViewModel = hiltViewModel(backStackEntry)
        val wheelFormViewModel: WheelViewModel = hiltViewModel(backStackEntry)

        TeamHomeScreen(
            viewModel = teamViewModel,
            wheelFormViewModel = wheelFormViewModel,
            onBack = onBack,
            onAddList = {
                wheelFormViewModel.prepareNewForm()
                navController.navigate(TeamRoutes.ADD)
            },
            onOpenPreparedForm = {
                navController.navigate(TeamRoutes.ADD)
            },
            onEditList = { listId ->
                wheelFormViewModel.prepareEditForm(listId)
                navController.navigate(TeamRoutes.edit(listId))
            },
            onOpenList = { listId ->
                teamViewModel.openList(listId)
                navController.navigate(TeamRoutes.detail(listId))
            },
        )
    }

    composable(TeamRoutes.ADD) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(TeamRoutes.HOME)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)

        WheelAddEditScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onSaveSuccess = { navController.popBackStack() },
        )
    }

    composable(
        route = TeamRoutes.EDIT,
        arguments = listOf(
            navArgument(TeamRoutes.ARG_LIST_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(TeamRoutes.HOME)
        }
        val viewModel: WheelViewModel = hiltViewModel(parentEntry)

        WheelAddEditScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onSaveSuccess = { navController.popBackStack() },
        )
    }

    composable(
        route = TeamRoutes.DETAIL,
        arguments = listOf(
            navArgument(TeamRoutes.ARG_LIST_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(TeamRoutes.HOME)
        }
        val viewModel: TeamViewModel = hiltViewModel(parentEntry)
        val listId = backStackEntry.arguments?.getString(TeamRoutes.ARG_LIST_ID) ?: ""

        TeamDetailScreen(
            listId = listId,
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
            onOpenSettings = { navController.navigate(TeamRoutes.settings(listId)) },
            onPreview = { navController.navigate(TeamRoutes.preview(listId)) },
        )
    }

    composable(
        route = TeamRoutes.SETTINGS,
        arguments = listOf(
            navArgument(TeamRoutes.ARG_LIST_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(TeamRoutes.HOME)
        }
        val viewModel: TeamViewModel = hiltViewModel(parentEntry)

        TeamSettingsScreen(
            viewModel = viewModel,
            onBack = { navController.popBackStack() },
        )
    }

    composable(
        route = TeamRoutes.PREVIEW,
        arguments = listOf(
            navArgument(TeamRoutes.ARG_LIST_ID) { type = NavType.StringType },
        ),
    ) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry(TeamRoutes.HOME)
        }
        val viewModel: TeamViewModel = hiltViewModel(parentEntry)
        val listId = backStackEntry.arguments?.getString(TeamRoutes.ARG_LIST_ID) ?: ""

        TeamPreviewScreen(
            viewModel = viewModel,
            onHome = {
                navController.popBackStack(Screen.Home.route, inclusive = false)
            },
            onRetry = {
                viewModel.retryMatching()
                if (!navController.popBackStack()) {
                    navController.navigate(TeamRoutes.detail(listId))
                }
            },
        )
    }
}
