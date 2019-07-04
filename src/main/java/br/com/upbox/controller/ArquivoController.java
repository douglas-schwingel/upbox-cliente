package br.com.upbox.controller;

import br.com.upbox.utils.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ArquivoController {
    private static final Logger logger = LoggerFactory.getLogger(ArquivoController.class);
    private static final Marker marker = MarkerFactory.getMarker("arquivoController");

    @PostMapping("/envia_arquivo")
    public String enviaArquivos(@RequestParam("arquivo") MultipartFile arquivo,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password) {
        try {
            boolean sucesso = FtpUtil.storeFiles(arquivo, username, password);
            if (sucesso) {
                logger.info(marker, "Arquivo {} inserido", arquivo.getName());
            }
        } catch (IOException e) {
            logger.error(marker, "Erro ao enviar o arquivo: {}", arquivo.getOriginalFilename());
        }
        return "redirect:/usuario/" + username;
    }

    @PostMapping("/deleta_arquivo")
    public String deletaArquivo(@RequestParam("nomeArquivo") String nomeArquivo,
                                @RequestParam("username") String username,
                                @RequestParam("password") String password) {
        boolean sucesso = FtpUtil.removeFile(nomeArquivo, username, password);
        if (sucesso) {
            logger.info(marker, "Arquivo {} de {} deleteado com sucesso", nomeArquivo, username);
        } else {
            logger.error(marker, "Arquivo {} de {} não pôde ser deletado", nomeArquivo, username);
        }
        return "redirect:/usuario/" + username;
    }

    @PostMapping("/download")
    public ModelAndView baixarArquivo(@RequestParam("nomeArquivo") String nomeArquivo,
                              @RequestParam("username") String username,
                              @RequestParam("password") String password) {
        String arquivoTemporario = FtpUtil.baixaArquivo(nomeArquivo, username, password);
        ModelAndView view = new ModelAndView("download");
        view.addObject("arquivo", arquivoTemporario);
        view.addObject("nome", nomeArquivo);
        return view;
    }

    @PostMapping("/arquivo")
    public void downloadArquivo(HttpServletResponse response, @RequestParam("arquivo") String nomeArquivo) {
        String caminho = System.getProperty("user.dir") + "/temp";
        Path filePath = Paths.get(caminho, nomeArquivo);
        if (Files.exists(filePath)) {
            response.addHeader("Content-Disposition", "attachment; filename=" + nomeArquivo);
            try {
                response.getOutputStream().flush();
                Files.delete(filePath);
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
        }
    }

}
