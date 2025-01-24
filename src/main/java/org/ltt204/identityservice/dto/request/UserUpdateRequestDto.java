package org.ltt204.identityservice.dto.request;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UserUpdateRequestDto {
    @Size(min = 8, message = "INVALID_PASWORD")
    private String passWord;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
