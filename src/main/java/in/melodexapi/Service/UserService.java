package in.melodexapi.Service;

import in.melodexapi.document.User;
import in.melodexapi.dto.RegisterRequest;
import in.melodexapi.dto.UserResponse;
import in.melodexapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse registerUser(RegisterRequest request){

        // check if user is already exists
        if(userRepository.existsByEmail(request.getEmail())){
            throw  new RuntimeException("email alredy exists");
        }
        // create a new user'
        User newUser=User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .build();

        userRepository.save(newUser);
        return UserResponse.builder()
                .id(newUser.getId())
                .email(newUser.getEmail())
                .role(UserResponse.Role.USER)
                .build();
    }

    //to generate JWt token we need user details thats why find user by email
    public User findByEmail(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
    }
}
