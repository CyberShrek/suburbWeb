package org.vniizht.suburbsweb.service;

import com.vniizht.ucheck.UserCheckRemote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.vniizht.suburbsweb.exception.UserCheckException;

import javax.naming.InitialContext;
import javax.servlet.http.HttpServletRequest;

@Service
public class UserCheck {
    private static final String EJB_NAME = "java:global/UCheck-1.0/UserCheck!com.vniizht.ucheck.UserCheckRemote";

    @Value("${code}")
    private String code;

    public UserCheckRemote checkRequest(HttpServletRequest request) throws UserCheckException {
        try {
            UserCheckRemote uCheck = (UserCheckRemote) new InitialContext().lookup(EJB_NAME);

            uCheck.setUser(request.getRemoteUser());
            uCheck.setTaskCode(code);
            uCheck.setStatTaskCode(code);
            uCheck.setIp(request.getHeader("X-Real-IP") != null ? request.getHeader("X-Real-IP")
                    : request.getHeader("X-Forwarded-For") != null ? request.getHeader("X-Forwarded-For")
                    : request.getRemoteAddr());

            if (!uCheck.check()) {
                throw new Exception("Доступ запрещён");
            }

            System.out.println(uCheck.getParamI("create"));
            System.out.println(uCheck.getParamI("read"));
            System.out.println(uCheck.getParamI("update"));
            System.out.println(uCheck.getParamI("delete"));
            System.out.println(uCheck.getUserRole());
            return uCheck;
        } catch (Exception ex) {
            throw new UserCheckException(ex.getMessage());
        }
    }
}