package com.codefylabs.Maple.Leaf.rest.dto.user

data class ConfigDto (
    val isUserBlocked:Boolean,
    val isUserEnabled: Boolean,
    val isOnboardingSurveyUploaded: Boolean
)