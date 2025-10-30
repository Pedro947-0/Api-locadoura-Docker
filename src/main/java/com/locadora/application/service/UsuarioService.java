package com.locadora.application.service;



import com.locadora.application.dto.request.UsuarioLoginRequest;
import com.locadora.application.dto.request.UsuarioRequest;
import com.locadora.application.dto.response.UsuarioResponse;
import com.locadora.domain.entity.Usuario;
import com.locadora.domain.repository.UsuarioRepository;
import com.locadora.domain.repository.EmpresaRepository;
import com.locadora.domain.exception.validadores.CpfValidator;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {
    private static final Logger logger = LoggerFactory.getLogger(UsuarioService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioResponse registrar(UsuarioRequest request) {
        try {
            if (!CpfValidator.isValid(request.getCpf())) {
                logger.warn("Tentativa de registro com CPF inválido: {}", request.getCpf());
                throw new IllegalArgumentException("CPF inválido");
            }
            if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
                logger.warn("Tentativa de registro com email já existente: {}", request.getEmail());
                throw new IllegalArgumentException("Email já cadastrado");
            }

            String role = request.getRole() != null ? request.getRole().toUpperCase() : "USER";

            Usuario usuario = new Usuario();
            usuario.setNome(request.getNome());
            usuario.setEmail(request.getEmail());
            usuario.setSenha(passwordEncoder.encode(request.getSenha()));
            usuario.setCpf(request.getCpf());
            usuario.setRole(role);

            // associar empresa padrão se informado
            if (request.getDefaultEmpresaId() != null) {
                empresaRepository.findById(request.getDefaultEmpresaId()).ifPresent(usuario::setDefaultEmpresa);
            }

            Usuario salvo = usuarioRepository.save(usuario);
            logger.info("Usuário registrado com sucesso: {}", salvo.getEmail());
            Long empresaId = salvo.getDefaultEmpresa() != null ? salvo.getDefaultEmpresa().getId() : null;
            return new UsuarioResponse(salvo.getId(), salvo.getNome(), salvo.getEmail(), salvo.getCpf(), empresaId);
        } catch (IllegalArgumentException e) {
            logger.error("Erro de validação ao registrar usuário: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao registrar usuário: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao registrar usuário");
        }
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        try {
            return usuarioRepository.findByEmail(email);
        } catch (Exception e) {
            logger.error("Erro ao buscar usuário por email: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<Usuario> buscarPorEmailECpf(String email, String cpf) {
        try {
            return usuarioRepository.findByEmailAndCpf(email, cpf);
        } catch (Exception e) {
            logger.error("Erro ao buscar usuário por email e CPF: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    public boolean validarCredenciais(UsuarioLoginRequest request) {
        try {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmailAndCpf(request.getEmail(), request.getCpf());
            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                boolean senhaValida = passwordEncoder.matches(request.getSenha(), usuario.getSenha());
                if (!senhaValida) {
                    logger.warn("Senha inválida para o email: {}", request.getEmail());
                    return false;
                }

                // se o cliente informou uma empresa no payload de login, verificar se o usuário pertence a ela
                if (request.getDefaultEmpresaId() != null) {
                    if (usuario.getDefaultEmpresa() == null) {
                        logger.warn("Usuário não possui empresa padrão, mas login solicitou empresa id={}", request.getDefaultEmpresaId());
                        return false; // negar login se empresa for exigida
                    }
                    if (!request.getDefaultEmpresaId().equals(usuario.getDefaultEmpresa().getId())) {
                        logger.warn("Empresa do usuário ({}) não confere com valor enviado no login ({})", usuario.getDefaultEmpresa().getId(), request.getDefaultEmpresaId());
                        return false;
                    }
                }

                return true;
            } else {
                logger.warn("Tentativa de login com email/cpf não encontrados: {} / {}", request.getEmail(), request.getCpf());
            }
            return false;
        } catch (Exception e) {
            logger.error("Erro ao validar credenciais: {}", e.getMessage(), e);
            return false;
        }
    }

    public UsuarioResponse toResponse(Usuario usuario) {
        Long empresaId = usuario.getDefaultEmpresa() != null ? usuario.getDefaultEmpresa().getId() : null;
        return new UsuarioResponse(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getCpf(), empresaId);
    }

    // Novo método para listar todos os usuários (para exibir no Swagger)
    public List<UsuarioResponse> listarTodos() {
        try {
            List<Usuario> usuarios = usuarioRepository.findAll();
            logger.info("Listando todos os usuários. Total: {}", usuarios.size());
            return usuarios.stream()
                    .map(this::toResponse)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Erro ao listar usuários: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao listar usuários");
        }
    }

    public boolean excluirUsuario(Long id) {
        try {
            var usuarioBanco = usuarioRepository.findByIdAndStatusNot(id, com.locadora.domain.enums.StatusUsuario.EXCLUIDO)
                    .orElse(null);
            if (usuarioBanco == null) {
                return false;
            }
            usuarioBanco.setStatus(com.locadora.domain.enums.StatusUsuario.EXCLUIDO);
            usuarioRepository.save(usuarioBanco);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao excluir usuário: " + e.getMessage());
            return false;
        }
    }

    public boolean bloquearUsuario(Long id) {
        try {
            var usuarioBanco = usuarioRepository.findByIdAndStatusNot(id, com.locadora.domain.enums.StatusUsuario.EXCLUIDO)
                    .orElse(null);
            if (usuarioBanco == null) {
                return false;
            }
            usuarioBanco.setStatus(com.locadora.domain.enums.StatusUsuario.BLOQUEADO);
            usuarioRepository.save(usuarioBanco);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao bloquear usuário: " + e.getMessage());
            return false;
        }
    }

    public boolean desbloquearUsuario(Long id) {
        try {
            var usuarioBanco = usuarioRepository.findByIdAndStatusNot(id, com.locadora.domain.enums.StatusUsuario.EXCLUIDO)
                    .orElse(null);
            if (usuarioBanco == null) {
                return false;
            }
            usuarioBanco.setStatus(com.locadora.domain.enums.StatusUsuario.ATIVO);
            usuarioRepository.save(usuarioBanco);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao desbloquear usuário: " + e.getMessage());
            return false;
        }
    }

    public boolean atualizarUsuario(Long id, com.locadora.application.dto.request.UsuarioCriarRequestDto usuarioRequest) {
        try {
            var usuarioBanco = usuarioRepository.findByIdAndStatusNot(id, com.locadora.domain.enums.StatusUsuario.EXCLUIDO)
                    .orElse(null);
            if (usuarioBanco == null) {
                return false;
            }
            usuarioBanco.atualizarUsuarioFromDTO(usuarioRequest);
            usuarioRepository.save(usuarioBanco);
            return true;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar usuário: " + e.getMessage());
            return false;
        }
    }

}