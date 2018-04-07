package com.leemin.genealogy.error;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
//    private static Logger logger = LoggerFactory.getLogger(MyAccessDeniedHandler.class);
//
//    @Override
//    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//
//        Authentication auth
//                = SecurityContextHolder.getContext().getAuthentication();
//
//        if (auth != null) {
//            logger.info("User '" + auth.getName()
//                                + "' attempted to access the protected URL: "
//                                + httpServletRequest.getRequestURI());
//        }
//
//        httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/404");
//
//    }
//}
