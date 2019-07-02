package br.com.upbox.controller;

import br.com.upbox.ftp.FtpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;

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
                logger.info(marker, "Arquivo inserido");
            }
        } catch (IOException e) {
            logger.error(marker, "Erro ao enviar o arquivo: {}", arquivo.getOriginalFilename());
            e.printStackTrace();
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
        File file = new File(arquivoTemporario);
        view.addObject("arquivo", file);
        view.addObject("nome", nomeArquivo);
        return view;
    }



}
