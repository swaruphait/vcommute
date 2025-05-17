package com.vareli.tecsoft.vcommute_business.config;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

import com.vareli.tecsoft.vcommute_business.model.LoginHistory;
import com.vareli.tecsoft.vcommute_business.model.dto.MyUserDetails;
import com.vareli.tecsoft.vcommute_business.repository.LoginHistoryRepository;
import com.vareli.tecsoft.vcommute_business.repository.SubscriptionDataRepository;

@Component
public class MySuccessHandler implements AuthenticationSuccessHandler{

    @Autowired
	LoginHistoryRepository loginHistoryRepository;

	@Autowired
	SubscriptionDataRepository subscriptionDataRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
       Authentication authentication) throws IOException, ServletException {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		MyUserDetails user =(MyUserDetails) auth.getPrincipal();
		if (user.getAuthorities().contains(new SimpleGrantedAuthority("VARELI"))) {
			String redirectUrl = "/homeCompany";
			String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
			String ipAddress = request.getRemoteAddr(); // Get client IP address
			LoginHistory login = new LoginHistory();
			login.setUserId(user.getUser().getId());
			login.setName(user.getUser().getName());
			login.setSessionId(sessionId);
			login.setLoginTime(LocalDateTime.now());
			login.setCompanyId(user.getUser().getCompanyId());
			login.setSuperCompanyId(user.getUser().getSuperCompanyId());
			login.setIpAddress(ipAddress);
			loginHistoryRepository.save(login);
			new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
		}else if(user.getAuthorities().contains(new SimpleGrantedAuthority("SUPERADMIN"))) {

			String redirectUrl = "/homeSuperAdmin";
			String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
			String ipAddress = request.getRemoteAddr(); // Get client IP address
			LoginHistory login = new LoginHistory();
			login.setUserId(user.getUser().getId());
			login.setName(user.getUser().getName());
			login.setSessionId(sessionId);
			login.setLoginTime(LocalDateTime.now());
            login.setCompanyId(user.getUser().getCompanyId());
			login.setSuperCompanyId(user.getUser().getSuperCompanyId());
			login.setIpAddress(ipAddress);
			loginHistoryRepository.save(login);
			new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
		}else if(user.getAuthorities().stream()
				.anyMatch(authority -> authority.getAuthority().equals("ADMIN")
						|| authority.getAuthority().equals("HOD")
						|| authority.getAuthority().equals("FINANCE")
						|| authority.getAuthority().equals("BACKOFFICE"))) {
			if (subscriptionDataRepository.existsByStatusAndSuperCompanyId(true,user.getUser().getSuperCompanyId())) {
				String redirectUrl = "/home";
				String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
			 String ipAddress = request.getRemoteAddr(); // Get client IP address
				LoginHistory login = new LoginHistory();
				login.setUserId(user.getUser().getId());
				login.setName(user.getUser().getName());
				login.setSessionId(sessionId);
				login.setLoginTime(LocalDateTime.now());
                login.setCompanyId(user.getUser().getCompanyId());
                login.setSuperCompanyId(user.getUser().getSuperCompanyId());
			    login.setIpAddress(ipAddress);
				loginHistoryRepository.save(login);
				new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);

			} else {
				String redirectUrl = "/errorSubscriptionEnd";
				new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
				return;
			}
			
		}else {
			String redirectUrl = "/403";
			new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
			return;
		}
    }
    
}
