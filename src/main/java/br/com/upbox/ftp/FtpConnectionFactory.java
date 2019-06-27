package br.com.upbox.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.io.IOException;

public class FtpConnectionFactory {
    private static final Logger logger = LoggerFactory.getLogger(FtpConnectionFactory.class);
    private static final Marker marker = MarkerFactory.getMarker("ftpConnectionFactory");

    private FTPClient ftpClient;

    public FTPClient conecta(String username, String password, String workDir) throws IOException {
        ftpClient = new FTPClient();
        ftpClient.connect("localhost", 2112);
        ftpClient.login(username, password);
        ftpClient.changeWorkingDirectory(workDir);
        return ftpClient;
    }

    public void disconecta() throws IOException {
        if (ftpClient.isConnected()) {
            ftpClient.disconnect();
        }
        System.out.println("Não conectado");
    }
}
