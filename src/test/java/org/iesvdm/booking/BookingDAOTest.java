package org.iesvdm.booking;


import static org.assertj.core.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDate;
import java.util.*;


public class BookingDAOTest {


    private BookingDAO bookingDAO;
    private Map<String, BookingRequest> bookings;


    @BeforeEach
    public void setup() {
        bookings = new HashMap<>();
        bookingDAO = new BookingDAO(bookings);
    }


    /**
     * Crea 2 peticiones de reserva (BookingRequest)
     * agrégalas a las reservas (bookings) con la que
     * construyes el objeto BookingDAO bajo testeo.
     * Comprueba que cuando invocas bookingDAO.getAllBookingRequest
     * obtienes las 2 peticiones.
     */
    @Test
    void  getAllBookingRequestsTest() {
        // Creación de las reservas
        LocalDate fecha1 = LocalDate.of(2020, 1, 1);
        LocalDate fecha2 = LocalDate.of(2021, 1, 2);
        LocalDate fecha3 = LocalDate.of(2022, 1, 3);
        LocalDate fecha4 = LocalDate.of(2023, 1, 4);


        BookingRequest br1 = new BookingRequest("1",fecha1,fecha2,4,false);
        BookingRequest br2 = new BookingRequest("2",fecha3,fecha4,6,true);


        // Añadido al mapa
        bookings.put(br1.getUserId(), br1);
        bookings.put(br2.getUserId(), br2);


        // Recogida de las reservas
        Collection<BookingRequest> coleccion = bookingDAO.getAllBookingRequests();


        // Comprobación
        int cont = 1;


        Iterator<BookingRequest> iterador = coleccion.iterator();
        while(iterador.hasNext()){
            BookingRequest reserva = iterador.next();


            if(cont == 1){
                assertThat(reserva).isEqualTo(br1);
            } else{
                assertThat(reserva).isEqualTo(br2);
            }


            cont++;
        }
    }


    /**
     * Crea 2 peticiones de reserva (BookingRequest)
     * agrégalas a las reservas mediante bookingDAO.save.
     * Comprueba que cuando invocas bookingDAO.getAllUUIDs
     * obtienes las UUIDs de las 2 peticiones guardadas.
     */
    @Test
    void getAllUUIDsTest() {
        // Creación de las reservas
        LocalDate fecha1 = LocalDate.of(2020, 1, 1);
        LocalDate fecha2 = LocalDate.of(2021, 1, 2);
        LocalDate fecha3 = LocalDate.of(2022, 1, 3);
        LocalDate fecha4 = LocalDate.of(2023, 1, 4);


        BookingRequest br1 = new BookingRequest("1",fecha1,fecha2,4,false);
        BookingRequest br2 = new BookingRequest("2",fecha3,fecha4,6,true);


        // Añadido al mapa
        bookingDAO.save(br1);
        bookingDAO.save(br2);


        // Recogida de las claves de la forma del proyecto y de la forma predefinida de Java
        Set<String> coleccion = bookingDAO.getAllUUIDs();
        Set<String> claves = bookings.keySet();


        // Comprobación
        assertThat(coleccion).containsAll(claves);
    }




    /**
     * Crea 2 peticiones de reserva (BookingRequest)
     * agrégalas a las reservas mediante bookingDAO.save.
     * Comprueba que cuando invocas bookingDAO.get con el UUID
     * obtienes las respectivas 2 peticiones guardadas.
     */
    @Test
    void getTest() {
        // Creación de las reservas
        LocalDate fecha1 = LocalDate.of(2020, 1, 1);
        LocalDate fecha2 = LocalDate.of(2021, 1, 2);
        LocalDate fecha3 = LocalDate.of(2022, 1, 3);
        LocalDate fecha4 = LocalDate.of(2023, 1, 4);


        BookingRequest br1 = new BookingRequest("1",fecha1,fecha2,4,false);
        BookingRequest br2 = new BookingRequest("2",fecha3,fecha4,6,true);


        // Añadido al mapa
        bookingDAO.save(br1);
        bookingDAO.save(br2);


        // Recogida de las claves de la forma predefinida de Java
        Set<String> claves = bookings.keySet();


        // Comprobación
        Iterator<String> iterador = claves.iterator();
        while(iterador.hasNext()){
            String clave = iterador.next();


            assertThat(bookingDAO.get(clave)).isEqualTo(bookings.get(clave));
        }




    }


    /**
     * Crea 2 peticiones de reserva (BookingRequest)
     * agrégalas a las reservas mediante bookingDAO.save.
     * A continuación, borra la primera y comprueba
     * que se mantiene 1 reserva, la segunda guardada.
     */
    @Test
    void deleteTest() {
        // Creación de las reservas
        LocalDate fecha1 = LocalDate.of(2020, 1, 1);
        LocalDate fecha2 = LocalDate.of(2021, 1, 2);
        LocalDate fecha3 = LocalDate.of(2022, 1, 3);
        LocalDate fecha4 = LocalDate.of(2023, 1, 4);


        BookingRequest br1 = new BookingRequest("1",fecha1,fecha2,4,false);
        BookingRequest br2 = new BookingRequest("2",fecha3,fecha4,6,true);


        // Añadido al mapa
        bookingDAO.save(br1);
        bookingDAO.save(br2);


        // Recogida de las claves de la forma predefinida de Java
        Set<String> claves = bookings.keySet();

        // Eliminación de la 1ª reserva
        boolean borrado = false;

        Iterator<String> iterador = claves.iterator();
        while(iterador.hasNext() && !borrado){
            String clave = iterador.next();

            if(bookingDAO.get(clave).equals(br1)){
                borrado = true;

                bookingDAO.delete(clave);
            }
        }

        // Comprobación
        assertThat(bookingDAO.getAllBookingRequests()).containsOnly(br2);
    }


    /**
     * Guarda 2 veces la misma petición de reserva (BookingRequest)
     * y demuestra que en la colección de bookings están repetidas
     * pero con UUID diferente
     *
     */
    @Test
    void saveTwiceSameBookingRequestTest() {
        // Creación de las reservas
        LocalDate fecha1 = LocalDate.of(2020, 1, 1);
        LocalDate fecha2 = LocalDate.of(2021, 1, 2);


        BookingRequest br1 = new BookingRequest("1",fecha1,fecha2,4,false);


        // Añadido al mapa
        bookingDAO.save(br1);
        bookingDAO.save(br1);

        // Recogida de las claves de la forma predefinida de Java
        int cont = 1;
        BookingRequest reserva1 = null;
        String clave1 = null;

        Set<String> claves = bookings.keySet();

        // Comprobación
        Iterator<String> iterador = claves.iterator();
        while(iterador.hasNext()){
            String clave = iterador.next();

            if(cont == 1){
                reserva1 = bookingDAO.get(clave);
                clave1 = clave;
            } else{
                assertThat(clave).isNotEqualTo(clave1);
                assertThat(bookingDAO.get(clave)).isEqualTo(reserva1);
            }

            cont++;
        }

    }


}
