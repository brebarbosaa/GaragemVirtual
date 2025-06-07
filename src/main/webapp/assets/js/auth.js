import { showError } from './utils.js';

document.getElementById('login-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const email = document.getElementById('email').value;
    const senha = document.getElementById('senha').value;

    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email, senha })
        });

        if (response.ok) {
            window.location.href = '/veiculos/list.html';
        } else {
            const error = await response.json();
            showError(error.message || 'Credenciais inválidas');
        }
    } catch (err) {
        showError('Erro na conexão com o servidor');
    }
});
