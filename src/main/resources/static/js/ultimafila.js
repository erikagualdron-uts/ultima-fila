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
