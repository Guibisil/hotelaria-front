package DTO;

public class HospedesDTO {

    private Long id;
    private String name;
    private String cpf;
    private String email;
    private String birth_date;

    public String getName() {
        return name;
    }

    public String getCpf() {
        return cpf;
    }

    public String getEmail() {
        return email;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public Long getId() {
        return id;
    }
}
