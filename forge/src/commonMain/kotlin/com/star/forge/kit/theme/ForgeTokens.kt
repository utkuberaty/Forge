package com.star.forge.kit.theme

import androidx.compose.runtime.Immutable

@Immutable
public data class ForgeTokens(
    public val colors: ForgeColors = ForgeColorSchemes.light(),
    public val spacing: ForgeSpacing = ForgeSpacing(),
    public val radii: ForgeRadii = ForgeRadii(),
    public val borders: ForgeBorders = ForgeBorders(),
    public val typography: ForgeTypography = ForgeTypography.default,
    public val opacity: ForgeOpacity = ForgeOpacity(),
    public val motion: ForgeMotion = ForgeMotion(),
    public val elevation: ForgeElevation = ForgeElevation(),
    public val touchTargets: ForgeTouchTargets = ForgeTouchTargets(),
    public val components: ForgeComponentTokens = ForgeComponentTokens(),
)

@Immutable
public data class ForgeTokenSet(
    public val light: ForgeTokens,
    public val dark: ForgeTokens,
)

public object ForgeTokenSets {
    public fun light(): ForgeTokens = ForgeTokens(colors = ForgeColorSchemes.light())

    public fun dark(): ForgeTokens = ForgeTokens(colors = ForgeColorSchemes.dark())

    public fun default(): ForgeTokenSet = ForgeTokenSet(light = light(), dark = dark())
}
