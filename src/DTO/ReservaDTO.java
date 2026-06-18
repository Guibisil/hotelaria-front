package DTO;

public class ReservaDTO {

    private Long id;
    private int room_id;
    private String checkin_date;
    private String checkout_date;
    private String status;

    public Long getId() {
        return id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public String getStatus() {
        return status;
    }

    public String getCheckout_date() {
        return checkout_date;
    }

    public String getCheckin_date() {
        return checkin_date;
    }
}