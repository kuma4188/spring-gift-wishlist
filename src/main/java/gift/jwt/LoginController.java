package gift.jwt;

import gift.user.UserCreateForm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/auth")
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailsService jwtUserDetailsService;

    @PostMapping("/login")
    @ResponseBody
    public Map<String, String> login(@RequestBody UserCreateForm userCreateForm) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCreateForm.getUsername(), userCreateForm.getPassword()));
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(userCreateForm.getUsername());
            String token = jwtTokenProvider.createToken(userDetails);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            System.out.println("Generated Token: " + token); // 토큰 출력
            return response;
        } catch (AuthenticationException e) {
            System.out.println("실패!!!!!");
            throw new RuntimeException("Invalid login credentials");
        }
    }
}
