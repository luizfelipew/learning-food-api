package com.works.foodapi.api.controller;

import com.works.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.works.foodapi.domain.model.Cidade;
import com.works.foodapi.domain.repository.CidadeRepository;
import com.works.foodapi.domain.service.CadastroCidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = "/cidades")
@RequiredArgsConstructor
public class CidadeController {

    private final CidadeRepository cidadeRepository;
    private final CadastroCidadeService cadastroCidade;

    @GetMapping
    public List<Cidade> listar() {
        return cidadeRepository.listar();
    }

    @GetMapping("/{cidadeId}")
    public ResponseEntity<Cidade> buscar(@PathVariable Long cidadeId) {
        Cidade cidade = cidadeRepository.buscar(cidadeId);

        if (Objects.nonNull(cidade)) {
            return ResponseEntity.ok(cidade);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
        try {
            cidade = cadastroCidade.salvar(cidade);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(cidade);
        } catch (EntidadeNaoEncontradaException ex) {
            return ResponseEntity.badRequest()
                    .body(ex.getMessage());
        }
    }

    @PutMapping("/{cidadeId}")
    public ResponseEntity<?> atualizar(@PathVariable Long cidadeId,
                                            @RequestBody Cidade cidade) {
        Cidade cidadeAtual = cidadeRepository.buscar(cidadeId);

        if (Objects.nonNull(cidadeAtual)) {
            BeanUtils.copyProperties(cidade, cidadeAtual, "id");
            try {
                cidadeAtual = cadastroCidade.salvar(cidadeAtual);
                return ResponseEntity.ok(cidadeAtual);
            } catch (EntidadeNaoEncontradaException ex) {
                return ResponseEntity.badRequest()
                        .body(ex.getMessage());
            }

        }
        return ResponseEntity.notFound().build();
    }


}
