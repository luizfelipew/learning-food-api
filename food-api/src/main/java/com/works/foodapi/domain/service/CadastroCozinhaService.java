package com.works.foodapi.domain.service;

import com.works.foodapi.domain.exception.EntidadeEmUsoException;
import com.works.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.works.foodapi.domain.model.Cozinha;
import com.works.foodapi.domain.repository.CozinhaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CadastroCozinhaService {

    private static final String MSG_COZINHA_NAO_ENCONTRADA = "Não existe um cadastro de cozinha com código %d";
    private static final String MSG_COZINHA_EM_USO = "Cozinha de código %d não pode ser removida, pois está em uso";

    @Autowired
    private CozinhaRepository cozinhaRepository;

    public Cozinha salvar(Cozinha cozinha) {
        return cozinhaRepository.save(cozinha);
    }

    public void excluir(Long cozinhaId) {
        try {
            cozinhaRepository.deleteById(cozinhaId);
        } catch (EmptyResultDataAccessException ex) {
            throw new EntidadeNaoEncontradaException(
                    String.format(MSG_COZINHA_NAO_ENCONTRADA, cozinhaId)
            );

        } catch (DataIntegrityViolationException ex) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_COZINHA_EM_USO, cozinhaId)
            );
        }
    }

    public Cozinha buscarOuFalhar(final Long cozinhaId) {
        return cozinhaRepository
                .findById(cozinhaId)
                .orElseThrow(
                        () -> new EntidadeNaoEncontradaException(
                                String.format(MSG_COZINHA_NAO_ENCONTRADA, cozinhaId)));
    }
}
