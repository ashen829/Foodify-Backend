package com.ashen.Foodify.controller;

import com.ashen.Foodify.config.JwtProvider;
import com.ashen.Foodify.model.Cart;
import com.ashen.Foodify.model.USER_ROLE;
import com.ashen.Foodify.model.User;
import com.ashen.Foodify.repository.CartRepository;
import com.ashen.Foodify.repository.UserRepository;
import com.ashen.Foodify.request.LoginRequest;
import com.ashen.Foodify.response.Authresponse;
import com.ashen.Foodify.service.CustomerUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    private CartRepository cartRepository;

    @PostMapping("/signup")
    public ResponseEntity<Authresponse> createUserHandler(@RequestBody User user) throws Exception {
        User isEmailExist=userRepository.findByEmail(user.getEmail());
        if(isEmailExist!=null){
            throw new Exception("Email is already used with another account");
        }
        User createdUser = new User();
        createdUser.setEmail(user.getEmail());
        createdUser.setFullName(user.getFullName());
        createdUser.setRole(user.getRole());
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
        User saveUser = userRepository.save(createdUser);

        Cart cart = new Cart();
        cart.setCustomer(saveUser);
        cartRepository.save(cart);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        Authresponse authresponse=new Authresponse();
        authresponse.setJwt(jwt);
        authresponse.setMessage("Register Success");
        authresponse.setRole(saveUser.getRole());

        return new ResponseEntity<>(authresponse, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<Authresponse>signin(@RequestBody LoginRequest loginRequest){
        String username= loginRequest.getEmail();
        String password= loginRequest.getPassword();

        Authentication authentication= authenticate(username,password);

        Collection<? extends GrantedAuthority>authorities=authentication.getAuthorities();
        String role=authorities.isEmpty()?null:authorities.iterator().next().getAuthority();

        String jwt = jwtProvider.generateToken(authentication);

        Authresponse authresponse=new Authresponse();
        authresponse.setJwt(jwt);
        authresponse.setMessage("Login Success");
        authresponse.setRole(USER_ROLE.valueOf(role));

        return new ResponseEntity<>(authresponse,HttpStatus.OK);
    }

    private Authentication authenticate(String username, String password){
        UserDetails userDetails=customerUserDetailsService.loadUserByUsername(username);

        if(userDetails==null){
            throw new BadCredentialsException("Invalid username...");
        }
        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new BadCredentialsException("Invalid  Password...");
        }
        return new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
    }
}
