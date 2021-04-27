package com.example.loginservice.control;

import com.example.loginservice.auth.jwt.JwtProvider;
import com.example.loginservice.common.Constants;
import com.example.loginservice.common.Response;
import com.example.loginservice.dto.TokenDTO;
import com.example.loginservice.entity.Role;
import com.example.loginservice.enums.Permission;
import com.example.loginservice.repository.RoleRepository;
import com.example.loginservice.repository.UserRepository;
import com.example.loginservice.service.RoleService;
import com.example.loginservice.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.User;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Created by NhanNguyen on 4/26/2021
 *
 * @author: NhanNguyen
 * @date: 4/26/2021
 */
@RestController
@RequestMapping("/oauth")
@CrossOrigin
public class OauthControl {
    @Value("${google.clientId}")
    private String googleClientId;

    @Value("${secretPsw}")
    private String secretPsw;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/google")
    public @ResponseBody
    Response google(@RequestBody TokenDTO tokenDTO) throws IOException {
        final NetHttpTransport transport = new NetHttpTransport();
        final JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                .setAudience(Collections.singletonList(googleClientId));

        final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenDTO.getValue());
        final GoogleIdToken.Payload payload = googleIdToken.getPayload();
        com.example.loginservice.entity.User entity = new com.example.loginservice.entity.User();
        if (userRepository.existsByEmail(payload.getEmail())) {
            entity = userRepository.findUserByEmail(payload.getEmail()).get();
        } else {
            entity = userService.registerUser(payload.getEmail());
        }
        if (Objects.nonNull(entity)) {
            TokenDTO token = login(entity);
            return Response.success().withData(token);
        }
        return Response.error(Constants.RESPONSE_CODE.ERROR);
    }

    @PostMapping("/facebook")
    public @ResponseBody
    Response facebook(@RequestBody TokenDTO tokenDTO) throws IOException {
        Facebook facebook = new FacebookTemplate(tokenDTO.getValue());
        final String[] fields = {"email", "picture"};
        User user = facebook.fetchObject("me", User.class,fields);
        com.example.loginservice.entity.User entity = new com.example.loginservice.entity.User();
        if (userRepository.existsByEmail(user.getEmail())) {
            entity = userRepository.findUserByEmail(user.getEmail()).get();
        } else {
            entity = userService.registerUser(user.getEmail());
        }
        if (Objects.nonNull(entity)) {
            TokenDTO token = login(entity);
            return Response.success().withData(token);
        }
        return Response.error(Constants.RESPONSE_CODE.ERROR);
    }

    private TokenDTO login(com.example.loginservice.entity.User user) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getEmail(), "admin"));
        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt = jwtProvider.generateToken(auth);
        TokenDTO tokenDTO = new TokenDTO();
        tokenDTO.setValue(jwt);
        return tokenDTO;
    }

}
