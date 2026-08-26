package com.star.forge.kit.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable public data class ForgeButtonSize(
    public val visualHeight: Dp,
    public val horizontalPadding: Dp,
    public val verticalPadding: Dp,
    public val iconSize: Dp,
)

@Immutable public data class ForgeIconButtonSize(
    public val visualSize: Dp,
    public val iconSize: Dp,
)

@Immutable public data class ForgeSymbolSize(
    public val visualSize: Dp,
    public val iconSize: Dp,
)

@Immutable public data class ForgeCheckboxSize(
    public val visualSize: Dp,
    public val cornerRadius: Dp,
    public val strokeWidth: Dp,
)

@Immutable public data class ForgeSwitchSize(
    public val width: Dp,
    public val height: Dp,
    public val thumbSize: Dp,
    public val thumbPadding: Dp,
)

@Immutable public data class ForgeSliderSize(
    public val visualHeight: Dp,
    public val trackHeight: Dp,
    public val thumbRadius: Dp,
)

@Immutable public data class ForgeRadioButtonSize(
    public val visualSize: Dp,
    public val dotSize: Dp,
    public val strokeWidth: Dp,
)

@Immutable public data class ForgeProgressSize(
    public val visualSize: Dp,
    public val strokeWidth: Dp,
    public val iconSize: Dp,
)

/** Optional component-level visuals. Null values continue to inherit semantic/foundation tokens. */
@Immutable
public data class ForgeComponentVisualOverrides(
    public val container: Color? = null,
    public val content: Color? = null,
    public val selectedContainer: Color? = null,
    public val selectedContent: Color? = null,
    public val unselectedContainer: Color? = null,
    public val unselectedContent: Color? = null,
    public val disabledContainer: Color? = null,
    public val disabledContent: Color? = null,
    public val border: Color? = null,
    public val pressedOpacity: Float? = null,
    public val cornerRadius: Dp? = null,
    public val motionDurationMillis: Int? = null,
) {
    init {
        require(pressedOpacity == null || (pressedOpacity.isFinite() && pressedOpacity in 0f..1f)) {
            "component pressed opacity must be null or a finite value from 0 to 1"
        }
        require(cornerRadius == null || cornerRadius.value >= 0f) { "component corner radius must be nonnegative" }
        require(motionDurationMillis == null || motionDurationMillis >= 0) { "component motion duration must be nonnegative" }
    }
}

@Immutable
public data class ForgeButtonTokens(
    public val small: ForgeButtonSize = ForgeButtonSize(36.dp, 12.dp, 6.dp, 16.dp),
    public val medium: ForgeButtonSize = ForgeButtonSize(44.dp, 16.dp, 8.dp, 18.dp),
    public val large: ForgeButtonSize = ForgeButtonSize(52.dp, 20.dp, 10.dp, 20.dp),
    public val minimumTouchTarget: Dp? = null,
    public val cornerRadius: Dp? = null,
    public val pressedOpacity: Float? = null,
    public val visuals: ForgeComponentVisualOverrides = ForgeComponentVisualOverrides(),
)

@Immutable
public data class ForgeIconButtonTokens(
    public val small: ForgeIconButtonSize = ForgeIconButtonSize(36.dp, 16.dp),
    public val medium: ForgeIconButtonSize = ForgeIconButtonSize(44.dp, 20.dp),
    public val large: ForgeIconButtonSize = ForgeIconButtonSize(52.dp, 24.dp),
    public val minimumTouchTarget: Dp? = null,
    public val cornerRadius: Dp? = null,
    public val visuals: ForgeComponentVisualOverrides = ForgeComponentVisualOverrides(),
)

@Immutable
public data class ForgeSymbolTokens(
    public val icon: ForgeSymbolSize = ForgeSymbolSize(18.dp, 18.dp),
    public val small: ForgeSymbolSize = ForgeSymbolSize(28.dp, 16.dp),
    public val medium: ForgeSymbolSize = ForgeSymbolSize(36.dp, 18.dp),
    public val large: ForgeSymbolSize = ForgeSymbolSize(44.dp, 22.dp),
    public val minimumTouchTarget: Dp? = null,
    public val visuals: ForgeComponentVisualOverrides = ForgeComponentVisualOverrides(),
)

@Immutable
public data class ForgeCheckboxTokens(
    public val small: ForgeCheckboxSize = ForgeCheckboxSize(18.dp, 5.dp, 2.dp),
    public val medium: ForgeCheckboxSize = ForgeCheckboxSize(22.dp, 6.dp, 2.dp),
    public val large: ForgeCheckboxSize = ForgeCheckboxSize(26.dp, 7.dp, 2.5.dp),
    public val minimumTouchTarget: Dp? = null,
    public val visuals: ForgeComponentVisualOverrides = ForgeComponentVisualOverrides(),
)

