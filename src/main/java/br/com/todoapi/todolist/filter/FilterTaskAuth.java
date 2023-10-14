package br.com.todoapi.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.todoapi.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Para spring gerenciar.
@Component
public class FilterTaskAuth extends OncePerRequestFilter {
    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                var servletPath = request.getServletPath();

                if (servletPath.startsWith("/tasks")) {
                    // Get auth
                    var auth = request.getHeader("Authorization");

                    var authEncoded = auth.substring("Basic".length()).trim();

                    byte[] authDecoded = Base64.getDecoder().decode(authEncoded);
                    var authString  = new String(authDecoded);
                    
                    String[] credentials = authString.split(":");

                    String username = credentials[0];
                    String password = credentials[1];

                    // Validate user || pass
                    var user = this.userRepository.findByUsername(username);

                    if (user == null) {
                        response.sendError(401, "Unauthorized");
                    } else {
                        var passVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
                        if (passVerify.verified) {
                            request.setAttribute("idUser", user.getId());
                            filterChain.doFilter(request, response);
                        } else {
                            response.sendError(401, "Unauthorized");
                        }
                    }
                } else {
                    filterChain.doFilter(request, response);
                }
    }
}
