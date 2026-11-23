// Ayudas de las pantallas de administración (la validación real sigue en el servidor)
(function () {
  // Contador de caracteres: <div data-contador="idDelCampo">
  document.querySelectorAll("[data-contador]").forEach(function (salida) {
    var campo = document.getElementById(salida.dataset.contador);
    if (!campo) return;
    function pintar() {
      salida.textContent = campo.value.length + " de " + campo.maxLength;
    }
    campo.addEventListener("input", pintar);
    pintar();
  });

  // Capacidad de la sala en vivo
  var capacidad = document.getElementById("capacidad");
  var filas = document.getElementById("filas");
  var porFila = document.getElementById("sillasPorFila");
  if (capacidad && filas && porFila) {
    function pintarCapacidad() {
      var f = parseInt(filas.value, 10);
      var s = parseInt(porFila.value, 10);
      if (f >= 1 && s >= 1 && f <= Number(filas.max) && s <= Number(porFila.max)) {
        var total = f * s;
        capacidad.textContent = "Tendrá " + total + (total === 1 ? " silla" : " sillas");
      } else {
        capacidad.textContent = "Indica las filas (1 a " + filas.max + ") y las sillas por fila (1 a " + porFila.max + ").";
      }
    }
    filas.addEventListener("input", pintarCapacidad);
    porFila.addEventListener("input", pintarCapacidad);
    pintarCapacidad();
  }

  // Filtros que se aplican solos al cambiar (el botón «Filtrar» sigue sirviendo sin JavaScript)
  document.querySelectorAll("form[data-autoenvio]").forEach(function (formulario) {
    formulario.querySelectorAll("select, input[type='date']").forEach(function (campo) {
      campo.addEventListener("change", function () { formulario.requestSubmit(); });
    });
  });

  // Hora de fin estimada al programar una función
  var pelicula = document.getElementById("idPelicula");
  var hora = document.getElementById("hora");
  var fin = document.getElementById("finEstimado");
  if (pelicula && hora && fin) {
    function pintarFin() {
      var opcion = pelicula.selectedOptions[0];
      var duracion = opcion ? parseInt(opcion.dataset.duracion, 10) : NaN;
      var partes = hora.value.match(/^(\d{2}):(\d{2})/);
      if (!duracion || !partes) {
        fin.textContent = "Elige película y hora para ver cuándo termina.";
        return;
      }
      var total = Number(partes[1]) * 60 + Number(partes[2]) + duracion + 15;
      var siguiente = total >= 1440;
      total = total % 1440;
      var texto = String(Math.floor(total / 60)).padStart(2, "0") + ":" + String(total % 60).padStart(2, "0");
      fin.textContent = "Termina hacia las " + texto + (siguiente ? " del día siguiente" : "") +
        " (incluye 15 min de limpieza)";
    }
    pelicula.addEventListener("change", pintarFin);
    hora.addEventListener("input", pintarFin);
    pintarFin();
  }

  // Miniatura del afiche en la tabla de películas: si no carga, se muestra la inicial sobre el degradado
  function inicialSobreDegradado(img) {
    var respaldo = document.createElement("div");
    respaldo.className = "miniatura-respaldo";
    respaldo.setAttribute("role", "img");
    respaldo.setAttribute("aria-label", "Afiche de " + img.dataset.titulo);
    respaldo.textContent = img.dataset.titulo.charAt(0);
    img.replaceWith(respaldo);
  }
  document.querySelectorAll(".miniatura img[data-titulo]").forEach(function (img) {
    if (img.complete && img.naturalWidth === 0) {
      inicialSobreDegradado(img);
    } else {
      img.addEventListener("error", function () { inicialSobreDegradado(img); });
    }
  });

  // Vista previa del afiche al escribir el enlace
  var campoAfiche = document.getElementById("posterUrl");
  var vistaImagen = document.getElementById("vistaImagen");
  var vistaRespaldo = document.getElementById("vistaRespaldo");
  var estadoVista = document.getElementById("estadoVista");
  if (campoAfiche && vistaImagen && vistaRespaldo && estadoVista) {
    var campoTitulo = document.getElementById("titulo");
    var espera;

    function mostrarRespaldo(mensaje) {
      vistaImagen.hidden = true;
      vistaRespaldo.hidden = false;
      vistaRespaldo.textContent = campoTitulo && campoTitulo.value ? campoTitulo.value.charAt(0) : "?";
      estadoVista.textContent = mensaje;
    }

    vistaImagen.addEventListener("load", function () {
      vistaImagen.hidden = false;
      vistaRespaldo.hidden = true;
      estadoVista.textContent = "Vista previa del afiche cargada.";
    });
    vistaImagen.addEventListener("error", function () {
      mostrarRespaldo("No se pudo cargar el afiche; se mostrará el respaldo.");
    });

    function actualizarVista() {
      var enlace = campoAfiche.value.trim();
      if (!enlace) {
        vistaImagen.removeAttribute("src");
        mostrarRespaldo("");
        return;
      }
      vistaImagen.src = enlace;
    }

    campoAfiche.addEventListener("input", function () {
      clearTimeout(espera);
      espera = setTimeout(actualizarVista, 400);
    });
    if (campoTitulo) {
      campoTitulo.addEventListener("input", function () {
        if (!vistaRespaldo.hidden) vistaRespaldo.textContent = campoTitulo.value.charAt(0) || "?";
      });
    }
    if (campoAfiche.value.trim()) {
      actualizarVista();
    } else {
      mostrarRespaldo("");
    }
  }
})();
