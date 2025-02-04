package com.works.foodapi.api.controller;

import com.works.foodapi.api.assembler.FotoProdutoModelAssembler;
import com.works.foodapi.api.model.FotoProdutoModel;
import com.works.foodapi.api.model.input.FotoProdutoInput;
import com.works.foodapi.domain.model.FotoProduto;
import com.works.foodapi.domain.service.CadastroProdutoService;
import com.works.foodapi.domain.service.CatalagoFotoProdutoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

    private final CadastroProdutoService cadastroProdutoService;
    private final CatalagoFotoProdutoService catalagoFotoProdutoService;
    private final FotoProdutoModelAssembler fotoProdutoModelAssembler;

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId,
                                          @PathVariable Long produtoId,
                                          @Valid FotoProdutoInput fotoProdutoInput) throws IOException {

        val produto = cadastroProdutoService.buscarOuFalhar(restauranteId, produtoId);
        val arquivo = fotoProdutoInput.getArquivo();

        FotoProduto foto = new FotoProduto();
        foto.setProduto(produto);
        foto.setDescricao(fotoProdutoInput.getDescricao());
        foto.setContentType(arquivo.getContentType());
        foto.setTamanho(arquivo.getSize());
        foto.setNomeArquivo(arquivo.getOriginalFilename());

        val fotoSalva = catalagoFotoProdutoService.salvar(foto, arquivo.getInputStream());

        return fotoProdutoModelAssembler.toModel(fotoSalva);
    }
}
