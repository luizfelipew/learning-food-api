package com.works.foodapi.api.controller;

import com.works.foodapi.api.assembler.EstadoInputDisassembler;
import com.works.foodapi.api.assembler.EstadoModelAssembler;
import com.works.foodapi.api.model.EstadoModel;
import com.works.foodapi.api.model.input.EstadoInput;
import com.works.foodapi.domain.model.Estado;
import com.works.foodapi.domain.repository.EstadoRepository;
import com.works.foodapi.domain.service.CadastroEstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/estados")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoRepository estadoRepository;
    private final CadastroEstadoService cadastroEstado;
    private final EstadoModelAssembler estadoModelAssembler;
    private final EstadoInputDisassembler estadoInputDisassembler;

    @GetMapping
    public List<EstadoModel> listar() {
        final List<Estado> todosEstados = estadoRepository.findAll();

        return estadoModelAssembler.toCollectionModel(todosEstados);
    }

    @GetMapping("/{estadoId}")
    public EstadoModel buscar(@PathVariable Long estadoId) {
        final Estado estado = cadastroEstado.buscarOuFalhar(estadoId);

        return estadoModelAssembler.toModel(estado);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EstadoModel adicionar(@RequestBody @Valid EstadoInput estadoInput) {
        Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);

        estado = cadastroEstado.salvar(estado);

        return estadoModelAssembler.toModel(estado);
    }

    @PutMapping("/{estadoId}")
    public EstadoModel atualizar(@PathVariable Long estadoId,
                            @RequestBody @Valid EstadoInput estadoInput) {

        Estado estadoAtual = cadastroEstado.buscarOuFalhar(estadoId);

        estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);

        estadoAtual = cadastroEstado.salvar(estadoAtual);

        return estadoModelAssembler.toModel(estadoAtual);
    }

    @DeleteMapping("/{estadoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long estadoId) {
        cadastroEstado.excluir(estadoId);
    }

}
