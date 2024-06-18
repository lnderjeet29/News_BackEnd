package com.codefylabs.Maple.Leaf.rest.dto;

import com.codefylabs.Maple.Leaf.persistance.User

data class ApiUserMessage(
    var message: String,
    var status: Boolean,
    var response: User?
)
