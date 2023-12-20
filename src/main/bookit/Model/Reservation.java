package main.bookit.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reservation {
    private int id;
    private int listId;
    private int userId;
    private int coopId;
    private int sequence;
}
