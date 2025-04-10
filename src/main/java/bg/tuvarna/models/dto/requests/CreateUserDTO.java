package bg.tuvarna.models.dto.requests;

import bg.tuvarna.enums.EmployeePosition;

public class CreateUserDTO {
    private String username;
    private String email;
    private String password;
    private EmployeePosition position;

    public CreateUserDTO() {
    }

    public CreateUserDTO(String username, String email, String password, EmployeePosition position) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.position = position;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EmployeePosition getPosition() {
        return position;
    }

    public void setPosition(EmployeePosition position) {
        this.position = position;
    }
}
