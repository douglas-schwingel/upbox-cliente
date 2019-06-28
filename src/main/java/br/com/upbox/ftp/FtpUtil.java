package br.com.upbox.ftp;

import br.com.upbox.models.Usuario;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class FtpUtil {
    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);
    private static final Marker marker = MarkerFactory.getMarker("ftpUtil");

    private static FtpConnectionFactory fsf;

    private static FTPClient getFtpClient(String username, String password) {
        try {
            fsf = new FtpConnectionFactory();
            return fsf.setUpUser(username, password).getSession().getClientInstance();
        } catch (IllegalStateException e) {
            logger.error(marker, "Erro ao logar: {}", e.getMessage());
        }
        return null;
    }

    public static List<FTPFile> getArquivos(Usuario usuario) {
        logger.info(marker, "tranzendo arquivos para {} com senha {}", usuario.getUsername(), usuario.getSenha());
        FTPClient ftpClient = getFtpClient(usuario.getUsername(), usuario.getSenha());
        List<FTPFile> listaDeArquivos = null;
        if (ftpClient != null && listaDeArquivos == null) {
            try {
                FTPFile[] ftpFiles = ftpClient.listFiles();
                listaDeArquivos = (ftpClient != null) ? Arrays.asList(ftpFiles) : null;
                desconecta(ftpClient);
            } catch (IOException e) {
                logger.error(marker, "Erro ao listar arquivos");
            }
        }
        return listaDeArquivos;
    }

    public static boolean storeFiles(MultipartFile arquivo, String username, String password) throws IOException {
        FTPClient ftpClient = getFtpClient(username, password);
        InputStream inputStream = arquivo.getInputStream();
        return ftpClient.storeFile(arquivo.getOriginalFilename(), inputStream);

    }

    private static void desconecta(FTPClient ftpClient) throws IOException {
        ftpClient.logout();
        ftpClient.disconnect();
    }

    public static void removeUsuario(Usuario usuario) {
        FTPClient ftpClient = getFtpClient(usuario.getUsername() + ".delete", usuario.getSenha());
        if (ftpClient != null) {
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getPathname(String nomeArquivo, String owner) {
        return System.getProperty("user.home") + "/" + owner + "/" + nomeArquivo;
    }

    private static String getPathname(String owner) {
        return System.getProperty("user.home") + "/upbox-files/" + owner;
    }

}
