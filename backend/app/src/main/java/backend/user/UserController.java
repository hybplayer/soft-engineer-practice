package backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import backend.user.request.UserRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserRequest request) {
        User registeredUser = userService.registerNewUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(registeredUser);
    }

    @GetMapping("/login")
    public ResponseEntity<Long> loginUser(@RequestBody UserRequest request) {
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
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User updatedUser = userService.updateUser(user);
        return ResponseEntity.ok(updatedUser);
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
}