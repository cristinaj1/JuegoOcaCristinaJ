/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cris.juegooca;

import java.util.ArrayList;

/**
 *
 * @author Cris
 */
public class Juego {

    private final Tablero tablero;
    private final ControladorJugadores cj;

    // Crea el juego a partir del tablero y la  lista de jugadores
    // Pone a los jugadores en la casilla de salida
    public Juego(Tablero tablero, ControladorJugadores controlador) {
        this.tablero = tablero;
        this.cj = controlador;
        // Coloca a los jugadores en la casilla de salida
        for (Jugador aux : cj.getTodosJugadores()) {
            this.tablero.getCasilla(1).ponerJugador(aux);
        }

    }

    private static boolean terminar(ArrayList<Jugador> prueba) {
        boolean sol = false;
        for (Jugador j : prueba) {
            if (j.ganaPartida()) {
                sol = true;
            }
        }
        return sol;

    }

    public static void main(String[] args) {

        // Se crea el juego
        String[] nombres = {"J1", "J2", "J3"};

        ControladorJugadores cj = new ControladorJugadores(nombres);

        Tablero tablero = new Tablero();

        Juego juego = new Juego(tablero, cj);

        //Guardo a los jugadores en un arrayList para poder trabajar con ellos facilmente
        ArrayList<Jugador> aux = cj.getTodosJugadores();

        //Este do while lo que hace es que el jueo se ejecute hasta que se acabe
        do {
            // Imprime el estado del tablero inicialmente
            Vista.mostrarTablero(juego.getTablero());
            
            //Este for hace que se ejecute todo jugador por jugador para que así 
            //todos tiren, se muevan y se cumplan todos los requisitos en todos los turnos
            for (Jugador jugador : aux) {

                //El if nos indica que mientras no le queden al 
                //jugador turnos sin jugar puede ejecutarse el programa
                if (jugador.getTurnosSinJugar() == 0) {

                    do {
                        //esto hace que se vea el movimiento y 
                        //se borre en la anterior(para que no se repita posicion)
                        tablero.getCasilla(jugador.getCasillaActual()).quitarJugador(jugador);

                        //Hace que salga un número random entre 1 y 6.
                        jugador.tirarDado();

                        //Hace que se mueva en el array según el número aleatorio anterior
                        jugador.mover(jugador.getTirada());

                        //Informa de la tirada para que se vea por consola
                        Vista.informarTirada(jugador);

                        //Informa por consola hacia donde se mueve y donde acaba
                        Vista.informarProgreso(jugador);

                        //Hace que se mueva pero respetando las casillas(en el método anterior no lo respeta)
                        jugador.mover(tablero.getCasilla(jugador.getCasillaActual()).getTipo().getSiguienteMovimiento());

                        //Hace que se respeten las casillas de turnos sin jugar y se sigan los pasos
                        jugador.setTurnosSinJugar(tablero.getCasilla(jugador.getCasillaActual()).getTipo().getTurnosSinJugar());

                        //Hace que se vea al jugador en la casilla que le toca  y podamos ver el juego en el tablero
                        tablero.getCasilla(jugador.getCasillaActual()).ponerJugador(jugador);

                        //Hace que se respete el pozo(y salga el otro compañero)
                        if (jugador.getCasillaActual() == 31) {
                            jugador.setPozo(true);
                        }
                        //Hace que se respete la tirada extra como en el acso de la oca
                    } while (tablero.getCasilla(jugador.getCasillaActual()).getTipo().isTiradaExtra());

                    //Hace que se respete el pozo antes de comenzar el juego 
                } else if (jugador.isPozo()) {
                    for (int i = 0; i < aux.size(); i++) {
                        if (aux.get(i).getCasillaActual() > 31) {//31 es la casilla pozo

                            //Aquí avisa de que si tiene turnos en 0 se podrá salir sin problema
                            jugador.setTurnosSinJugar(0);
                            break;
                        }
                    }
                    jugador.setPozo(false);
                } else {
                    
                    //Aquí avisa al jugador de los turnos que le toca sin jugar quitándole uno.
                    jugador.setTurnosSinJugar(jugador.getTurnosSinJugar() - 1);
                    System.out.println("Te quedan: " + jugador.getTurnosSinJugar());

                }
            }
        } while (!terminar(aux));

    }

    public Tablero getTablero() {
        return tablero;
    }

    public ControladorJugadores getCj() {
        return cj;
    }

}
