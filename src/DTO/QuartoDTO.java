package DTO;

public class QuartoDTO {
    private Long id;
    private String number;
    private String status;
    private int bed_count;
    private CurrentGuestDTO current_guest;

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getStatus() {
        return status;
    }

    public int getBed_count() {
        return bed_count;
    }

    public CurrentGuestDTO getCurrent_guest() {
        return current_guest;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBed_count(int bed_count) {
        this.bed_count = bed_count;
    }

    public void setCurrent_guest(CurrentGuestDTO current_guest) {
        this.current_guest = current_guest;
    }
}
