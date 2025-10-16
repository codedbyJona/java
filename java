import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

class Room {
    private int number;
    private boolean isOccupied;

    public Room(int number) {
        this.number = number;
        this.isOccupied = false;
    }

    public void reserve(LocalDate startDate, LocalDate endDate) {
        isOccupied = true;
        // Perform necessary checks and actions for making a reservation
    }

    public void free() {
        isOccupied = false;
        // Perform necessary actions for freeing a room
    }

    public int getNumber() {
        return number;
    }

    public boolean isOccupied() {
        return isOccupied;
    }
}
class Reservation {
    private LocalDate startDate;
    private LocalDate endDate;
    private Room room;

    public Reservation(LocalDate startDate, LocalDate endDate, Room room) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.room = room;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Room getRoom() {
        return room;
    }
}

class HotelManagementSystem {
    private List<Room> rooms;
    private List<Reservation> reservations;

    public HotelManagementSystem(List<Room> rooms, List<Reservation> reservations) {
        this.rooms = rooms;
        this.reservations = reservations;
    }

    public boolean makeReservation(Reservation reservation) {
        // Check if the room is available during the reservation period
        long reservationDuration = ChronoUnit.DAYS.between(reservation.getStartDate(), reservation.getEndDate());
        return rooms.stream()
                .filter(room -> Objects.equals(room.getNumber(), reservation.getRoom().getNumber()))
                .filter(room -> !room.isOccupied())
                .anyMatch(room -> checkRoomAvailability(room, reservation.getStartDate(), reservationDuration));
    }

    private boolean checkRoomAvailability(Room room, LocalDate startDate, long reservationDuration) {
        return reservations.stream()
                .filter(reservation -> Objects.equals(reservation.getRoom().getNumber(), room.getNumber()))
                .filter(reservation -> !reservation.getEndDate().isBefore(startDate))
                .filter(reservation -> !startDate.isAfter(reservation.getStartDate()))
                .mapToLong(reservation -> ChronoUnit.DAYS.between(startDate, reservation.getEndDate()))
                .sum() < reservationDuration;
    }

    public List<Room> checkRoomAvailability(LocalDate startDate, LocalDate endDate) {
        return rooms.stream()
                .filter(room -> !room.isOccupied())
                .filter(room -> checkRoomAvailability(room, startDate, ChronoUnit.DAYS.between(startDate, endDate)))
                .collect(Collectors.toList());
    }

    public Map<LocalDate, List<Reservation>> viewReservations() {
        return reservations.stream()
                .collect(Collectors.groupingBy(Reservation::getStartDate));
    }

    public void printReservations() {
        viewReservations().forEach((date, reservations) -> {
            System.out.println("Reservations for " + date);
            reservations.forEach(reservation -> System.out.println("Room " + reservation.getRoom().getNumber() + " from " + reservation.getStartDate() + " to " + reservation.getEndDate()));
        });
    }
}
public class HotelManagementSystemExample {

	 public static void main(String[] args) {
	        // Initialize hotel rooms and reservations
	        List<Room> rooms = new ArrayList<>();
	        for (int i = 1; i <= 10; i++) {
	            rooms.add(new Room(i));
	        }

	        List<Reservation> reservations = new ArrayList<>();
	        reservations.add(new Reservation(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 5), rooms.get(0)));
	        reservations.add(new Reservation(LocalDate.of(2022, 1, 5), LocalDate.of(2022, 1, 10), rooms.get(1)));

	        HotelManagementSystem hotelManagementSystem = new HotelManagementSystem(rooms, reservations);

	        // Make a reservation
	        Reservation newReservation = new Reservation(LocalDate.of(2022, 1, 2), LocalDate.of(2022, 1, 4), rooms.get(2));
	        if (hotelManagementSystem.makeReservation(newReservation)) {
	            reservations.add(newReservation);
	            System.out.println("Reservation made successfully!");
	        } else {
	            System.out.println("Room not available for the specified dates.");
	        }

	        // Check room availability
	        List<Room> availableRooms = hotelManagementSystem.checkRoomAvailability(LocalDate.of(2022, 1, 2), LocalDate.of(2022, 1, 4));
	        System.out.println("Available rooms:");
	        availableRooms.forEach(room -> System.out.println("Room " + room.getNumber()));

	        // View reservationsSystem.out.println("\nExisting reservations:");
	        hotelManagementSystem.printReservations();
	    }
	}
