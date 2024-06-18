package com.codefylabs.CodyfylabsSimpleSecurity.business.gateway

import com.codefylabs.Maple.Leaf.rest.dto.MailBody


interface EmailServices {
    fun sendSimpleMessage(mailBody: MailBody)
}