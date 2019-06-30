package br.com.upbox.ftp;

import br.com.upbox.models.Arquivo;
import br.com.upbox.models.ArquivoVazio;
import br.com.upbox.models.Usuario;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FtpUtil {
    private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);
    private static final Marker marker = MarkerFactory.getMarker("ftpUtil");

    private FtpUtil() {
    }


    private static FTPClient getFtpClient(String username, String password) {
        try {
            FtpConnectionFactory fsf = new FtpConnectionFactory();
            return fsf.setUpUser(username, password).getSession().getClientInstance();
        } catch (IllegalStateException e) {
            logger.error(marker, "Erro ao logar: {}", e.getMessage());
        }
        return null;
    }

    public static List<Arquivo> getArquivos(Usuario usuario) {
        FTPClient ftpClient = getFtpClient(usuario.getUsername(), usuario.getSenha());
        List<Arquivo> listaDeArquivos = new ArrayList<>();
        if (ftpClient != null) {
            logger.info(marker, "tranzendo arquivos para {} com senha {}", usuario.getUsername(), usuario.getSenha());
            try {
                FTPFile[] ftpFiles = ftpClient.listFiles();
                if (ftpFiles[0] == null) {
                    listaDeArquivos.add(new ArquivoVazio());
                    return listaDeArquivos;
                } else {
                    for (FTPFile ftpFile : ftpFiles) {
                        Arquivo arquivo = new Arquivo(ftpFile);
                        listaDeArquivos.add(arquivo);
                        logger.info(marker, "Adicionando arquivo {} a lista", arquivo.getNome());
                    }
                }
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


}
