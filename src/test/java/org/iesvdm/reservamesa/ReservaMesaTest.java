package org.iesvdm.reservamesa;

//import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ReservaMesaTest {

    ReservaMesa reserva;

    @BeforeEach
    void setUp(){
        reserva = new ReservaMesa();
        reserva.setTamanioMesa(8);
        reserva.setMesas(new int[10]);
    }

    @Test
    void getMesasTest(){
        // WHEN
        int[] tablaNueva = new int[reserva.getMesas().length];

        for(int i=0; i<tablaNueva.length; i++){
            tablaNueva[i] = (int)(Math.random()* (reserva.getTamanioMesa() + 1));
        }

        // DO
        reserva.setMesas(tablaNueva);

        // THEN
        for(int i=0; i< reserva.getMesas().length; i++){
            assertThat(reserva.getMesas()[i] == tablaNueva[i]);
        }
    }

    @Test
    void getTamanioMesaTest(){
        // WHEN & DO
        int num = (int)(Math.random()*20 + 1);

        reserva.setTamanioMesa(num);

        // THEN
        assertThat(reserva.getTamanioMesa()).isEqualTo(num);
    }

    /*Comprobación de que cada mesa tiene tamaño entre 1 y ´´tamanioMesa``*/
    @Test
    void rellenarMesasTest(){
        // WHEN & DO
        reserva.rellenarMesas();

        // THEN
        for(int mesa : reserva.getMesas()){
            assertThat(mesa).isBetween(1, reserva.getTamanioMesa());
        }
    }

    @Test
    void buscarPrimeraMesaVaciaTest1SiHayMesasVacias(){
        // WHEN & DO
        for(int i=0; i<reserva.getMesas().length; i++){
            int numNoCero = (int)(Math.random()*reserva.getTamanioMesa() + 1);

            reserva.getMesas()[i] = numNoCero;
        }

        int posMesaVacia = (int)(Math.random()*reserva.getMesas().length);

        reserva.getMesas()[posMesaVacia] = 0;

        // THEN
        assertThat(reserva.buscarPrimeraMesaVacia()).isEqualTo(posMesaVacia);
    }

    @Test
    void buscarPrimeraMesaVaciaTest2NoHayMesasVacias(){
        // WHEN & DO
        for(int i=0; i<reserva.getMesas().length; i++){
            int numNoCero = (int)(Math.random()*reserva.getTamanioMesa() + 1);

            reserva.getMesas()[i] = numNoCero;
        }

        // THEN
        assertThat(reserva.buscarPrimeraMesaVacia()).isEqualTo(-1);
    }

    @Test
    void buscarMesaParaCompartirTest(){
        // WHEN & DO
        ReservaMesa reserva2 = new ReservaMesa();
        reserva2.setTamanioMesa(8);
        reserva2.setMesas(new int[10]);

        for(int i=0; i<reserva.getMesas().length; i++){
            reserva.getMesas()[i] = 8;
            reserva2.getMesas()[i] = 8;
        }

        int posMesaNoLlena = (int)(Math.random()*reserva.getMesas().length);

        reserva.getMesas()[posMesaNoLlena] = (int)(Math.random()* reserva.getTamanioMesa());

        // THEN
        assertThat(reserva.buscarMesaParaCompartir(1)).isEqualTo(posMesaNoLlena);
        assertThat(reserva2.buscarMesaParaCompartir(1)).isEqualTo(-1);
    }

    @Test
    void comensalesTotalesTest(){
        // WHEN
        int comensales = 0;

        // DO
        for(int mesa : reserva.getMesas()){
            mesa = (int)(Math.random() * (reserva.getTamanioMesa()+1));

            comensales *= mesa;
        }

        // THEN
        assertThat(reserva.comensalesTotales()).isEqualTo(comensales);
    }

    /* PLANTILLAS DE LOS WHEN-DO-THEN
        // WHEN


        // DO


        // THEN

    * */

}
