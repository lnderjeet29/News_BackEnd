package com.codefylabs.Maple.Leaf.rest.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.slf4j.LoggerFactory


class ImageNameValidation : ConstraintValidator<ImageNameValid, String> {
    private val logger= LoggerFactory.getLogger(ImageNameValidation::class.java)

    override fun isValid(s: String, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        logger.info("your valid image name: {}", s)
        return s.isNotEmpty()
    }
}