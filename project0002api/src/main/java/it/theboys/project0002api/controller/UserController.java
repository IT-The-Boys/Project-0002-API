package it.theboys.project0002api.controller;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.DuplicateKeyException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.theboys.project0002api.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Slf4j
public class UserController {
    @Autowired
    private final UserService userService;

    @PostMapping("/signup")
//    @CrossOrigin(origins = "http://localhost:3000")
    @JsonView(UserView.IdNameContact.class)
    public ResponseEntity<?> register(@RequestBody User request) {
        try {
            log.debug(String.valueOf(request));
            return new ResponseEntity<>(userService.register(request), HttpStatus.OK);
        } catch (UserCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (ConstraintViolationException| DuplicateKeyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/token/refresh")
//    @CrossOrigin(origins = "http://localhost:3000")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws AppSecurityException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String refresh = authHeader.substring("Bearer ".length());
                Algorithm a = Algorithm.HMAC256("ecchi".getBytes());
                JWTVerifier verifier = JWT.require(a).build();
                DecodedJWT decodedJWT = verifier.verify(refresh);
                String username = decodedJWT.getSubject();
                User user = userService.getUserByUsername(username);
                String access = new AppSecurityUtils().generateJWT(new SecUserDetails(user), request.getRequestURL().toString(), 10 * 60 * 1000);
                TokenResponse(response, refresh, access);
            } catch (Exception e) {
                TokenErrorResponse(response, e);
            }

        } else {
            TokenErrorResponse(response, new AppSecurityException(AppSecurityException.TokenNotFoundException()));
        }
    }

    /**
     * Generate view with access and refresh tokens
     *
     * @param response HTTP response for output
     * @param refresh  refresh token
     * @param access   access token
     * @throws IOException error handling
     */
    public static void TokenResponse(HttpServletResponse response, String refresh, String access) throws IOException {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("access_token", access);
        responseBody.put("refresh_token", refresh);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseBody);
    }

    /**
     * Generate view for access token Error
     *
     * @param response HTTP response for output
     * @param e        exception
     * @throws IOException error handling
     */
    public static void TokenErrorResponse(HttpServletResponse response, Exception e) throws IOException {
        response.setHeader("error", e.getMessage());
        response.setStatus(FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put("error_message", e.getMessage());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }


    @PostMapping("/signout")
    public ResponseEntity<?> logout(@RequestBody User request) {
        try {
            System.out.println(request);
            return new ResponseEntity<>(new User(), HttpStatus.OK);
        } catch (ConstraintViolationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * Get User account by id endpoint
     *
     * @param id String user id
     * @return ResponseEntity with user json
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> fetchUserProfile(@PathVariable String id) {
        try {
            return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
        } catch (ConstraintViolationException | UserCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * @param code verification code received throw email
     * @return ResponseEntity with HTTP status
     */
    @GetMapping("/activate/{code}")
    public ResponseEntity<?> verifyUser(@PathVariable String code) {
        try {
            User verifiedUser = userService.verifyUser(code);
            return new ResponseEntity<>(verifiedUser, HttpStatus.OK);
        } catch (ConstraintViolationException | UserCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * Update user account
     *
     * @param id      String user id
     * @param request json String with new data
     * @return ResponseEntity with updated {@link User}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<?> modifyUser(@PathVariable String id, @RequestBody String request) {
        try {
            User verifiedUser = userService.modifyUser(id, request);
            return new ResponseEntity<>(verifiedUser, HttpStatus.OK);
        } catch (ConstraintViolationException | UserCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * delete user from db
     *
     * @param id user String ID
     * @return String ResponseEntity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(String.format("user with id %s was successfully removed from database", id), HttpStatus.OK);
        } catch (UserCollectionException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    /**
     * Get All usernames in use
     *
     * @return list of Stings
     */
    @GetMapping("/")
    public ResponseEntity<List<String>> fetchUsernames() {
        return new ResponseEntity<List<String>>(userService.getUsernameList(), HttpStatus.OK);
    }

}