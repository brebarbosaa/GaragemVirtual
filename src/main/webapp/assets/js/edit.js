// edit.js
import { fetchWithAuth, showError } from './utils.js';

document.addEventListener('DOMContentLoaded', async () => {
    const params = new URLSearchParams(window.location.search);
    const veiculoId = params.get('id');
    if (!veiculoId) {
        showError('ID do veículo não fornecido.');
        return;
    }

    try {
        const response = await fetchWithAuth(`/api/veiculos/${veiculoId}`, {
            method: 'GET'
        });
        if (!response.ok) {
            showError('Erro ao carregar dados do veículo.');
            return;
        }
        const veiculo = await response.json();
        document.getElementById('marca').value = veiculo.marca;
        document.getElementById('modelo').value = veiculo.modelo;
        document.getElementById('ano').value = veiculo.ano;
        document.getElementById('placa').value = veiculo.placa;
    } catch (error) {
        showError('Erro ao carregar dados do veículo.');
        console.error(error);
    }
});

document.getElementById('veiculo-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    const params = new URLSearchParams(window.location.search);
    const veiculoId = params.get('id');

    const veiculo = {
        marca: document.getElementById('marca').value,
        modelo: document.getElementById('modelo').value,
        ano: parseInt(document.getElementById('ano').value, 10),
        placa: document.getElementById('placa').value
    };

    try {
        const response = await fetchWithAuth(`/api/veiculos/${veiculoId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(veiculo)
        });
        if (response.ok) {
            window.location.href = '/veiculos/list.html';
        } else {
            showError('Erro ao atualizar veículo.');
        }
    } catch (error) {
        showError(error.message || 'Erro ao atualizar veículo');
        console.error(error);
    }
});

// Logout
document.getElementById('btn-logout').addEventListener('click', async () => {
    await fetch('/api/auth/logout', { method: 'POST' });
    window.location.href = '/index.html';
});

