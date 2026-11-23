// Navbar compacta al bajar más de 40 px
(function () {
  var navbar = document.querySelector(".navbar-uf");
  if (navbar) {
    var actualizar = function () {
      navbar.classList.toggle("compacta", window.scrollY > 40);
    };
    window.addEventListener("scroll", actualizar, { passive: true });
    actualizar();
  }

  function cerrarAviso(aviso) {
    aviso.classList.add("saliendo");
    setTimeout(function () { aviso.remove(); }, 300);
  }

  // Los avisos de éxito se cierran solos a los 3,5 s; los de error se quedan hasta pulsar ×
  document.querySelectorAll(".aviso-uf").forEach(function (aviso) {
    if (!aviso.classList.contains("error")) {
      setTimeout(function () { cerrarAviso(aviso); }, 3500);
      return;
    }
    var boton = document.createElement("button");
    boton.type = "button";
    boton.className = "aviso-cerrar";
    boton.setAttribute("aria-label", "Cerrar aviso");
    boton.textContent = "\u00d7";
    boton.addEventListener("click", function () { cerrarAviso(aviso); });
    aviso.appendChild(boton);
  });

  // Formularios que no deben enviarse dos veces (data-envio-unico="Texto mientras procesa")
  document.querySelectorAll("form[data-envio-unico]").forEach(function (formulario) {
    var boton = formulario.querySelector('button[type="submit"]');
    if (!boton) return;
    var textoOriginal = boton.textContent;
    formulario.addEventListener("submit", function (evento) {
      if (boton.disabled) {
        evento.preventDefault();
        return;
      }
      boton.disabled = true;
      boton.textContent = formulario.dataset.envioUnico;
    });
    // Al volver con «Atrás» el botón vuelve a estar disponible
    window.addEventListener("pageshow", function (evento) {
      if (evento.persisted) {
        boton.disabled = false;
        boton.textContent = textoOriginal;
      }
    });
  });

  // Botones «Imprimir»
  document.querySelectorAll("[data-imprimir]").forEach(function (boton) {
    boton.addEventListener("click", function () { window.print(); });
  });
})();

// Si un afiche no carga, se muestra el degradado con el título
document.querySelectorAll(".afiche img").forEach(function (img) {
  img.addEventListener("error", function () {
    var respaldo = document.createElement("div");
    respaldo.className = "afiche-respaldo";
    respaldo.setAttribute("role", "img");
    respaldo.setAttribute("aria-label", "Afiche de " + img.dataset.titulo);
    respaldo.textContent = img.dataset.titulo;
    img.replaceWith(respaldo);
  });
});
