package com.works.foodapi.api.assembler;

import com.works.foodapi.api.model.FotoProdutoModel;
import com.works.foodapi.domain.model.FotoProduto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FotoProdutoModelAssembler {

    private final ModelMapper modelMapper;

    public FotoProdutoModel toModel(FotoProduto foto) {
        return modelMapper.map(foto, FotoProdutoModel.class);
    }
}
