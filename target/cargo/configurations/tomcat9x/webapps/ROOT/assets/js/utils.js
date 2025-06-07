// Helper para requisições autenticadas
export async function fetchWithAuth(url, options = {}) {
    const response = await fetch(url, {
        ...options,
        credentials: 'include' // Para enviar cookies
    });

    if (response.status === 401) {
        window.location.href = '/auth/login.html';
        throw new Error('Não autenticado');
    }

    if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Erro na requisição');
    }

    return response;
}

// Exibir mensagens de erro
export function showError(message) {
    const errorDiv = document.createElement('div');
    errorDiv.className = 'error-message';
    errorDiv.textContent = message;

    document.body.prepend(errorDiv);
    setTimeout(() => errorDiv.remove(), 5000);
}

// Verificar autenticação ao carregar a página
export function checkAuth() {
    if (!document.cookie.includes('JSESSIONID')) {
        window.location.href = '/auth/login.html';
    }
}