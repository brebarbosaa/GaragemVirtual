// src/main/java/com/exemplo/garagem/filters/AuthFilter.java
package com.exemplo.garagem.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest  req  = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        String context = req.getContextPath();
        // pega a parte após o contextPath; "/" vira ""
        String uri     = req.getRequestURI().substring(context.length());

        // --- WHITELIST DE URLS SEM NECESSIDADE DE SESSÃO ---
        if (uri.equals("") || uri.equals("/") || uri.equals("/index.html")) {
            // raiz do app e index.html
            chain.doFilter(request, response);
            return;
        }
        if (uri.startsWith("/auth/")) {
            // login.html, register.html, etc.
            chain.doFilter(request, response);
            return;
        }
        if (uri.startsWith("/assets/") ||
                uri.matches(".*\\.(css|js|png|jpg|jpeg|gif)$")) {
            // arquivos estáticos
            chain.doFilter(request, response);
            return;
        }
        // liberar login/register via API (POST) e preflight (OPTIONS)
        if (uri.equals("/api/auth/login")  ||
                uri.equals("/api/auth/register")) {
            if ("POST".equalsIgnoreCase(req.getMethod()) ||
                    "OPTIONS".equalsIgnoreCase(req.getMethod())) {
                chain.doFilter(request, response);
                return;
            }
        }

        // --- DEMÁS REQUISIÇÕES EXIGEM SESSÃO VÁLIDA ---
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("usuarioId") != null) {
            chain.doFilter(request, response);
        } else {
            if (uri.startsWith("/api/")) {
                resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            } else {
                resp.sendRedirect(context + "/auth/login.html");
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) { }

    @Override
    public void destroy() { }
}
