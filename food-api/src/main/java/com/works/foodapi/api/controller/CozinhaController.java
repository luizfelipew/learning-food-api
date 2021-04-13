package com.works.foodapi.api.controller;

import com.works.foodapi.api.model.CozinhasXmlWrapper;
import com.works.foodapi.domain.exception.EntidadeEmUsoException;
import com.works.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.works.foodapi.domain.model.Cozinha;
import com.works.foodapi.domain.repository.CozinhaRepository;
import com.works.foodapi.domain.service.CadastroCozinhaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/cozinhas", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CozinhaController {

    private final CozinhaRepository cozinhaRepository;
    private final CadastroCozinhaService cadastroCozinha;

    @GetMapping
    public List<Cozinha> listar() {
        return cozinhaRepository.findAll();
    }

    @GetMapping(produces = MediaType.APPLICATION_XML_VALUE)
    public CozinhasXmlWrapper listarXml() {
        return new CozinhasXmlWrapper(cozinhaRepository.findAll());
    }

    @GetMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> buscar(@PathVariable Long cozinhaId) {
        Optional<Cozinha> cozinha = cozinhaRepository.findById(cozinhaId);

        if (cozinha.isPresent()) {
            return ResponseEntity.ok(cozinha.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cozinha adicionar(@RequestBody Cozinha cozinha) {
        return cadastroCozinha.salvar(cozinha);
    }

    @PutMapping("/{cozinhaId}")
    public ResponseEntity<Cozinha> atualizar(@PathVariable Long cozinhaId,
                                             @RequestBody Cozinha cozinha) {
        Optional<Cozinha> cozinhaAtual = cozinhaRepository.findById(cozinhaId);

        if (cozinhaAtual.isPresent()) {
            BeanUtils.copyProperties(cozinha, cozinhaAtual.get(), "id");
            Cozinha conzinhaSalva = cadastroCozinha.salvar(cozinhaAtual.get());
            return ResponseEntity.ok(conzinhaSalva);
        }
        return ResponseEntity.notFound().build();
    }

//    @DeleteMapping("/{cozinhaId}")
//    public ResponseEntity<Cozinha> deletar(@PathVariable Long cozinhaId) {
//        try {
//            cadastroCozinha.excluir(cozinhaId);
//            return ResponseEntity.noContent().build();
//
//        } catch (EntidadeNaoEncontradaException ex) {
//            return ResponseEntity.notFound().build();
//
//        } catch (EntidadeEmUsoException ex) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
//    }

    @DeleteMapping("/{cozinhaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long cozinhaId) {
        cadastroCozinha.excluir(cozinhaId);
    }
}
