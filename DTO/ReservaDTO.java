package DTO;

public class ReservaDTO {

    private Long id;
    private int roomId;
    private String expectedCheckInDate;
    private String expectedCheckOutDate;
    private String statuts;

    public Long getId() {
        return id;
    }

    public int getRoomId() {
        return roomId;
    }

    public String getExpectedCheckInDate() {
        return expectedCheckInDate;
    }

    public String getExpectedCheckOutDate() {
        return expectedCheckOutDate;
    }

    public String getStatuts() {
        return statuts;
    }
}
