package com.works.foodapi.api.controller;

import com.works.foodapi.domain.exception.EntidadeEmUsoException;
import com.works.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.works.foodapi.domain.model.Estado;
import com.works.foodapi.domain.repository.EstadoRepository;
import com.works.foodapi.domain.service.CadastroEstadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/estados")
@RequiredArgsConstructor
public class EstadoController {

    private final EstadoRepository estadoRepository;
    private final CadastroEstadoService cadastroEstado;

    @GetMapping
    public List<Estado> listar() {
        return estadoRepository.listar();
    }

    @GetMapping("/{estadoId}")
    public ResponseEntity<Estado> buscar(@PathVariable Long estadoId) {
        Estado estado = estadoRepository.buscar(estadoId);

        if (Objects.nonNull(estado)) {
            return ResponseEntity.ok(estado);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estado adicionar(@RequestBody Estado estado) {
        return estadoRepository.salvar(estado);
    }

    @PutMapping("/{estadoId}")
    public ResponseEntity<Estado> atualizar(@PathVariable Long estadoId,
                                            @RequestBody Estado estado) {
        Estado estadoAtual = estadoRepository.buscar(estadoId);

        if (Objects.nonNull(estadoAtual)) {
            BeanUtils.copyProperties(estado, estadoAtual, "id");
            estadoAtual = cadastroEstado.salvar(estadoAtual);
            return ResponseEntity.ok(estadoAtual);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{estadoId}")
    public ResponseEntity<Estado> deletar(@PathVariable Long estadoId) {
        try {
            Estado estado = estadoRepository.buscar(estadoId);
            if (Objects.nonNull(estado)) {
                cadastroEstado.excluir(estado);
            }
            return ResponseEntity.notFound().build();

        } catch (EntidadeNaoEncontradaException ex) {
            return ResponseEntity.notFound().build();

        } catch (EntidadeEmUsoException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
