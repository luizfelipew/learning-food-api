package com.works.foodapi.domain.service;

import com.works.foodapi.domain.exception.ProdutoNaoEncontradoException;
import com.works.foodapi.domain.model.Produto;
import com.works.foodapi.domain.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CadastroProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public Produto salvar(final Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto buscarOuFalhar(final Long restauranteId, final Long produtoId) {
        return produtoRepository.findById(restauranteId, produtoId)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(restauranteId, produtoId));
    }
}
