package com.star.forgekitdemo.showcase.registry

import com.star.forgekitdemo.showcase.model.ShowcaseSection
import com.star.forgekitdemo.showcase.model.ShowcaseSectionId

object ForgeShowcaseRegistry {
    val sections: List<ShowcaseSection> =
        listOf(
            ShowcaseSection(ShowcaseSectionId.Actions, "Actions", "Button sizes, loading, disabled, and icon-only actions."),
            ShowcaseSection(ShowcaseSectionId.Inputs, "Inputs", "Fields and sliders with validation, stepping, long content, and RTL."),
            ShowcaseSection(ShowcaseSectionId.Selection, "Selection", "Checkbox, radio, whole-row selection, switch, and segments."),
            ShowcaseSection(ShowcaseSectionId.Feedback, "Feedback", "Determinate and indeterminate progress across editable sizes."),
        )
}
