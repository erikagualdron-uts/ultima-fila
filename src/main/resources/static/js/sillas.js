// Selección de sillas: contador, límite de 6 y total
(function () {
  var MAXIMO = 6;
  var mapa = document.getElementById("mapa");
  if (!mapa) return;

  var precio = Number(mapa.dataset.precio);
  var elegidas = [];
  var contador = document.getElementById("contador");
  var lista = document.getElementById("lista-sillas");
  var total = document.getElementById("total");
  var aviso = document.getElementById("aviso-limite");

  function dinero(valor) {
    return "$ " + valor.toLocaleString("es-CO");
  }

  function pintar() {
    contador.textContent = elegidas.length;
    total.textContent = dinero(precio * elegidas.length);
    lista.innerHTML = "";
    elegidas.forEach(function (boton) {
      var item = document.createElement("li");
      item.textContent = "Silla " + boton.dataset.etiqueta;
      lista.appendChild(item);
    });
  }

  mapa.addEventListener("click", function (evento) {
    var boton = evento.target.closest("button.silla");
    if (!boton || boton.disabled) return;

    var indice = elegidas.indexOf(boton);
    if (indice >= 0) {
      elegidas.splice(indice, 1);
    } else if (elegidas.length >= MAXIMO) {
      aviso.textContent = "Máximo 6 boletas por compra";
      return;
    } else {
      elegidas.push(boton);
    }
    aviso.textContent = "";

    var elegida = indice < 0;
    boton.classList.toggle("elegida", elegida);
    boton.setAttribute("aria-pressed", elegida);
    boton.setAttribute("aria-label", "Silla " + boton.dataset.etiqueta + ", " + (elegida ? "elegida" : "libre"));
    pintar();
  });
})();
