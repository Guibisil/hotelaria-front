package DTO;

public class ReservaDTO {

    private Long id;
    private int room_id;
    private int guest_id;
    private float total_amount;
    private String checkin_date;
    private String checkout_date;
    private String statuts;

    public Long getId() {
        return id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public String getStatuts() {
        return statuts;
    }

    public String getCheckout_date() {
        return checkout_date;
    }

    public String getCheckin_date() {
        return checkin_date;
    }

    public int getGuest_id() {
        return guest_id;
    }

    public float getTotal_amount() {
        return total_amount;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public void setGuest_id(int guest_id) {
        this.guest_id = guest_id;
    }

    public void setTotal_amount(float total_amount) {
        this.total_amount = total_amount;
    }

    public void setCheckin_date(String checkin_date) {
        this.checkin_date = checkin_date;
    }

    public void setCheckout_date(String checkout_date) {
        this.checkout_date = checkout_date;
    }
}