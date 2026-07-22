package com.vga.spinwheel.ui.screen.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vga.spinwheel.data.model.AppSettingKeys
import com.vga.spinwheel.data.model.RandomFeature
import com.vga.spinwheel.data.repo.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class PaymentPlan(
    val key: String,
    val title: String,
    val price: String,
    val subtitle: String,
) {
    WEEKLY(
        key = "weekly",
        title = "Subscribe Weekly",
        price = "394.000 đ/Week",
        subtitle = "",
    ),
    ANNUAL(
        key = "annual",
        title = "Subscribe Annually",
        price = "65.750 đ/Month",
        subtitle = "3-DAYS FREE TRIAL! Billed Yearly 789.000 đ",
    ),
    MONTHLY(
        key = "monthly",
        title = "Subscribe Monthly",
        price = "263.000 đ/Month",
        subtitle = "",
    );

    companion object {
        fun fromKey(key: String): PaymentPlan =
            entries.firstOrNull { it.key == key } ?: ANNUAL
    }
}

data class PaymentUiState(
    val selectedPlan: PaymentPlan = PaymentPlan.ANNUAL,
)

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        PaymentUiState(
            selectedPlan = PaymentPlan.fromKey(
                settingsRepository.getString(
                    RandomFeature.APP,
                    AppSettingKeys.PAYMENT_PLAN,
                    PaymentPlan.ANNUAL.key,
                )
            )
        )
    )
    val uiState: StateFlow<PaymentUiState> = _uiState.asStateFlow()

    fun selectPlan(plan: PaymentPlan) {
        _uiState.update { it.copy(selectedPlan = plan) }
        viewModelScope.launch {
            settingsRepository.putString(
                feature = RandomFeature.APP,
                key = AppSettingKeys.PAYMENT_PLAN,
                value = plan.key,
            )
        }
    }
}
