package com.locadora.presentation.assembler;

import com.locadora.application.dto.response.UsuarioResponse;
import com.locadora.presentation.UsuarioController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

@Component
public class UsuarioModelAssembler implements RepresentationModelAssembler<UsuarioResponse, EntityModel<UsuarioResponse>> {

    @Override
    public @NonNull EntityModel<UsuarioResponse> toModel(@NonNull UsuarioResponse usuario) {
        EntityModel<UsuarioResponse> model = EntityModel.of(usuario);
        if (usuario.getId() != null) {
            model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class).getUsuario(usuario.getId())).withSelfRel());
        }
        model.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioController.class).listar()).withRel("usuarios"));
        return model;
    }
}
