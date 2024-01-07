package main.bookit.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reservation {
    private int id;
    private int listId;
    private int userId;
    private String username; // The username of the user who made the reservation
    private int coopId;
    private int sequence;
    private String listName;
    private String coWorker; // The username of the co-worker
}
