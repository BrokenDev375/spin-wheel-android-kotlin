package com.vga.spinwheel.ui.nav

import androidx.annotation.StringRes
import com.vga.spinwheel.R

enum class Screen(
    val route: String,
    @StringRes val titleRes: Int = R.string.spinwheel,
) {
    // Hub & Onboarding
    Intro("intro", R.string.spinwheel),
    Home("home", R.string.spinwheel),

    // Wheel
    Wheel("wheel", R.string.roulette),
    WheelAdd("wheel/add", R.string.roulette),
    WheelEdit("wheel/{wheelId}/edit", R.string.roulette),
    WheelSpin("wheel/{wheelId}/spin", R.string.roulette),
    WheelSettings("wheel/{wheelId}/settings", R.string.roulette),
    WheelResult("wheel/{wheelId}/result/{resultId}", R.string.roulette),
    WheelHistory("wheel/{wheelId}/history", R.string.roulette),
    WheelPalette("wheel/{wheelId}/palette", R.string.roulette),

    // Finger
    Finger("finger", R.string.fingerChooser),

    // Coin
    Coin("coin", R.string.coin),
    CoinSettings("coin/settings", R.string.coin),
    CoinLabel("coin/label", R.string.coin),
    CoinResult("coin/result/{isHeads}", R.string.coin),

    // Team
    Team("team", R.string.homograft),
    TeamAdd("team/add", R.string.homograft),
    TeamEdit("team/{listId}/edit", R.string.homograft),
    TeamDetail("team/{listId}", R.string.homograft),
    TeamSettings("team/{listId}/settings", R.string.homograft),
    TeamPreview("team/{listId}/preview", R.string.homograft),

    // Number
    Number("number", R.string.randerNum),
    NumberHome("number_home", R.string.randerNum),
    NumberSettings("number_settings", R.string.randerNum),
    NumberResult("number_result", R.string.randerNum),
    NumberHistory("number_history", R.string.randerNum),

    // Drawing
    Drawing("drawing", R.string.drawn),
    DrawingHome("drawing_home", R.string.drawn),
    DrawingAdd("drawing_add", R.string.drawn),
    DrawingEdit("drawing_edit/{wheelId}", R.string.drawn),
    DrawingAiForm("drawing_ai_form", R.string.drawn),
    DrawingSpin("drawing_spin/{wheelId}", R.string.drawn),
    DrawingResult("drawing_result/{wheelId}", R.string.drawn),
    DrawingSettings("drawing_settings/{wheelId}", R.string.drawn),
    DrawingPalette("drawing_palette/{wheelId}", R.string.drawn),

    // Bottle
    Bottle("bottle", R.string.spinBottle),
    BottleSettings("bottle/settings", R.string.spinBottle),
    BottleLabel("bottle/label", R.string.spinBottle),

    // Dice
    Dice("dice", R.string.diceRoller),
    DiceHome("dice_home", R.string.diceRoller),
    DiceSettings("dice_settings", R.string.diceRoller),
    DiceLabel("dice_label", R.string.diceRoller),
    DicePreview("dice_preview", R.string.diceRoller),
    DiceResult("dice_result", R.string.diceRoller),

    // Card
    Card("card", R.string.card),
    CardSettings("card/settings", R.string.card),
    CardLabel("card/label", R.string.card),

    // Settings
    Settings("settings", R.string.settings);

    companion object {
        const val ARG_WHEEL_ID = "wheelId"
        const val ARG_RESULT_ID = "resultId"
        const val ARG_LIST_ID = "listId"
        const val ARG_IS_HEADS = "isHeads"

        fun wheelEdit(wheelId: String) = "wheel/$wheelId/edit"
        fun wheelSpin(wheelId: String) = "wheel/$wheelId/spin"
        fun wheelSettings(wheelId: String) = "wheel/$wheelId/settings"
        fun wheelResult(wheelId: String, resultId: String) = "wheel/$wheelId/result/$resultId"
        fun wheelHistory(wheelId: String) = "wheel/$wheelId/history"
        fun wheelPalette(wheelId: String) = "wheel/$wheelId/palette"

        fun coinResult(isHeads: Boolean) = "coin/result/$isHeads"

        fun teamEdit(listId: String) = "team/$listId/edit"
        fun teamDetail(listId: String) = "team/$listId"
        fun teamSettings(listId: String) = "team/$listId/settings"
        fun teamPreview(listId: String) = "team/$listId/preview"

        fun drawingEdit(wheelId: String) = "drawing_edit/$wheelId"
        fun drawingSpin(wheelId: String) = "drawing_spin/$wheelId"
        fun drawingResult(wheelId: String) = "drawing_result/$wheelId"
        fun drawingSettings(wheelId: String) = "drawing_settings/$wheelId"
        fun drawingPalette(wheelId: String) = "drawing_palette/$wheelId"
    }
}

val featureScreens = listOf(
    Screen.Wheel,
    Screen.Finger,
    Screen.Coin,
    Screen.Team,
    Screen.Number,
    Screen.Drawing,
    Screen.Bottle,
    Screen.Dice,
    Screen.Card,
)
