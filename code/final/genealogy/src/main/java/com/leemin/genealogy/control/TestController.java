package com.leemin.genealogy.control;

import com.leemin.genealogy.email.EmailService;
import com.leemin.genealogy.email.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {
    private static Logger log = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/test/locale", method = RequestMethod.GET)
    public String getLocalePage() {
        return "/test/my-locale";
    }

    @RequestMapping(value = "/test/greeting", method = RequestMethod.GET)
    public String greeting() {
        return "/greeting";
    }

    @RequestMapping(value = "/test/send-email", method = RequestMethod.GET)
    public String email() {

        log.info("Spring Mail - Sending Simple Email with JavaMailSender Example");

        Mail mail = new Mail();
        mail.setFrom("no-reply@memorynotfound.com");
        mail.setTo("lemin2601@gmail.com");
        mail.setSubject("Sending Simple Email with JavaMailSender Example");
        mail.setContent("This tutorial demonstrates how to send a simple email using Spring Framework.");

        emailService.sendSimpleMessage(mail);
        return "/greeting";

    }

    @RequestMapping(value = "/test/upload", method = RequestMethod.GET)
    public String upload() {

        return "/test/upload";

    }


    @RequestMapping(value = "/test/send-email-async", method = RequestMethod.GET)
    public String emailAsync() {

        log.info("Spring Mail - Sending Simple Email with JavaMailSender Example");

        Mail mail = new Mail();
        mail.setFrom("no-reply@memorynotfound.com");
        mail.setTo("lemin2601@gmail.com");
        mail.setSubject("Sending Simple Email with JavaMailSender Example");
        mail.setContent("This tutorial demonstrates how to send a simple email using Spring Framework.");

        emailService.sendMailAsync(mail);
        return "/greeting";

    }

}
