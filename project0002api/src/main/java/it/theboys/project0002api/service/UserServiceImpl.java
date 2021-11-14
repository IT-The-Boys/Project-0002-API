package it.theboys.project0002api.service;

import it.theboys.project0002api.enums.UserRole;
import it.theboys.project0002api.exception.database.CardSetCollectionException;
import it.theboys.project0002api.exception.database.UserCollectionException;
import it.theboys.project0002api.model.CardSet;
import it.theboys.project0002api.model.SecUserDetails;
import it.theboys.project0002api.model.database.User;
import it.theboys.project0002api.model.database.cah.CahCard;
import it.theboys.project0002api.repository.UserRepository;
import it.theboys.project0002api.storage.GuestUserStorage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private MailService mailService;

    @Override
    public User register(User user) throws UserCollectionException {
        //check if user is registering or using guest account
        if (user.getUserContact() != null && !user.getUserContact().isBlank()) {
            // add user to db
            user.addUserRole(UserRole.USER);
            user.setVerificationCode(UUID.randomUUID().toString());
            String message = String.format("Hello, %s! \n" +
                            "Welcome to please visit next link: http://127.0.0.1:8080/api/v1/user/activate/%s \n" +
                            "to verify account",
                    user.getUserName(), user.getVerificationCode());
            mailService.sendTo(user.getUserContact(), "Verification code", message);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setActive(true);
            user.setRegisteredAt(Instant.now().toEpochMilli());
            return userRepo.save(user);
        } else {
            String username = user.getUserName();
            List<String> usernamesInDb = userRepo.findDistinctUserName();
            List<String> usernamesInStorage = GuestUserStorage.getInstance().getUsernameList();
            if (usernamesInDb.contains(username)|| usernamesInStorage.contains(username))
                throw new UserCollectionException(UserCollectionException.UsernameConstraintException(username));
            user.setActive(true);
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setLastVisitedAt(Instant.now().toEpochMilli());
            // add user to storage
            return GuestUserStorage.getInstance().add(user);
        }

    }

    @Override
    public String guestLogin() {
        return null;
    }

    @Override
    public String login() {
        return null;
    }

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> inDB = userRepo.findUserByUserName(username);
        if (!inDB.isEmpty()) return new SecUserDetails(inDB.get());
        User guest=GuestUserStorage.getInstance().getUserPassword(username);
        if (guest!=null) return new SecUserDetails(guest);
        throw new UserCollectionException(UserCollectionException.NotFoundException(username));
    }

    @Override
    public String getUserList() {
        return null;
    }

    @Override
    public User getUser(String id) throws UserCollectionException {
        Optional<User> inDB = userRepo.findById(id);
        if (inDB.isEmpty()) throw new UserCollectionException(UserCollectionException.NotFoundException(id));
        return inDB.get();
    }

    @Override
    public User verifyUser(String code) throws UserCollectionException {
        Optional<User> inDB = userRepo.findUserByVerificationCode(code);
        if (inDB.isEmpty()) throw new UserCollectionException(UserCollectionException.VerificationCodeException(code));
        User user = inDB.get();
        user.setVerificationCode(null);
        user.setVerified(true);
        user.setActive(true);
        return userRepo.save(user);
    }

    @Override
    public List<String> getUsernameList() {
        return userRepo.findDistinctUserName();
    }

    @Override
    public void deleteUser(String id) throws UserCollectionException {
        Optional<User> inDB = userRepo.findById(id);
        // check if set exist
        if (inDB.isPresent()) {
            userRepo.deleteById(id);
        } else {
            throw new UserCollectionException(UserCollectionException.NotFoundException(id));
        }
    }

    @Override
    public User modifyUser(String id, String request) {
        return null;
    }
}