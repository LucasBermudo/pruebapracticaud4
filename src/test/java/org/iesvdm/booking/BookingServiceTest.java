package org.iesvdm.booking;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BookingServiceTest {

    @Mock
    private PaymentService paymentService;
    @Mock
    private  RoomService roomService;
    @Spy
    private  BookingDAO bookingDAO = new BookingDAO(new HashMap<>());
    @Mock
    private  MailSender mailSender;
    @InjectMocks
    private BookingService bookingService;
    @Captor
    private ArgumentCaptor<BookingRequest> bookingRequestCaptor;
    @Captor
    private ArgumentCaptor<String> roomIdCaptor;
    @Captor
    private ArgumentCaptor<String> bookingIdCaptor;
    @Captor
    private ArgumentCaptor<Double> priceCaptor;
    @Spy
    private BookingRequest bookingRequest1 = new BookingRequest("1"
            , LocalDate.of(2024,6, 10)
            , LocalDate.of(2024, 6, 16)
            ,4
            ,false
            );

    @Spy
    private BookingRequest bookingRequest2 = new BookingRequest("2"
            , LocalDate.of(2024,8, 3)
            , LocalDate.of(2024, 9, 9)
            ,3
            ,true
    );

    @BeforeEach
    public void setup() {

        MockitoAnnotations.initMocks(this);


    }

    /**
     * Crea un stub para roomService.getAvailableRoom
     * que devuelva una lista de 3 habitaciones disponibles
     * con un total de plazas libres de 10 para la invocación
     * de getAvailablePlaceCount.
     */
    @Test
    void getAvailablePlaceCountTest() {
        Room r1 = new Room("1",2);
        Room r2 = new Room("2",5);
        Room r3 = new Room("3",3);

        ArrayList<Room> rooms = new ArrayList<>();

        rooms.add(r1);
        rooms.add(r2);
        rooms.add(r3);

        when(roomService.getAvailableRooms()).thenReturn(rooms);

        assertThat(bookingService.getAvailablePlaceCount()).isEqualTo(10);
    }

    /**
     * Verifica que para bookingRequest1 se invocan
     * los métodos getDateFrom, getDateTo y getGuestCount
     * y que el precio en dólares (bookingService.calculatePrice) es el que corresponde para
     * el número de noches de la reserva de bookingRequest1.
     */
     @Test
    void calculatePriceTest() {
        bookingRequest1.getDateFrom();
        bookingRequest1.getDateTo();
        bookingRequest1.getGuestCount();

        verify(bookingRequest1).getDateFrom();
        verify(bookingRequest1).getDateTo();
        verify(bookingRequest1).getGuestCount();

        int noches = (int)ChronoUnit.DAYS.between(bookingRequest1.getDateFrom(),bookingRequest1.getDateTo());
        double precio = 50 * bookingRequest1.getGuestCount() * noches;

        assertThat(bookingService.calculatePrice(bookingRequest1)).isEqualTo(precio);
     }


    /**
     * Crea un stub para roomService.findAvailableRoomId
     * que cuando se pase la bookingRequest2 devuelva el roomId
     * 101.
     * Verfica si la bookingRequest2 es de prepago (isPrepaid)
     * , se invoca paymentService.pay pasándosele los argumentos
     * bookingRequest2 y el precio esperado. Para ello, captura el bookingRequest
     * el precio en paymentService.pay con los captors necesarios.
     *
     */
    @Test
    void makeBookingTest1() {
        when(roomService.findAvailableRoomId(bookingRequest2)).thenReturn("101");

        assertThat(bookingRequest2.isPrepaid()).isTrue();

        bookingService.makeBooking(bookingRequest2);

        verify(paymentService).pay(bookingRequest2, bookingService.calculatePrice(bookingRequest2));
    }

    /**
     * Igual que antes, crea un stub para roomService.findAvailableRoomId
     * que cuando se pase la bookingRequest2 devuelva el roomId
     * 101.
     * Ahora verifica que se llaman los métodos roomService.bookRoom
     * con el argumento (a capturar por captor) 101.
     * Verifica, también, que se invoca a mailSender.sendBookingConfirmation
     * con el bookingId esperado (es decir, el que devuelve makeBooking).
     * En este caso la verificación tiene que cumplir el orden de invocación
     * bookRoom 1º, sendBookingConfirmation 2º
     */
    @Test
    void makeBookingTest2() {
        when(roomService.findAvailableRoomId(bookingRequest2)).thenReturn("101");

//        verify()

    }
}
