package com.exemplo.garagem;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();

        // Configurar parâmetros de contexto
        ctx.setAttribute("appName", "Garagem Virtual");

        // Configurar parâmetros de banco (opcional)
        ctx.setInitParameter("db.url", "jdbc:mysql://localhost:3306/garagem_virtual");
        ctx.setInitParameter("db.user", "root");
        ctx.setInitParameter("db.password", "1234");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Limpeza ao finalizar a aplicação
    }
}