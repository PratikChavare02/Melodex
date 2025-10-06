package in.melodexapi.Service;

import in.melodexapi.document.User;
import in.melodexapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailsSService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User existinigUser = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found for the email :" + email));
        return new org.springframework.security.core.userdetails.User(existinigUser.getEmail(), existinigUser.getPassword(), getAuthorities(existinigUser));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User existinigUser) {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+ existinigUser.getRole().name()));
    }
}
