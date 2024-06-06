package backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerNewUser(User newUser) {
        if (userRepository.findByUsername(newUser.getUsername()) != null) {
            // TODO: 这里不该抛出异常
            throw new IllegalStateException("Username already exists");
        }
        return userRepository.save(newUser);
    }

    public boolean loginUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    public User getUserByID(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // public void addTripPreference(Long userID, String tripPreference) {
    //     User user = userRepository.findById(userID).orElse(null);
    //     if (user != null) {
    //         user.addTripPreference(tripPreference);
    //         userRepository.save(user);
    //     }
    // }

    // public void removeTripPreference(Long userID, String tripPreference) {
    //     User user = userRepository.findById(userID).orElse(null);
    //     if (user != null) {
    //         user.removeTripPreference(tripPreference);
    //         userRepository.save(user);
    //     }
    // }

    public void deleteUser(Long userID) {
        User user = userRepository.findById(userID).orElse(null);
        if (user != null) {
            userRepository.delete(user);
        }
    }
}

