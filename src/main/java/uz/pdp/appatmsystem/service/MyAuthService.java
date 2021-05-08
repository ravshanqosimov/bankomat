package uz.pdp.appatmsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.appatmsystem.entity.Card;
import uz.pdp.appatmsystem.entity.User;
import uz.pdp.appatmsystem.payload.ApiResponse;
import uz.pdp.appatmsystem.payload.CardDto;
import uz.pdp.appatmsystem.payload.LoginDto;
import uz.pdp.appatmsystem.repository.UserRepository;
import uz.pdp.appatmsystem.security.JwtProvider;

import java.util.Optional;

@Service
public class MyAuthService implements UserDetailsService {
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    UserRepository userRepository;
    public ApiResponse login(LoginDto loginDto) {
        try {
            Authentication authenticate =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
                                    loginDto.getPassword()));
            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(user.getUsername());
            return new ApiResponse("token", true, token);
        } catch (BadCredentialsException e) {
            return new ApiResponse("parol yoki login xato", false);
        }
    }

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(s);
        if (optionalUser.isPresent())
            return optionalUser.get();
        else throw new UsernameNotFoundException(s + " topilmadi");
    }

}
