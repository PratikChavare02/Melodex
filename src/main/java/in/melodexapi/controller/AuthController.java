package in.melodexapi.controller;

import in.melodexapi.Service.AppUserDetailsSService;
import in.melodexapi.Service.UserService;
import in.melodexapi.document.User;
import in.melodexapi.dto.AuthRequest;
import in.melodexapi.dto.AuthResponse;
import in.melodexapi.dto.RegisterRequest;
import in.melodexapi.dto.UserResponse;
import in.melodexapi.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsSService userDetailsService;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request){

        try {
//            autheticate user
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
            // load the user details
            UserDetails userDetails= userDetailsService.loadUserByUsername(request.getEmail());
            User existinguser=userService.findByEmail(request.getEmail());

            //generating JWT tokens
            // to generate need to create a utility class
            String token=jwtUtil.generateToken(userDetails,existinguser.getRole().name());

            return ResponseEntity.ok(new AuthResponse(token,request.getEmail(),existinguser.getRole().name()));

        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body("Email/password is incorrect");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request){
        try {
            UserResponse response=userService.registerUser(request);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
