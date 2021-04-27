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

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/cidades")
@RequiredArgsConstructor
public class CidadeController {

    private final CidadeRepository cidadeRepository;
    private final CadastroCidadeService cadastroCidade;

    @GetMapping
    public List<Cidade> listar() {
        return cidadeRepository.findAll();
    }

    @GetMapping("/{cidadeId}")
    public Cidade buscar(@PathVariable Long cidadeId) {
        return cadastroCidade.buscarOuFalhar(cidadeId);
    }

//    @PostMapping
//    public ResponseEntity<?> adicionar(@RequestBody Cidade cidade) {
//        try {
//            cidade = cadastroCidade.salvar(cidade);
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(cidade);
//        } catch (EntidadeNaoEncontradaException ex) {
//            return ResponseEntity.badRequest()
//                    .body(ex.getMessage());
//        }
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cidade adicionar(@RequestBody Cidade cidade) {
        return cadastroCidade.salvar(cidade);
    }

//    @PutMapping("/{cidadeId}")
//    public ResponseEntity<?> atualizar(@PathVariable Long cidadeId,
//                                            @RequestBody Cidade cidade) {
//        Optional<Cidade> cidadeAtual = cidadeRepository.findById(cidadeId);
//
//        if (cidadeAtual.isPresent()) {
//            BeanUtils.copyProperties(cidade, cidadeAtual.get(), "id");
//            try {
//                Cidade cidadeSalva = cadastroCidade.salvar(cidadeAtual.get());
//                return ResponseEntity.ok(cidadeSalva);
//            } catch (EntidadeNaoEncontradaException ex) {
//                return ResponseEntity.badRequest()
//                        .body(ex.getMessage());
//            }
//
//        }
//        return ResponseEntity.notFound().build();
//    }

    @PutMapping("/{cidadeId}")
    public Cidade atualizar (@PathVariable Long cidadeId, @RequestBody Cidade cidade) {
        Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);

        BeanUtils.copyProperties(cidade, cidadeAtual, "id");

        return cadastroCidade.salvar(cidadeAtual);
    }

    @DeleteMapping("/{cidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cidadeId) {
        cadastroCidade.excluir(cidadeId);
    }

}
