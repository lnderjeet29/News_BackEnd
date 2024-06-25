package com.codefylabs.Maple.Leaf.rest.validator

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Constraint(validatedBy = [ImageNameValidation::class])
annotation class ImageNameValid(
    val message: String = "image is not valid...",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)