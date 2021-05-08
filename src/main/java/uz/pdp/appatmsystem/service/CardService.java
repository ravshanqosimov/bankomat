//package uz.pdp.appatmsystem.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import uz.pdp.appatmsystem.entity.Card;
//import uz.pdp.appatmsystem.entity.User;
//import uz.pdp.appatmsystem.payload.ApiResponse;
//import uz.pdp.appatmsystem.payload.CardDto;
//import uz.pdp.appatmsystem.repository.CardRepository;
//import uz.pdp.appatmsystem.security.JwtProvider;
//
//import java.util.Optional;
//
//@Service
//public class CardService implements UserDetailsService {
//    @Autowired
//    AuthenticationManager authenticationManager;
//    @Autowired
//    JwtProvider jwtProvider;
//    @Autowired
//    CardRepository cardRepository;
//
//    public ApiResponse loginCard(CardDto cardDto) {
//        try {
//            Authentication authenticate =
//                    authenticationManager.authenticate(
//                            new UsernamePasswordAuthenticationToken(cardDto.getNumber(),
//                                    cardDto.getPassword()));
//            Card card = (Card) authenticate.getPrincipal();
//            String token = jwtProvider.generateToken(card.getNumber());
//            return new ApiResponse("token", true, token);
//        } catch (BadCredentialsException e) {
//            return new ApiResponse("parol xato", false);
//        }
//    }
//
//
//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        Optional<Card> optionalCard = cardRepository.findByNumber(s);
//        if (optionalCard.isPresent())
//            return optionalCard.get();
//        else throw new UsernameNotFoundException(s + " topilmadi");
//    }
//}
