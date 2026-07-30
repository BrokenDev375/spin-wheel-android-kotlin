package com.vga.spinwheel.ui.screen.intro

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.vga.spinwheel.R
import com.vga.spinwheel.advertisement.AdPositions
import com.vga.spinwheel.advertisement.AdScenario
import com.vga.spinwheel.advertisement.NativeAdSlot
import com.vga.spinwheel.advertisement.NativeInterController
import com.vga.spinwheel.firebase.Remote
import com.vga.spinwheel.ui.components.SpinPrimaryButton
import com.vga.spinwheel.ui.components.clickableWithSound
import com.vga.spinwheel.ui.theme.SpinColors
import com.vga.spinwheel.ui.theme.SpinRadius
import com.vga.spinwheel.ui.theme.SpinSpacing
import kotlinx.coroutines.delay

@Composable
fun IntroScreen(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: IntroViewModel = hiltViewModel(),
) {
    val activity = LocalContext.current as? Activity
    val adPositions = remember { AdPositions.selected() }
    var pageIndex by remember { mutableIntStateOf(0) }
    var continueEnabled by remember { mutableStateOf(false) }
    var advancePending by remember { mutableStateOf(false) }
    val page = introPagesI18n[pageIndex]
    val slideNumber = pageIndex + 1
    val hasInlineAd = slideNumber in adPositions

    LaunchedEffect(pageIndex, hasInlineAd) {
        continueEnabled = !hasInlineAd
        advancePending = false
        if (hasInlineAd) {
            delay(INTRO_AD_TIMEOUT_MS)
            continueEnabled = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(SpinColors.Background)
            .padding(horizontal = SpinSpacing.ScreenHorizontal)
            .padding(top = 20.dp, bottom = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clip(RoundedCornerShape(SpinRadius.Sheet))
                .background(SpinColors.BackgroundDeep),
            contentAlignment = Alignment.Center,
        ) {
            IntroVisual(
                imageRes = page.imageRes,
                modifier = Modifier.fillMaxSize(),
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (hasInlineAd) {
            NativeAdSlot(
                placement = "native_intro$slideNumber",
                isSmall = true,
                onResolved = { continueEnabled = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        Text(
            text = stringResource(page.titleRes),
            color = SpinColors.Action,
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(page.descriptionRes),
            color = SpinColors.TextPrimary,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(16.dp))

        PageIndicator(
            pageCount = introPagesI18n.size,
            activeIndex = pageIndex,
            onPageClick = { pageIndex = it },
        )

        Spacer(modifier = Modifier.height(20.dp))

        SpinPrimaryButton(
            text = stringResource(
                if (pageIndex == introPagesI18n.lastIndex) {
                    R.string.start
                } else {
                    R.string.continue_btn
                }
            ),
            onClick = {
                if (!advancePending) {
                    advancePending = true
                    val advance = {
                        if (pageIndex == introPagesI18n.lastIndex) {
                            viewModel.markIntroDone(onSaved = onFinished)
                        } else {
                            pageIndex += 1
                        }
                    }
                    if (slideNumber * MODAL_POSITION_MULTIPLIER in adPositions) {
                        showIntroNativeInter(activity, advance)
                    } else {
                        advance()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = continueEnabled && !advancePending,
        )
    }
}

@Composable
private fun PageIndicator(
    pageCount: Int,
    activeIndex: Int,
    onPageClick: (Int) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        repeat(pageCount) { index ->
            Box(
                modifier = Modifier
                    .size(width = if (index == activeIndex) 34.dp else 14.dp, height = 14.dp)
                    .clip(CircleShape)
                    .background(if (index == activeIndex) SpinColors.Action else Color(0xFF45515D))
                    .clickableWithSound { onPageClick(index) },
            )
        }
    }
}

@Composable
private fun IntroVisual(
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
        )
    }
}

private fun showIntroNativeInter(
    activity: Activity?,
    onNext: () -> Unit,
) {
    if (activity == null) {
        onNext()
        return
    }

    val remote = Remote.instance
    val placement = INTRO_NATIVE_INTER_PLACEMENT
    val unitId = remote.adUnit(placement)
    val shouldShow = remote.isAdEnabled(placement) &&
        unitId.isNotBlank() &&
        AdScenario.shouldShow(
            activity,
            placement,
            ratio = remote.getInt("${placement}_ratio"),
            maxPerDay = remote.getInt("${placement}_max"),
        )

    if (shouldShow) {
        NativeInterController.show(placement, onNext)
    } else {
        onNext()
    }
}

private data class IntroPageI18n(
    @StringRes val titleRes: Int,
    @StringRes val descriptionRes: Int,
    @DrawableRes val imageRes: Int,
)

private val introPagesI18n = listOf(
    IntroPageI18n(
        titleRes = R.string.titileIntro1,
        descriptionRes = R.string.desIntro1,
        imageRes = R.drawable.img_intro_1,
    ),
    IntroPageI18n(
        titleRes = R.string.titileIntro2,
        descriptionRes = R.string.desIntro2,
        imageRes = R.drawable.img_intro_2,
    ),
    IntroPageI18n(
        titleRes = R.string.titileIntro3,
        descriptionRes = R.string.desIntro3,
        imageRes = R.drawable.img_intro_3,
    ),
    IntroPageI18n(
        titleRes = R.string.titileIntro4,
        descriptionRes = R.string.desIntro4,
        imageRes = R.drawable.img_intro_4,
    ),
)

private const val INTRO_AD_TIMEOUT_MS = 5_000L
private const val MODAL_POSITION_MULTIPLIER = 11
private const val INTRO_NATIVE_INTER_PLACEMENT = "native_inter_intro"

