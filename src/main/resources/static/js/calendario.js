// Calendario de la cartelera: panel propio, sin librerías. Los días con funciones llevan a /cartelera?fecha=
(function () {
  var raiz = document.getElementById("calendario");
  var formulario = document.querySelector('form[role="search"]');
  if (!raiz || !formulario) return;

  var boton = document.getElementById("btn-calendario");
  var panel = document.getElementById("panel-calendario");
  var conFunciones = raiz.dataset.dias ? raiz.dataset.dias.split(",") : [];
  var hoy = raiz.dataset.hoy;
  var elegida = raiz.dataset.fecha || "";
  var MESES = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
  var DIAS = ["Lu", "Ma", "Mi", "Ju", "Vi", "Sa", "Do"];

  var base = (elegida || hoy).split("-");
  var anio = Number(base[0]);
  var mes = Number(base[1]) - 1;
  var mesInicial = Number(hoy.split("-")[0]) * 12 + Number(hoy.split("-")[1]) - 1;

  function iso(a, m, d) {
    return a + "-" + String(m + 1).padStart(2, "0") + "-" + String(d).padStart(2, "0");
  }

  function ir(fecha) {
    var url = new URL(formulario.action, window.location.href);
    ["q", "genero"].forEach(function (nombre) {
      var campo = formulario.elements[nombre];
      if (campo && campo.value) url.searchParams.set(nombre, campo.value);
    });
    if (fecha) url.searchParams.set("fecha", fecha);
    window.location.href = url.toString();
  }

  function el(etiqueta, clase, texto) {
    var nodo = document.createElement(etiqueta);
    if (clase) nodo.className = clase;
    if (texto) nodo.textContent = texto;
    return nodo;
  }

  function pintar(enfocar) {
    panel.replaceChildren();

    var cabecera = el("div", "cal-cabecera");
    var anterior = el("button", "cal-flecha", "‹");
    anterior.type = "button";
    anterior.setAttribute("aria-label", "Mes anterior");
    anterior.disabled = anio * 12 + mes <= mesInicial;
    anterior.addEventListener("click", function () { cambiarMes(-1, ".cal-flecha:first-child"); });
    var titulo = el("div", "cal-mes", MESES[mes] + " " + anio);
    titulo.setAttribute("aria-live", "polite");
    var siguiente = el("button", "cal-flecha", "›");
    siguiente.type = "button";
    siguiente.setAttribute("aria-label", "Mes siguiente");
    siguiente.addEventListener("click", function () { cambiarMes(1, ".cal-flecha:last-child"); });
    cabecera.append(anterior, titulo, siguiente);

    var rejilla = el("div", "cal-rejilla");
    DIAS.forEach(function (d) { rejilla.appendChild(el("span", "cal-semana", d)); });

    var primero = (new Date(anio, mes, 1).getDay() + 6) % 7;
    var total = new Date(anio, mes + 1, 0).getDate();
    for (var i = 0; i < primero; i++) rejilla.appendChild(el("span", "cal-vacio"));
    for (var d = 1; d <= total; d++) {
      var valor = iso(anio, mes, d);
      var dia = el("button", "cal-dia", String(d));
      dia.type = "button";
      dia.dataset.fecha = valor;
      var hay = conFunciones.indexOf(valor) >= 0;
      if (hay) dia.classList.add("con-funciones");
      if (valor === hoy) dia.classList.add("es-hoy");
      if (valor === elegida) dia.classList.add("elegido");
      dia.tabIndex = -1;
      if (hay) {
        dia.setAttribute("aria-label", d + " de " + MESES[mes] + ", hay funciones");
        dia.addEventListener("click", function (evento) { ir(evento.currentTarget.dataset.fecha); });
      } else {
        dia.setAttribute("aria-disabled", "true");
        dia.setAttribute("aria-label", d + " de " + MESES[mes] + ", sin funciones");
      }
      rejilla.appendChild(dia);
    }

    var pie = el("div", "cal-pie");
    var bLimpiar = el("button", "btn btn-sm btn-outline-uf", "Limpiar");
    bLimpiar.type = "button";
    bLimpiar.addEventListener("click", function () { ir(""); });
    var bHoy = el("button", "btn btn-sm btn-outline-uf", "Hoy");
    bHoy.type = "button";
    bHoy.addEventListener("click", function () { ir(hoy); });
    pie.append(bLimpiar, bHoy);

    panel.append(cabecera, rejilla, pie);

    var inicial = panel.querySelector(".cal-dia.elegido") || panel.querySelector(".cal-dia.es-hoy") || panel.querySelector(".cal-dia");
    if (inicial) inicial.tabIndex = 0;
    if (enfocar && inicial) inicial.focus();
  }

  function cambiarMes(delta, flecha) {
    mes += delta;
    if (mes < 0) { mes = 11; anio--; }
    if (mes > 11) { mes = 0; anio++; }
    pintar(false);
    var foco = flecha && panel.querySelector(flecha);
    if (foco && !foco.disabled) foco.focus();
  }

  function abrir() {
    panel.hidden = false;
    boton.setAttribute("aria-expanded", "true");
    pintar(true);
  }

  function cerrar(devolverFoco) {
    panel.hidden = true;
    boton.setAttribute("aria-expanded", "false");
    if (devolverFoco) boton.focus();
  }

  boton.addEventListener("click", function () {
    if (panel.hidden) abrir(); else cerrar(false);
  });

  panel.addEventListener("keydown", function (evento) {
    var dias = Array.prototype.slice.call(panel.querySelectorAll(".cal-dia"));
    var actual = dias.indexOf(document.activeElement);
    if (actual < 0) return;
    var paso = { ArrowLeft: -1, ArrowRight: 1, ArrowUp: -7, ArrowDown: 7 }[evento.key];
    if (paso) {
      evento.preventDefault();
      var destino = dias[actual + paso];
      if (destino) { dias[actual].tabIndex = -1; destino.tabIndex = 0; destino.focus(); }
    } else if (evento.key === "PageUp" || evento.key === "PageDown") {
      evento.preventDefault();
      if (evento.key === "PageUp" && anio * 12 + mes <= mesInicial) return;
      cambiarMes(evento.key === "PageUp" ? -1 : 1);
      var primero = panel.querySelector(".cal-dia");
      if (primero) { primero.tabIndex = 0; primero.focus(); }
    }
  });

  document.addEventListener("keydown", function (evento) {
    if (evento.key === "Escape" && !panel.hidden) cerrar(true);
  });

  // composedPath se calcula al hacer el clic: sigue valiendo aunque el panel se repinte y el nodo clicado ya no esté
  document.addEventListener("click", function (evento) {
    if (!panel.hidden && evento.composedPath().indexOf(raiz) < 0) cerrar(false);
  });
})();
