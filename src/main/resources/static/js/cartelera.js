// Filtros de la cartelera: género y fecha se aplican solos; el botón «Filtrar» sigue sirviendo sin JavaScript
(function () {
  var formulario = document.querySelector('form[role="search"]');
  var zona = document.getElementById("resultados");
  if (!formulario || !zona) return;

  var enviando = false;

  function esqueletos() {
    var fila = document.createElement("div");
    fila.className = "row g-4 mt-1";
    for (var i = 0; i < 4; i++) {
      var columna = document.createElement("div");
      columna.className = "col-12 col-sm-6 col-lg-3";
      var bloque = document.createElement("div");
      bloque.className = "esqueleto esqueleto-peli";
      columna.appendChild(bloque);
      fila.appendChild(columna);
    }
    return fila;
  }

  function aplicar() {
    if (enviando) return;
    enviando = true;
    zona.classList.add("saliendo");
    setTimeout(function () {
      zona.replaceChildren(esqueletos());
      zona.setAttribute("aria-busy", "true");
      zona.classList.remove("saliendo");
      formulario.submit();
    }, 150);
  }

  ["genero", "fecha"].forEach(function (id) {
    var campo = document.getElementById(id);
    if (campo) campo.addEventListener("change", aplicar);
  });

  // Al volver con «Atrás» se recarga para no dejar el esqueleto a la vista
  window.addEventListener("pageshow", function (evento) {
    if (evento.persisted) window.location.reload();
  });
})();
