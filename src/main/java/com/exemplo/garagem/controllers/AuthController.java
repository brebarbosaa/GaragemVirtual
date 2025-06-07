package com.exemplo.garagem.controllers;

import com.exemplo.garagem.dao.UsuarioDAO;
import com.exemplo.garagem.models.Usuario;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;

@WebServlet(
        name = "AuthController",
        urlPatterns = {"/api/auth/login", "/api/auth/logout", "/api/auth/register"},
        loadOnStartup = 1
)
public class AuthController extends HttpServlet {
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getRequestURI();
        resp.setContentType("application/json;charset=UTF-8");
        resp.setHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        resp.setHeader("Access-Control-Max-Age", "3600");
        resp.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept");

        if (path.endsWith("/login")) {
            processarLogin(req, resp);
        } else if (path.endsWith("/register")) {
            processarRegistro(req, resp);
        } else if (path.endsWith("/logout")) {
            processarLogout(req, resp);
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(gson.toJson(
                    Collections.singletonMap("message", "Recurso não encontrado")
            ));
        }
    }

    private void processarRegistro(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // le todo o corpo como JSON
        StringBuilder buffer = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            buffer.append(line);
        }
        Usuario novoUsuario = gson.fromJson(buffer.toString(), Usuario.class);

        try {
            if (usuarioDAO.emailExiste(novoUsuario.getEmail())) {
                // Retorna 409 + JSON de erro
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                Map<String,String> body = Collections.singletonMap("message", "Email já cadastrado");
                resp.getWriter().write(gson.toJson(body));
                return;
            }

            // aqui você aplicaria hash na senha antes de persistir
            // novoUsuario.setSenha(BCrypt.hashpw(novoUsuario.getSenha(), BCrypt.gensalt()));

            usuarioDAO.criarUsuario(novoUsuario);

            // Retorna 201 + JSON de sucesso (opcionalmente devolvendo o usuário sem senha)
            resp.setStatus(HttpServletResponse.SC_CREATED);
            Map<String,String> success = Collections.singletonMap("message", "Cadastro realizado com sucesso");
            resp.getWriter().write(gson.toJson(success));

        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String,String> err = Collections.singletonMap("message", "Erro interno no servidor");
            resp.getWriter().write(gson.toJson(err));
        }
    }

    private void processarLogout(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    // Classe interna para representar credenciais
    private static class Credenciais {
        public String email;
        public String senha;
    }

    private void processarLogin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // o contentType e CORS já foram configurados no doPost

        // lê todo o corpo da requisição
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = req.getReader().readLine()) != null) {
            sb.append(line);
        }

        // desserializa direto para Usuario
        Usuario creds = gson.fromJson(sb.toString(), Usuario.class);
        String email = creds.getEmail();
        String senha = creds.getSenha();

        try {
            // tenta autenticar
            Usuario usuario = usuarioDAO.autenticar(email, senha);
            if (usuario != null) {
                // cria sessão
                HttpSession session = req.getSession();
                session.setAttribute("usuarioId", usuario.getId());

                // remove senha antes de serializar o retorno
                usuario.setSenha(null);

                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(gson.toJson(usuario));
            } else {
                // 401 + JSON de erro
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                Map<String, String> err = Collections.singletonMap(
                        "message", "Email ou senha inválidos"
                );
                resp.getWriter().write(gson.toJson(err));
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            Map<String, String> err = Collections.singletonMap(
                    "message", "Erro interno no servidor"
            );
            resp.getWriter().write(gson.toJson(err));
        }
    }

}
