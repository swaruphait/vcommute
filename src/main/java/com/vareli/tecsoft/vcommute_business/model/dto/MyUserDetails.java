package com.vareli.tecsoft.vcommute_business.model.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vareli.tecsoft.vcommute_business.model.SubscriptionData;
import com.vareli.tecsoft.vcommute_business.model.SuperCompany;
import com.vareli.tecsoft.vcommute_business.model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MyUserDetails implements UserDetails {
    private User user;
    private Optional<SubscriptionData> subcriptionData;
    private Optional<SuperCompany> company;

    public MyUserDetails(User user, Optional<SubscriptionData> subcriptionData, Optional<SuperCompany> company) {
        this.user = user;
        this.subcriptionData = subcriptionData;
        this.company = company;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRole()));
        if (subcriptionData.isPresent()) {
            if (subcriptionData.get().isCommuteAccess()) {
                authorities.add(new SimpleGrantedAuthority("COMMUTEACCESS"));
            }
            if (subcriptionData.get().isFinanceAccess()) {
                authorities.add(new SimpleGrantedAuthority("FINNACEACCESS"));
            }
            if (subcriptionData.get().isAttendaceAccess()) {
                authorities.add(new SimpleGrantedAuthority("ATTENDANCEACCESS"));
            }
            if (subcriptionData.get().isLeaveAccess()) {
                authorities.add(new SimpleGrantedAuthority("LEAVEACCESS"));
            }
            if (subcriptionData.get().isBiometricAttend()) {
                authorities.add(new SimpleGrantedAuthority("BIOMATRICACCESS"));
            }
            if (subcriptionData.get().getAddNoOfCompany() > 1) {
                authorities.add(new SimpleGrantedAuthority("SUBCOMPANY"));
            }
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }

    public Integer getSuperCompanyId() {
        return user.getSuperCompanyId();
    }

    public Integer getCompanyId() {
        return user.getCompanyId();
    }

}
