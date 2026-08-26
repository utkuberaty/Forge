package com.star.forgekitdemo.showcase.model

data class ShowcaseSection(
    val id: ShowcaseSectionId,
    val title: String,
    val description: String,
)

enum class ShowcaseSectionId { Actions, Inputs, Selection, Feedback }

data class ShowcaseConfiguration(
    val dark: Boolean,
    val personalized: Boolean,
    val rtl: Boolean,
)
