package com.works.foodapi.api.controller;

import com.google.common.collect.ImmutableMap;
import com.works.foodapi.api.assembler.PedidoInputDisassembler;
import com.works.foodapi.api.assembler.PedidoModelAssembler;
import com.works.foodapi.api.assembler.PedidoResumoModelAssembler;
import com.works.foodapi.api.model.PedidoModel;
import com.works.foodapi.api.model.PedidoResumoModel;
import com.works.foodapi.api.model.input.PedidoInput;
import com.works.foodapi.core.data.PageableTranslator;
import com.works.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.works.foodapi.domain.exception.NegocioException;
import com.works.foodapi.domain.model.Pedido;
import com.works.foodapi.domain.model.Usuario;
import com.works.foodapi.domain.repository.PedidoRepository;
import com.works.foodapi.domain.repository.filter.PedidoFilter;
import com.works.foodapi.domain.service.EmissaoPedidoService;
import com.works.foodapi.infrastructure.repository.spec.PedidoSpecs;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/pedidos")
public class PedidoController {

    private final PedidoRepository pedidoRepository;

    private final EmissaoPedidoService emissaoPedido;

    private final PedidoModelAssembler pedidoModelAssembler;

    private final PedidoResumoModelAssembler pedidoResumoModelAssembler;

    private final PedidoInputDisassembler pedidoInputDisassembler;

//    @GetMapping
//    public MappingJacksonValue listar(@RequestParam(required = false) final String campos) {
//        val pedidos = pedidoRepository.findAll();
//        val pedidosModel = pedidoResumoModelAssembler.toCollectionModel(pedidos);
//
//        val pedidosWrapper = new MappingJacksonValue(pedidosModel);
//
//        val filterProvider = new SimpleFilterProvider();
//        filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());
//
//        if (StringUtils.isNotBlank(campos)) {
//            filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter
//                    .filterOutAllExcept(campos.split(",")));
//        }
//
//        pedidosWrapper.setFilters(filterProvider);
//
//        return pedidosWrapper;
//    }

    @GetMapping
    public Page<PedidoResumoModel> pesquisar(final PedidoFilter filtro,
                                             @PageableDefault Pageable pageable) {
        pageable  = traduzirPageable(pageable);

        Page<Pedido> pedidosPage = pedidoRepository
                .findAll(PedidoSpecs.usandoFiltro(filtro), pageable);

        List<PedidoResumoModel> pedidosResumoModel = pedidoResumoModelAssembler
                .toCollectionModel(pedidosPage.getContent());

        PageImpl<PedidoResumoModel> pedidosResumoModelPage = new PageImpl<>(pedidosResumoModel, pageable, pedidosPage.getTotalElements());

        return pedidosResumoModelPage;
    }

    @GetMapping("/{codigoPedido}")
    public PedidoModel buscar(@PathVariable final String codigoPedido) {
        Pedido pedido = emissaoPedido.buscarOuFalhar(codigoPedido);

        return pedidoModelAssembler.toModel(pedido);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@RequestBody @Valid PedidoInput pedidoInput) {
        try {
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

            // TODO pegar usuário autenticado
            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(1L);

            novoPedido = emissaoPedido.emitir(novoPedido);

            return pedidoModelAssembler.toModel(novoPedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    private Pageable traduzirPageable(Pageable apiPageable) {
        val mapeamento = ImmutableMap.of(
                "codigo", "codigo",
                "restaurante.nome", "restaurante.nome",
                "nomeCliente", "cliente.nome",
                "valorTotal", "valorTotal"
        );

        return PageableTranslator.translate(apiPageable, mapeamento);
    }
}
