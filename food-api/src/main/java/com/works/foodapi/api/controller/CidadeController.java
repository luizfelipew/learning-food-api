package com.works.foodapi.api.controller;

import com.works.foodapi.api.assembler.CidadeInputDisassembler;
import com.works.foodapi.api.assembler.CidadeModelAssembler;
import com.works.foodapi.api.model.CidadeModel;
import com.works.foodapi.api.model.input.CidadeInput;
import com.works.foodapi.domain.exception.EstadoNaoEncontradoException;
import com.works.foodapi.domain.exception.NegocioException;
import com.works.foodapi.domain.model.Cidade;
import com.works.foodapi.domain.repository.CidadeRepository;
import com.works.foodapi.domain.service.CadastroCidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/cidades")
@RequiredArgsConstructor
public class CidadeController {

    private final CidadeRepository cidadeRepository;
    private final CadastroCidadeService cadastroCidade;
    private final CidadeModelAssembler cidadeModelAssembler;
    private final CidadeInputDisassembler cidadeInputDisassembler;

    @GetMapping
    public List<CidadeModel> listar() {
        final List<Cidade> todasCidades = cidadeRepository.findAll();
        return cidadeModelAssembler.toCollectionModel(todasCidades);
    }

    @GetMapping("/{cidadeId}")
    public CidadeModel buscar(@PathVariable Long cidadeId) {
        final Cidade cidade = cadastroCidade.buscarOuFalhar(cidadeId);
        return cidadeModelAssembler.toModel(cidade);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CidadeModel adicionar(@RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);

            cidade = cadastroCidade.salvar(cidade);

            return cidadeModelAssembler.toModel(cidade);
        } catch (EstadoNaoEncontradoException ex) {
            throw new NegocioException(ex.getMessage(), ex);
        }
    }

    @PutMapping("/{cidadeId}")
    public CidadeModel atualizar(@PathVariable Long cidadeId, @RequestBody @Valid CidadeInput cidadeInput) {

        try {
            Cidade cidadeAtual = cadastroCidade.buscarOuFalhar(cidadeId);

            cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);

            cidadeAtual = cadastroCidade.salvar(cidadeAtual);

            return cidadeModelAssembler.toModel(cidadeAtual);
        } catch (EstadoNaoEncontradoException ex) {
            throw new NegocioException(ex.getMessage(), ex);
        }
    }

    @DeleteMapping("/{cidadeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long cidadeId) {
        cadastroCidade.excluir(cidadeId);
    }

}
