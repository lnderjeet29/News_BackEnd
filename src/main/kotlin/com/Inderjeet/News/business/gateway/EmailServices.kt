package com.Inderjeet.News.business.gateway

import com.Inderjeet.News.rest.dto.others.MailBody


interface EmailServices {
    fun sendSimpleMessage(mailBody: MailBody)
}