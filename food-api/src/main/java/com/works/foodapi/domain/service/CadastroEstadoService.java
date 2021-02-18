package com.works.foodapi.domain.service;

import com.works.foodapi.domain.exception.EntidadeEmUsoException;
import com.works.foodapi.domain.exception.EntidadeNaoEncontradaException;
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

    @Autowired
    private EstadoRepository estadoRepository;

    public Estado salvar(Estado estado) {
        return estadoRepository.salvar(estado);
    }

    public void excluir(Estado estado) {
        try {
            estadoRepository.remover(estado);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntidadeNaoEncontradaException(
                    String.format("Não existe um cadastro de estado com código %d", estado.getId())
            );

        } catch (DataIntegrityViolationException ex) {
            throw new EntidadeEmUsoException(
                    String.format("Estado de código %d não pode ser removida, pois está em uso", estado.getId())
            );
        }
    }
}