@Immutable
public data class ForgeSwitchTokens(
    public val small: ForgeSwitchSize = ForgeSwitchSize(42.dp, 24.dp, 18.dp, 3.dp),
    public val medium: ForgeSwitchSize = ForgeSwitchSize(52.dp, 30.dp, 22.dp, 4.dp),
    public val large: ForgeSwitchSize = ForgeSwitchSize(62.dp, 36.dp, 28.dp, 4.dp),
    public val minimumTouchTarget: Dp? = null,
    public val visuals: ForgeComponentVisualOverrides = ForgeComponentVisualOverrides(),
)

@Immutable
public data class ForgeSliderTokens(
    public val small: ForgeSliderSize = ForgeSliderSize(32.dp, 5.dp, 8.dp),
    public val medium: ForgeSliderSize = ForgeSliderSize(40.dp, 6.dp, 10.dp),
    public val large: ForgeSliderSize = ForgeSliderSize(48.dp, 8.dp, 12.dp),
    public val minimumTouchTarget: Dp? = null,
    public val visuals: ForgeComponentVisualOverrides = ForgeComponentVisualOverrides(),
)

@Immutable
public data class ForgeTextFieldTokens(
    public val minimumHeight: Dp = 56.dp,
    public val iconSize: Dp = 18.dp,
    public val inputRowMinimumHeight: Dp = 24.dp,
    public val horizontalPadding: Dp? = null,
    public val verticalPadding: Dp? = null,
    public val cornerRadius: Dp? = null,
    public val helperColor: Color? = null,
    public val checkingColor: Color? = null,
    public val validColor: Color? = null,
    public val invalidColor: Color? = null,
    public val invalidContainer: Color? = null,
    public val visuals: ForgeComponentVisualOverrides = ForgeComponentVisualOverrides(),
)

@Immutable
public data class ForgeRadioButtonTokens(
    public val small: ForgeRadioButtonSize = ForgeRadioButtonSize(18.dp, 8.dp, 2.dp),
    public val medium: ForgeRadioButtonSize = ForgeRadioButtonSize(22.dp, 10.dp, 2.dp),
    public val large: ForgeRadioButtonSize = ForgeRadioButtonSize(26.dp, 12.dp, 2.5.dp),
    public val minimumTouchTarget: Dp? = null,
    public val visuals: ForgeComponentVisualOverrides = ForgeComponentVisualOverrides(),
)

@Immutable
public data class ForgeProgressTokens(
    public val small: ForgeProgressSize = ForgeProgressSize(24.dp, 2.dp, 12.dp),
    public val medium: ForgeProgressSize = ForgeProgressSize(32.dp, 3.dp, 16.dp),
    public val large: ForgeProgressSize = ForgeProgressSize(48.dp, 4.dp, 22.dp),
    public val visuals: ForgeComponentVisualOverrides = ForgeComponentVisualOverrides(),
)

@Immutable
public data class ForgeSelectionRowTokens(
    public val minimumHeight: Dp = 48.dp,
    public val horizontalPadding: Dp? = null,
    public val verticalPadding: Dp? = null,
    public val controlGap: Dp? = null,
    public val visuals: ForgeComponentVisualOverrides = ForgeComponentVisualOverrides(),
)

@Immutable
public data class ForgeSegmentedControlTokens(
    public val minimumHeight: Dp = 44.dp,
    public val contentPadding: Dp? = null,
    public val itemGap: Dp? = null,
    public val itemHorizontalPadding: Dp? = null,
    public val selectedContainer: Color? = null,
    public val selectedContent: Color? = null,
    public val unselectedContainer: Color? = null,
    public val unselectedContent: Color? = null,
    public val visuals: ForgeComponentVisualOverrides = ForgeComponentVisualOverrides(),
)

@Immutable
public data class ForgeComponentTokens(
    public val button: ForgeButtonTokens = ForgeButtonTokens(),
    public val iconButton: ForgeIconButtonTokens = ForgeIconButtonTokens(),
    public val symbol: ForgeSymbolTokens = ForgeSymbolTokens(),
    public val checkbox: ForgeCheckboxTokens = ForgeCheckboxTokens(),
    public val switch: ForgeSwitchTokens = ForgeSwitchTokens(),
    public val slider: ForgeSliderTokens = ForgeSliderTokens(),
    public val textField: ForgeTextFieldTokens = ForgeTextFieldTokens(),
    public val radioButton: ForgeRadioButtonTokens = ForgeRadioButtonTokens(),
    public val progress: ForgeProgressTokens = ForgeProgressTokens(),
    public val selectionRow: ForgeSelectionRowTokens = ForgeSelectionRowTokens(),
    public val segmentedControl: ForgeSegmentedControlTokens = ForgeSegmentedControlTokens(),
)
