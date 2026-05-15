package models

import java.util.Date

class Contacto {
    private val name: String;
    private val birthDate: Date;
    private val phoneNumber: Int;
    private val email: String;

    constructor(name: String, birthDate: Date, phoneNumber: Int, email: String) {
        this.name = name;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;

        if (!Regex ("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$").matches(email)) {
            throw Exception("Email no valido")
        }else{
            this.email = email;
        }
    }

    public fun getBirthDate(): Date {
        return birthDate;
    }
    public fun getEmail(): String {
        return email;
    }
    public fun getName(): String {
        return name;
    }
    public fun getPhoneNumber(): Int {
        return phoneNumber;
    }

}