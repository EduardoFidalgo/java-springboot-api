package br.com.todoapi.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private IUserRepository userRepository;

    @GetMapping("/status")
    public ResponseEntity status() {
        return ResponseEntity.status(HttpStatus.FOUND).body("API Working!");
    }

    @GetMapping("/all")
    public ResponseEntity all(@RequestBody UserModel userModel) {
        var allUsers = this.userRepository.findAll();

        if (allUsers == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users were found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(allUsers);
    }

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        // System.out.println(userModel.getUsername());
        var existentUser = this.userRepository.findByUsername(userModel.getUsername());
        
        if (existentUser != null) {
            // System.out.println("Usuário já existe");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User existent.");
        }

        var passwordHash = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());
        
        userModel.setPassword(passwordHash);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
    }
}
