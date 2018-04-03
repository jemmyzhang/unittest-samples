package pers.jz.unittest.entity;

import java.util.Objects;
import java.util.UUID;

/**
 * @author jemmyzhang on 2018/4/3.
 */
public class User {

    private Long id;

    private String name;

    private String email;

    private String address;

    public User(Long id, String name, String email, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public User() {
    }

    public static Builder withId(Long id) {
        return new Builder().id(id);
    }

    public static Builder withRandomId(){
        return new Builder().randomId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(address, user.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, name, email, address);
    }

    public static class Builder {

        private Long id;
        private String name;
        private String email;
        private String address;

        private Builder() {
        }

        private Builder id(Long id) {
            this.id = id;
            return this;
        }

        private Builder randomId() {
            this.id = UUID.randomUUID().getMostSignificantBits();
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public User build() {
            return new User(id, name, email, address);
        }


    }
}
