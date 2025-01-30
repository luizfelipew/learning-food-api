package com.works.foodapi.domain.service;

import com.works.foodapi.domain.model.FotoProduto;
import com.works.foodapi.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CatalagoFotoProdutoService {

    private final ProdutoRepository produtoRepository;

    @Transactional
    public FotoProduto salvar(final FotoProduto foto) {
        return produtoRepository.save(foto);
    }
}
