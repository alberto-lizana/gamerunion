
document.addEventListener('DOMContentLoaded', () => {

    const botonCargar = document.getElementById('cargar-mas');
    const contenedor = document.getElementById('eventos-container');

    if (!botonCargar || !contenedor) {
        return;
    }

    botonCargar.addEventListener('click', async () => {

        const pagina = botonCargar.dataset.nextPage;

        botonCargar.disabled = true;
        botonCargar.textContent = 'Cargando...';

        try {

            const respuesta = await fetch(`/eventos/cargar?page=${pagina}`);

            if (!respuesta.ok) {
                throw new Error('Error al cargar los eventos');
            }

            const html = await respuesta.text();

            const documento = new DOMParser()
                .parseFromString(html, 'text/html');

            const fragmento = documento.querySelector('[data-has-next]');

            if (!fragmento) {
                throw new Error('No se encontró el fragmento de eventos');
            }

            contenedor.insertAdjacentHTML('beforeend', fragmento.innerHTML);

            const hayMasEventos = fragmento.dataset.hasNext === 'true';

            if (hayMasEventos) {
                botonCargar.dataset.nextPage = Number(pagina) + 1;
                botonCargar.disabled = false;
                botonCargar.textContent = 'Cargar más eventos';
            } else {
                botonCargar.parentElement.remove();
            }

        } catch (error) {

            console.error(error);

            botonCargar.disabled = false;
            botonCargar.textContent = 'Reintentar';

        }

    });

});
