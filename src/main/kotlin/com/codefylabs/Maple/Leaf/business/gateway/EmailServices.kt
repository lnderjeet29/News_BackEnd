package com.codefylabs.Maple.Leaf.business.gateway

import com.codefylabs.Maple.Leaf.rest.dto.MailBody


interface EmailServices {
    fun sendSimpleMessage(mailBody: MailBody)
}