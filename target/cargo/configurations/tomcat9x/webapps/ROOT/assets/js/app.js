// Verificar status de autenticação
async function checkAuthStatus() {
    try {
        const response = await fetch('/api/auth/status');
        if (response.ok) {
            const user = await response.json();
            showUserInfo(user);
        }
    } catch (error) {
        console.error('Erro ao verificar autenticação:', error);
    }
}

// Exibir informações do usuário
function showUserInfo(user) {
    const authDiv = document.getElementById('auth-buttons');
    authDiv.innerHTML = `
        <span>Olá, ${user.nome}!</span>
        <a href="/veiculos/list.html" class="btn">Meus Veículos</a>
        <button id="logout-btn" class="btn">Sair</button>
    `;

    document.getElementById('logout-btn').addEventListener('click', logout);
}

// Função de logout
async function logout() {
    await fetch('/api/auth/logout', { method: 'POST' });
    window.location.reload();
}

// Inicializar
document.addEventListener('DOMContentLoaded', checkAuthStatus);