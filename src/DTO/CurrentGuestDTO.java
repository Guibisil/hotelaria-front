package DTO;

public class CurrentGuestDTO {
    private String name;
    private String checkout_date;

    public String getName() {
        return name;
    }

    public String getCheckout_date() {
        return checkout_date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCheckout_date(String checkout_date) {
        this.checkout_date = checkout_date;
    }
}
