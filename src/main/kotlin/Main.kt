import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    var mostrarJuego by remember { mutableStateOf(false) }
    val reglasTexto = "- El usuario es el jugador y el ordenador la banca.  \n" +
            "- No hay en la baraja 8s y 9s. El 10 es la sota, el 11 el caballo y el 12 el Rey.  \n" +
            "- Las figuras (10-sota, 11-caballo y 12-rey) valen medio punto y, el resto, su valor.  \n" +
            "- Hay dos turnos de juego: el turno del jugador y el turno de la banca. Se comienza por el turno del jugador.  \n" +
            "- El jugador va pidiendo cartas a la banca de una en una.  \n" +
            "- El jugador puede plantarse en cualquier momento.  \n" +
            "- Si la suma de los valores de las cartas sacadas es superior a 7 y medio, el jugador 'se pasa de siete y medio' y pierde.  \n" +
            "- Si el jugador no se pasa, comienza a sacar cartas la banca y ésta está obligada a sacar cartas hasta empatar o superar al jugador.  \n" +
            "- Si la banca consigue empatar o superar la puntuación del jugador 'sin pasarse de siete y medio', gana la banca.  \n" +
            "- La banca no se puede plantar y tiene que empatar o superar la puntuación del jugador sin pasarse.  \n" +
            "- En este proceso puede ocurrir que la banca 'se pase' y entonces pierde la banca y gana el jugador.  \n"

    MaterialTheme {
        if (!mostrarJuego) {
            // Pantalla de reglas
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Normas Siete y Juego",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = reglasTexto,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )

                Button(
                    onClick = { mostrarJuego = true },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Ok")
                }
            }
        } else {
            // Pantalla del juego
            PantallaJuego()
        }
    }
}

@Composable
@Preview
fun PantallaJuego(){
    var juego by remember { mutableStateOf(SieteYMedia ())}
    var primera_vez by remember { mutableStateOf(true) }
    if (primera_vez==true) {
        juego.anadirCartaJugador()
        primera_vez = false
    }
    var cartasJugador by remember { mutableStateOf(mostrarCartasJugador(juego)) }
    var mostrarFinal by remember { mutableStateOf(false) }
    var valorCartas by remember { mutableStateOf(juego.valorCartasJugador()) }


    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (mostrarFinal) {
            PantallaFinal(juego)
        }
        else if (juego.DentroLimites(valorCartas)) {
            Text(text = cartasJugador, fontSize = 24.sp)
            Text(text = valorCartas.toString(), fontSize = 24.sp)
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    juego.anadirCartaJugador()
                    cartasJugador = mostrarCartasJugador(juego)
                    valorCartas = juego.valorCartasJugador()
                }) {
                    Text("Carta")
                }
                Button(onClick = { mostrarFinal = true }) {
                    Text("Plantas")
                }
            }
        }
        else {
            Text("Jugador, te has pasado en tu jugada anterior, gana la banca\nAdios",modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            Button(onClick = {
                juego = SieteYMedia()
                juego.anadirCartaJugador()
                cartasJugador = mostrarCartasJugador(juego)
                valorCartas = juego.valorCartasJugador()
            }) {
                Text("Reiniciar juego")
            }
        }
    }
}

@Composable
fun PantallaFinal(juego: SieteYMedia){
    juego.anadirCartasBanca()
    var reiniciarJuego by remember { mutableStateOf(false) }
    if (reiniciarJuego == false)
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Turno de banca ...")
        Text("")
        Text("Éstas son mis cartas:")
        Text(mostrarCartasBanca(juego))
        Text(juego.valorCartasBanca().toString())
        Text (if (!juego.DentroLimites(juego.valorCartasBanca())) {
            "me pasé, ganas tú,jugador"
        } else {
            "Gana la banca"
        }
        )
        Button(onClick = {
            reiniciarJuego=true
        }) {
            Text("Reinicar juego")
        }
    }
    else {
        PantallaJuego()
    }
}


fun mostrarCartasJugador(juego: SieteYMedia):String {
    val resultados = juego.mostrarJugador()
    var devolver=resultados[0].toString()
    for (i in 1..resultados.size-1) {
        if (resultados[i] != null) {
            devolver=devolver+" "+resultados[i]
        }
    }
    return devolver
}
fun mostrarCartasBanca(juego: SieteYMedia):String {
    val resultados = juego.mostrarBanca()
    var devolver=resultados[0].toString()
    for (i in 1..resultados.size-1) {
        if (resultados[i] != null) {
            devolver=devolver+" "+resultados[i]
        }
    }
    return devolver
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
