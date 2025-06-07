package com.exemplo.garagem.controllers;

import com.exemplo.garagem.dao.VeiculoDAO;
import com.exemplo.garagem.models.Veiculo;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(
        name = "AuthStatusSrever",
        urlPatterns = {"/api/auth/status"},
        loadOnStartup = 1
)
public class AuthStatusServlet extends HttpServlet {

    private final VeiculoDAO veiculoDAO = new VeiculoDAO();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("usuarioId") != null) {
            int usuarioId = (Integer) session.getAttribute("usuarioId");

            try {
                // Supondo que um usuário possa ter mais de um veículo
                List<Veiculo> veiculos = veiculoDAO.listarPorUsuario(usuarioId);

                resp.setContentType("application/json");
                resp.getWriter().write(gson.toJson(veiculos));
                return;

            } catch (SQLException e) {
                e.printStackTrace();
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
        }

        resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
