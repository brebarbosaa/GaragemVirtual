import { fetchWithAuth, showError } from './utils.js';

document.addEventListener('DOMContentLoaded', async () => {
    try {
        const response = await fetchWithAuth('/api/veiculos');
        const veiculos = await response.json();

        const container = document.getElementById('veiculos-container');
        veiculos.forEach(veiculo => {
            const card = document.createElement('div');
            card.className = 'card';
            card.innerHTML = `
                <h3>${veiculo.marca} ${veiculo.modelo}</h3>
                <p>Ano: ${veiculo.ano}</p>
                <p>Placa: ${veiculo.placa}</p>
                <div class="actions">
                    <a href="/veiculos/edit.html?id=${veiculo.id}" class="btn">Editar</a>
                    <button class="btn-delete" data-id="${veiculo.id}">Excluir</button>
                </div>
            `;
            container.appendChild(card);
        });

        // Evento de logout
        document.getElementById('btn-logout').addEventListener('click', async () => {
            await fetch('/api/auth/logout', { method: 'POST' });
            window.location.href = '/index.html';
        });


        // Eventos de deleção
        document.querySelectorAll('.btn-delete').forEach(btn => {
            btn.addEventListener('click', async (e) => {
                if (confirm('Tem certeza que deseja excluir este veículo?')) {
                    try {
                        await fetchWithAuth(`/api/veiculos/${e.target.dataset.id}`, {
                            method: 'DELETE'
                        });
                        e.target.closest('.card').remove();
                    } catch (err) {
                        showError('Erro ao excluir veículo');
                    }
                }
            });
        });

    } catch (err) {
        if (err.status === 401) {
            window.location.href = '/auth/login.html';
        } else {
            showError('Erro ao carregar veículos');
        }
    }
});