package com.sjsu.miaas.security;

import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.sjsu.miaas.domain.User;
import com.sjsu.miaas.repository.AuthorityRepository;
import com.sjsu.miaas.repository.UserRepository;
import com.sjsu.miaas.web.rest.dto.UserDTO;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
@Component
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	
	@Inject
	 UserRepository userRepo;
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_OK);
        User u = userRepo.getUserByLogin(request.getParameter("j_username"));
        
        UserDTO udto = new UserDTO(u.getLogin(), u.getPassword(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getLangKey(), null);
        String user = new Gson().toJson(udto);
        response.getWriter().write(user);
    }
}
