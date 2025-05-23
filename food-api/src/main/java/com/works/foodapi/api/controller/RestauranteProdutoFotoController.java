package com.works.foodapi.api.controller;

import com.works.foodapi.api.assembler.FotoProdutoModelAssembler;
import com.works.foodapi.api.model.FotoProdutoModel;
import com.works.foodapi.api.model.input.FotoProdutoInput;
import com.works.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.works.foodapi.domain.model.FotoProduto;
import com.works.foodapi.domain.service.CadastroProdutoService;
import com.works.foodapi.domain.service.CatalogoFotoProdutoService;
import com.works.foodapi.domain.service.FotoStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

    private final CadastroProdutoService cadastroProdutoService;
    private final CatalogoFotoProdutoService catalogoFotoProdutoService;
    private final FotoStorageService fotoStorageService;
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

        val fotoSalva = catalogoFotoProdutoService.salvar(foto, arquivo.getInputStream());

        return fotoProdutoModelAssembler.toModel(fotoSalva);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public FotoProdutoModel buscar(@PathVariable Long restauranteId,
                                   @PathVariable Long produtoId) {
        log.info("Buscando foto do produto {} do restaurante {}", produtoId, restauranteId);
        FotoProduto fotoProduto = catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);

        return fotoProdutoModelAssembler.toModel(fotoProduto);
    }

    @GetMapping
    public ResponseEntity<?> servirFoto(@PathVariable Long restauranteId,
                                                          @PathVariable Long produtoId,
                                                          @RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {
        try {
            val fotoProduto = catalogoFotoProdutoService.buscarOuFalhar(restauranteId, produtoId);

            MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());

            List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);

            verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);

            val fotoRecuperada = fotoStorageService.recuperar(fotoProduto.getNomeArquivo());

            if (fotoRecuperada.temUrl()) {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
                        .build();
            } else {
                return ResponseEntity.ok()
                        .contentType(mediaTypeFoto)
                        .body(new InputStreamResource(fotoRecuperada.getInputStream()));
            }

        } catch (EntidadeNaoEncontradaException ex) {
            return ResponseEntity
                    .notFound()
                    .build();
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long restauranteId,
                        @PathVariable Long produtoId) {
        catalogoFotoProdutoService.excluir(restauranteId, produtoId);
    }

    private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {

        boolean compativel = mediaTypesAceitas.stream()
                .anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));

        if (!compativel) {
            throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
        }
    }
}
