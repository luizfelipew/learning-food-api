package com.works.foodapi.domain.service;

import com.works.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.works.foodapi.domain.model.Cidade;
import com.works.foodapi.domain.model.Estado;
import com.works.foodapi.domain.repository.CidadeRepository;
import com.works.foodapi.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CadastroCidadeService {

    private static final String MSG_CIDADE_NAO_ENCONTRADO = "Não existe cadastro de estado com o código %d";

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    public Cidade salvar(Cidade cidade) {
        Long estadoId = cidade.getEstado().getId();
        Optional<Estado> estado = estadoRepository.findById(estadoId);

        if (estado.isEmpty()) {
            throw new EntidadeNaoEncontradaException(
                    String.format(MSG_CIDADE_NAO_ENCONTRADO, estadoId)
            );
        }

        cidade.setEstado(estado.get());
        return cidadeRepository.save(cidade);
    }

    public Cidade buscarOuFalhar(final Long cidadeId) {
        return cidadeRepository
                .findById(cidadeId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        String.format(MSG_CIDADE_NAO_ENCONTRADO, cidadeId)));
    }

}
