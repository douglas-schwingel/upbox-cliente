package br.com.upbox.ftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpSession;
import org.springframework.stereotype.Component;

public class FtpConnectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(FtpConnectionFactory.class);
    private static final Marker marker = MarkerFactory.getMarker("ftpConnectionFactory");

    private String username;
    private String password;

    public FtpConnectionFactory setUpUser(String username, String password) {
        this.username = username;
        this.password = password;
        return this;
    }

    public FtpSession getSession() {
        DefaultFtpSessionFactory sf = new DefaultFtpSessionFactory();
        sf.setHost("localhost");
        sf.setPort(2112);
        sf.setUsername(username);
        sf.setPassword(password);
        return sf.getSession();
    }


}
