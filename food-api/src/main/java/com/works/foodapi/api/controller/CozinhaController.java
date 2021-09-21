package com.works.foodapi.api.controller;

import com.works.foodapi.api.assembler.CozinhaInputDisassembler;
import com.works.foodapi.api.assembler.CozinhaModelAssembler;
import com.works.foodapi.api.model.CozinhaModel;
import com.works.foodapi.api.model.input.CozinhaInput;
import com.works.foodapi.domain.model.Cozinha;
import com.works.foodapi.domain.repository.CozinhaRepository;
import com.works.foodapi.domain.service.CadastroCozinhaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CozinhaController {

    private final CozinhaRepository cozinhaRepository;
    private final CadastroCozinhaService cadastroCozinha;
    private final CozinhaModelAssembler cozinhaModelAssembler;
    private final CozinhaInputDisassembler cozinhaInputDisassembler;

    public List<CozinhaModel> listar() {
        final List<Cozinha> todasCozinhas = cozinhaRepository.findAll();

        return cozinhaModelAssembler.toCollectionModel(todasCozinhas);
    }


//    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
//    public CozinhasXmlWrapper listarXml() {
//        return new CozinhasXmlWrapper(cozinhaRepository.findAll());
//    }

    @GetMapping("/{cozinhaId}")
    public CozinhaModel buscar(@PathVariable Long cozinhaId) {
        final Cozinha cozinha = cadastroCozinha.buscarOuFalhar(cozinhaId);

//        Com ResponseEntity
//        return cozinha.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

        return cozinhaModelAssembler.toModel(cozinha);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CozinhaModel adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
        Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
        cozinha = cadastroCozinha.salvar(cozinha);

        return cozinhaModelAssembler.toModel(cozinha);
    }

    @PutMapping("/{cozinhaId}")
    public CozinhaModel atualizar(@PathVariable Long cozinhaId,
                                  @RequestBody @Valid CozinhaInput cozinhaInput) {
        Cozinha cozinhaAtual = cadastroCozinha.buscarOuFalhar(cozinhaId);
        cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
        cozinhaAtual = cadastroCozinha.salvar(cozinhaAtual);

        return cozinhaModelAssembler.toModel(cozinhaAtual);
    }


    @DeleteMapping("/{cozinhaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long cozinhaId) {
        cadastroCozinha.excluir(cozinhaId);
    }
}
