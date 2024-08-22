package com.Inderjeet.News.persistance.services

import com.Inderjeet.News.business.gateway.EmailServices
import com.Inderjeet.News.rest.dto.others.MailBody
import org.slf4j.LoggerFactory
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service


@Service
class EmailServicesImpl(javaMailSender: JavaMailSender): EmailServices {
    private val javaMailSender: JavaMailSender
    var logger = LoggerFactory.getLogger(EmailServicesImpl::class.java)
    init {
        this.javaMailSender = javaMailSender
    }

    override fun sendSimpleMessage(mailBody: MailBody) {
        val simpleMailMessage = SimpleMailMessage()
        simpleMailMessage.setTo(mailBody.to)
        simpleMailMessage.from = "inderjeet.yadav@codefylabs.com"
        simpleMailMessage.subject = mailBody.subject
        simpleMailMessage.text = mailBody.text
        try {
            javaMailSender.send(simpleMailMessage)
        } catch (e: MailException) {
            logger.error("Error occurred while sending mail to ${mailBody.to}: ${e.message}")
            throw RuntimeException("Failed to send mail", e)
        }
    }
}

