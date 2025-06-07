document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('registerForm');
    const submitBtn = document.getElementById('submitBtn');
    const messageDiv = document.getElementById('message');

    const errors = {
        nome: document.getElementById('nomeError'),
        email: document.getElementById('emailError'),
        senha: document.getElementById('senhaError'),
        confirmarSenha: document.getElementById('confirmarSenhaError')
    };

    function resetErrors() {
        Object.values(errors).forEach(el => {
            el.textContent = '';
            el.style.display = 'none';
        });
        messageDiv.style.display = 'none';
    }

    function validatePasswords() {
        const senha = document.getElementById('senha').value;
        const confirmar = document.getElementById('confirmarSenha').value;
        if (senha !== confirmar) {
            errors.confirmarSenha.textContent = 'As senhas não coincidem';
            errors.confirmarSenha.style.display = 'block';
            return false;
        }
        if (senha.length < 6) {
            errors.senha.textContent = 'A senha deve ter pelo menos 6 caracteres';
            errors.senha.style.display = 'block';
            return false;
        }
        return true;
    }

    function showMessage(text, isError = true) {
        messageDiv.textContent = text;
        messageDiv.className = isError ? 'message error' : 'message success';
        messageDiv.style.display = 'block';
        if (!isError) {
            setTimeout(() => {
                form.reset();
                messageDiv.style.display = 'none';
                window.location.href = "/auth/login.html";
            }, 3000);
        }
    }

    const registerUrl = `/api/auth/register`;

    form.addEventListener('submit', async e => {
        e.preventDefault();
        resetErrors();
        submitBtn.disabled = true;

        if (!validatePasswords()) {
            submitBtn.disabled = false;
            return;
        }

        const usuario = {
            nome: document.getElementById('nome').value.trim(),
            email: document.getElementById('email').value.trim(),
            senha: document.getElementById('senha').value
        };

        try {
            const resp = await fetch(registerUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(usuario)
            });

            const text = await resp.text();

            if (resp.status === 201) {
                showMessage('Cadastro realizado com sucesso!', false);
            } else if (resp.status === 409) {
                errors.email.textContent = text || 'Email já cadastrado';
                errors.email.style.display = 'block';
            } else {
                showMessage(text || 'Erro no cadastro. Tente novamente.');
            }
        } catch (err) {
            console.error('Erro na requisição:', err);
            showMessage('Erro de conexão com o servidor.');
        } finally {
            submitBtn.disabled = false;
        }
    });

    document.getElementById('confirmarSenha')
        .addEventListener('input', validatePasswords);
});
