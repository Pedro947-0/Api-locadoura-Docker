package com.locadora.presentation.assembler;

import com.locadora.application.dto.response.UsuarioResponse;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.*;

class UsuarioModelAssemblerTest {

    @Test
    void toModel_addsLinksWhenIdPresent() {
        UsuarioModelAssembler assembler = new UsuarioModelAssembler();
        UsuarioResponse u = new UsuarioResponse(1L, "Nome", "email@example.com", "12345678909", null);
        EntityModel<UsuarioResponse> model = assembler.toModel(u);
        assertNotNull(model);
        assertTrue(model.getLink("self").isPresent(), "self link should be present when id is set");
        assertTrue(model.getLink("usuarios").isPresent(), "usuarios link should be present");
    }

    @Test
    void toModel_handlesNullId_noSelfLink() {
        UsuarioModelAssembler assembler = new UsuarioModelAssembler();
        UsuarioResponse u = new UsuarioResponse(null, "Nome", "email@example.com", "12345678909", null);
        EntityModel<UsuarioResponse> model = assembler.toModel(u);
        assertNotNull(model);
        assertFalse(model.getLink("self").isPresent(), "self link should NOT be present when id is null");
        assertTrue(model.getLink("usuarios").isPresent(), "usuarios link should be present");
    }
}

