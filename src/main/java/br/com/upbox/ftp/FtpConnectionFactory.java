package br.com.upbox.ftp;

import org.springframework.integration.ftp.session.DefaultFtpSessionFactory;
import org.springframework.integration.ftp.session.FtpSession;

public class FtpConnectionFactory {

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
