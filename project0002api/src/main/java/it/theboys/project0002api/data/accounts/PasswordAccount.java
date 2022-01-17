package it.theboys.project0002api.data.accounts;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import javax.validation.constraints.NotNull;

import it.theboys.project0002api.Consts;

public class PasswordAccount extends UserAccount {
    public final String hashedPassword;

    public PasswordAccount(ResultSet user) throws SQLException, ParseException {
        super(user, user.getBoolean("email_verified"));

        hashedPassword = user.getString("password");
    }

    public PasswordAccount(String username, String email, boolean emailVerified, @NotNull String hashedPassword) {
        super(username, email, Consts.AuthType.PASSWORD, emailVerified, null); // TODO: Avatar

        this.hashedPassword = hashedPassword;
    }
}