package it.theboys.project0002api.singletons;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.crypto.bcrypt.BCrypt;

import it.theboys.project0002api.Consts;
import it.theboys.project0002api.data.accounts.PasswordAccount;
import it.theboys.project0002api.data.accounts.UserAccount;
import it.theboys.project0002api.server.BaseCahHandler;

public final class UsersWithAccount {
    private final ServerDatabase db;

    public UsersWithAccount(ServerDatabase db) {
        this.db = db;
    }

    @NotNull
    private static String VALUES(String... values) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");

        boolean first = true;
        for (@Nullable String val : values) {
            if (!first) builder.append(",");
            if (val == null) builder.append("NULL");
            else builder.append("'").append(val).append("'");
            first = false;
        }

        return builder.append(")").toString();
    }

    @NotNull
    private static <A extends UserAccount> A preferences(@NotNull Statement statement, @NotNull A account) throws SQLException {
        ResultSet prefs = statement.executeQuery("SELECT key, value FROM preferences WHERE username='" + account.username + "'");
        account.loadPreferences(prefs);
        return account; // Just to have a better code structure
    }

    @NotNull
    private static ResultSet user(@NotNull Statement statement, @NotNull Consts.AuthType type, @NotNull String key, @NotNull String value) throws SQLException {
        return statement.executeQuery("SELECT * FROM users WHERE " + key + "='" + value + "' AND auth='" + type.toString() + "'");
    }

    // ---- Query methods ---- //

    @Nullable
    public PasswordAccount getPasswordAccountForNickname(@NotNull String nickname) throws BaseCahHandler.CahException {
        try (Statement statement = db.statement();
             ResultSet user = user(statement, Consts.AuthType.PASSWORD, "username", nickname)) {
            return user.next() ? preferences(statement, new PasswordAccount(user)) : null;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (ParseException ex) {
            throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST, ex);
        }
    }

    @Nullable
    public PasswordAccount getPasswordAccountForEmail(@NotNull String email) throws BaseCahHandler.CahException {
        try (Statement statement = db.statement();
             ResultSet user = user(statement, Consts.AuthType.PASSWORD, "email", email)) {
            return user.next() ? preferences(statement, new PasswordAccount(user)) : null;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        } catch (ParseException ex) {
            throw new BaseCahHandler.CahException(Consts.ErrorCode.BAD_REQUEST, ex);
        }
    }

    public boolean hasEmail(@NotNull String email) {
        try (ResultSet set = db.statement().executeQuery("SELECT count(*) FROM users WHERE email='" + email + "'")) {
            set.next();
            return set.getInt(1) > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean hasNickname(@NotNull String nickname) {
        try (ResultSet set = db.statement().executeQuery("SELECT count(*) FROM users WHERE username='" + nickname + "'")) {
            set.next();
            return set.getInt(1) > 0;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    // ---- Add methods ---- //

    private void addAccount(@NotNull PasswordAccount account) {
        try (Statement statement = db.statement()) {
            int result = statement.executeUpdate("INSERT INTO users (username, auth, email, email_verified, avatar_url, password) VALUES "
                    + VALUES(account.username, Consts.AuthType.PASSWORD.toString(), account.email, "0", account.avatarUrl, account.hashedPassword));

            if (result != 1) throw new IllegalStateException();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    // ---- Update methods ---- //

    public void updatePreferences(UserAccount account, Map<String, String> map) {
        try (Statement statement = db.statement()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                statement.executeUpdate("INSERT OR REPLACE INTO preferences (username, key, value) VALUES " +
                        VALUES(account.username, entry.getKey(), entry.getValue()));
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void updateVerifiedStatus(PasswordAccount account, boolean verified) {
        try (Statement statement = db.statement()) {
            int result = statement.executeUpdate("UPDATE users SET email_verified=" + (verified ? 1 : 0) + " WHERE email='" + account.email + "'");
            if (result == 0) throw new IllegalStateException();
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    // ---- Utility methods ---- //

    @NotNull
    public PasswordAccount registerWithPassword(@NotNull String nickname, @NotNull String email, @NotNull String password) {
        PasswordAccount account = new PasswordAccount(nickname, email, false, BCrypt.hashpw(password, BCrypt.gensalt()));
        addAccount(account);
        return account;
    }
}