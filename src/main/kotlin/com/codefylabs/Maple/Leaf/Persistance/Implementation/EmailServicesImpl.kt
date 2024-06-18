package com.codefylabs.Maple.Leaf.persistance.Implementation

import com.codefylabs.CodyfylabsSimpleSecurity.business.gateway.EmailServices
import com.codefylabs.Maple.Leaf.rest.controller.UserController
import com.codefylabs.Maple.Leaf.rest.dto.MailBody
import org.slf4j.LoggerFactory
import org.springframework.mail.MailException
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service


@Service
class EmailServicesImpl(javaMailSender: JavaMailSender):EmailServices {
    private val javaMailSender: JavaMailSender
    var logger = LoggerFactory.getLogger(EmailServicesImpl::class.java)
    init {
        this.javaMailSender = javaMailSender
    }

    override fun sendSimpleMessage(mailBody: MailBody) {
        logger.info("${mailBody.text} ${mailBody.subject}")
        val simpleMailMessage = SimpleMailMessage()
        simpleMailMessage.setTo(mailBody.to)
        simpleMailMessage.from = "inderjeet.yadav@codefylabs.com"
        simpleMailMessage.subject = mailBody.subject
        simpleMailMessage.text = mailBody.text
        try {
            javaMailSender.send(simpleMailMessage)
            logger.info("Mail sent successfully to ${mailBody.to}...")
        } catch (e: MailException) {
            logger.error("Error occurred while sending mail to ${mailBody.to}: ${e.message}")
            throw RuntimeException("Failed to send mail", e)
        }
    }
}

