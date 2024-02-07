package org.poo.cb;

public class UserBuilder {

        private String email;
        private String firstname;
        private String lastname;
        private String address;

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setFirstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public UserBuilder setLastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public UserBuilder setAddress(String address) {
            this.address = address;
            return this;
        }

        public User build() {
            User user = new User(email, firstname, lastname, address);
            // Puteți adăuga și alte configurații sau verificări aici, dacă este necesar.
            return user;
        }

}
