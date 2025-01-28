package com.works.foodapi.api.controller;

import com.works.foodapi.api.model.input.FotoProdutoInput;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void atualizarFoto(@PathVariable Long restauranteId,
                              @PathVariable Long produtoId,
                              FotoProdutoInput fotoProdutoInput){
        val nomeArquivo = UUID.randomUUID()
                + "_" + fotoProdutoInput.getArquivo().getOriginalFilename();

        val arquivoFoto = Path.of("/Users/luizfelipew/Documents/git/spring-boot/alga-works/catalogo", nomeArquivo);

        log.info(String.valueOf(fotoProdutoInput.getDescricao()));
        log.info(String.valueOf(arquivoFoto));
        log.info(fotoProdutoInput.getArquivo().getContentType());

        try {
            fotoProdutoInput.getArquivo().transferTo(arquivoFoto);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
