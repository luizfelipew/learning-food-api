package com.works.foodapi.domain.service;

import com.works.foodapi.domain.exception.EntidadeEmUsoException;
import com.works.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.works.foodapi.domain.exception.EstadoNaoEncontradoException;
import com.works.foodapi.domain.model.Cozinha;
import com.works.foodapi.domain.model.Estado;
import com.works.foodapi.domain.repository.CozinhaRepository;
import com.works.foodapi.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class CadastroEstadoService {

    private static final String MSG_ESTADO_EM_USO = "Estado de código %d não pode ser removida, pois está em uso";

    @Autowired
    private EstadoRepository estadoRepository;

    public Estado salvar(Estado estado) {
        return estadoRepository.save(estado);
    }

    public void excluir(Long estadoId) {
        try {
            estadoRepository.deleteById(estadoId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EstadoNaoEncontradoException(estadoId);

        } catch (DataIntegrityViolationException ex) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_ESTADO_EM_USO, estadoId)
            );
        }
    }

    public Estado buscarOuFalhar(final Long estadoId) {
        return estadoRepository
                .findById(estadoId)
                .orElseThrow(() -> new EstadoNaoEncontradoException(estadoId));
    }
}
