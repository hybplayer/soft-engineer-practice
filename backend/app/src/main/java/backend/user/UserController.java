package backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import backend.user.request.UserRegisterRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRegisterRequest request) {
        User registeredUser = userService.registerNewUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(registeredUser);
    }

    @GetMapping("/login")
    public ResponseEntity<Long> loginUser(@RequestBody UserRegisterRequest request) {
        // return user id if login is successful
        if (userService.loginUser(request.getUsername(), request.getPassword())) {
            User user = userService.getUserByUsername(request.getUsername());
            return ResponseEntity.ok(user.getId());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{userID}")
    public ResponseEntity<User> getUser(@PathVariable Long userID) {
        User user = userService.getUserByID(userID);
        if (user == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PutMapping("/{userID}")
    public ResponseEntity<User> updateUser(@PathVariable Long userID, @RequestBody User user) {
        User existingUserByID = userService.getUserByID(userID);    
        User existingUserByUsername = userService.getUserByUsername(user.getUsername());

        if (existingUserByID == null) {
            return ResponseEntity.notFound().build();
        }

        if (existingUserByUsername != null && existingUserByUsername.getId().equals(userID)) {
            user.setId(userID);
            User updatedUser = userService.updateUser(user);
            return ResponseEntity.ok(updatedUser);
        }

        return ResponseEntity.badRequest().build();
    }

    @PatchMapping("/{userID}/addTripPreference")
    public ResponseEntity<User> addTripPreference(@PathVariable Long userID, @RequestBody String tripPreference) {
        User user = userService.getUserByID(userID);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        user.addTripPreference(tripPreference);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{userID}/removeTripPreference")
    public ResponseEntity<User> removeTripPreference(@PathVariable Long userID, @RequestBody String tripPreference) {
        User user = userService.getUserByID(userID);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        user.removeTripPreference(tripPreference);
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userID}")
    public ResponseEntity<User> deleteUser(@PathVariable Long userID) {
        User user = userService.getUserByID(userID);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(userID);
        return ResponseEntity.ok(user);
    }
}