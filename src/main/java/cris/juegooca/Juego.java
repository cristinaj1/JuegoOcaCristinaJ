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
        
        ArrayList<Jugador> aux = cj.getTodosJugadores();
        
        do {
            // Imprime el estado del tablero inicialmente
            Vista.mostrarTablero(juego.getTablero());
            for (Jugador jugador : aux) {
                if (jugador.getTurnosSinJugar() == 0) {
                    
                    do {
                        tablero.getCasilla(jugador.getCasillaActual()).quitarJugador(jugador);
                        
                        jugador.tirarDado();
                        
                        jugador.mover(jugador.getTirada());
                        
                        Vista.informarTirada(jugador);
                        
                        Vista.informarProgreso(jugador);
                        
                        jugador.mover(tablero.getCasilla(jugador.getCasillaActual()).getTipo().getSiguienteMovimiento());
                        
                        jugador.setTurnosSinJugar(tablero.getCasilla(jugador.getCasillaActual()).getTipo().getTurnosSinJugar());
                        
                        tablero.getCasilla(jugador.getCasillaActual()).ponerJugador(jugador);
                        
                        if (jugador.getCasillaActual() == 31) {
                            jugador.setPozo(true);
                        }
                        
                    } while (tablero.getCasilla(jugador.getCasillaActual()).getTipo().isTiradaExtra());
                } else if (jugador.isPozo()) {
                    for (int i = 0; i < aux.size(); i++) {
                        if (aux.get(i).getCasillaActual() > 31) {//31 es la casilla pozo
                            jugador.setTurnosSinJugar(0);
                            break;
                        }
                    }
                    jugador.setPozo(false);
                } else {
                    
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
