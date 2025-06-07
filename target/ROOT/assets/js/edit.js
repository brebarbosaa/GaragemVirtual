document.addEventListener("DOMContentLoaded", async () => {
    const urlParams = new URLSearchParams(window.location.search);
    const veiculoId = urlParams.get("id");

    const form = document.getElementById("edit-form");
    const marcaInput = document.getElementById("marca");
    const modeloInput = document.getElementById("modelo");
    const anoInput = document.getElementById("ano");
    const placaInput = document.getElementById("placa");

    if (!veiculoId) {
        alert("Veículo não especificado.");
        return;
    }

    // Carregar dados do veículo
    try {
        const response = await fetch(`http://localhost:3000/veiculos/${veiculoId}`);
        const veiculo = await response.json();

        marcaInput.value = veiculo.marca;
        modeloInput.value = veiculo.modelo;
        anoInput.value = veiculo.ano;
        placaInput.value = veiculo.placa;
    } catch (error) {
        console.error("Erro ao carregar veículo:", error);
    }

    // Enviar atualização
    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const dadosAtualizados = {
            marca: marcaInput.value,
            modelo: modeloInput.value,
            ano: parseInt(anoInput.value),
            placa: placaInput.value
        };

        try {
            const response = await fetch(`http://localhost:3000/veiculos/${veiculoId}`, {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(dadosAtualizados)
            });

            if (response.ok) {
                alert("Veículo atualizado com sucesso!");
                window.location.href = "list.html";
            } else {
                alert("Erro ao atualizar veículo.");
            }
        } catch (error) {
            console.error("Erro ao enviar atualização:", error);
        }
    });
});
