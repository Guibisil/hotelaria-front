package DTO;

public class HospedesDTO {

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

    public void setName(String name) {
        this.name = name;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }
}
