import { fetchWithAuth, showError } from './utils.js';

document.getElementById('veiculo-form').addEventListener('submit', async (e) => {
    e.preventDefault();

    const veiculo = {
        marca: document.getElementById('marca').value,
        modelo: document.getElementById('modelo').value,
        ano: parseInt(document.getElementById('ano').value),
        placa: document.getElementById('placa').value
    };

    try {
        const response = await fetchWithAuth('/api/veiculos', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(veiculo)
        });

        if (response.ok) {
            window.location.href = '/veiculos/list.html';
        }
    } catch (err) {
        showError(err.message || 'Erro ao salvar veículo');
    }
});

// Logout
document.getElementById('btn-logout').addEventListener('click', async () => {
    await fetch('/api/auth/logout', { method: 'POST' });
    window.location.href = '/index.html';
});