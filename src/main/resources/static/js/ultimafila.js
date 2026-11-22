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

  // Los avisos se cierran solos a los 3,5 s
  document.querySelectorAll(".aviso-uf").forEach(function (aviso) {
    setTimeout(function () {
      aviso.classList.add("saliendo");
      setTimeout(function () { aviso.remove(); }, 300);
    }, 3500);
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
