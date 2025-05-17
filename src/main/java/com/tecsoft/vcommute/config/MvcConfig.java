package com.tecsoft.vcommute.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/resetpass").setViewName("resetpass");
        registry.addViewController("/forgetpass").setViewName("forgetpass");
        registry.addViewController("/403").setViewName("403");
        registry.addViewController("/privacy").setViewName("privacy");
        
        // ***************************ADMIN******************************************
        registry.addViewController("/home").setViewName("admin/home");
        registry.addViewController("/viewuser").setViewName("admin/viewuser");
        registry.addViewController("/userreg").setViewName("admin/userreg");
        registry.addViewController("/customer").setViewName("admin/customer");
        registry.addViewController("/department").setViewName("admin/department");
        registry.addViewController("/city").setViewName("admin/city");
        registry.addViewController("/cluster").setViewName("admin/cluster");
        registry.addViewController("/grade").setViewName("admin/grade");
        registry.addViewController("/financeAuthority").setViewName("admin/financeAuthority");
        registry.addViewController("/role").setViewName("admin/role");
        registry.addViewController("/transport").setViewName("admin/transport");
        registry.addViewController("/mode").setViewName("admin/mode");
        registry.addViewController("/approval").setViewName("admin/approval");
        registry.addViewController("/commutedata").setViewName("admin/commutedata");
        registry.addViewController("/incompleteactivity").setViewName("admin/incompleteactivity");
        registry.addViewController("/report").setViewName("admin/report");
        registry.addViewController("/disapproved").setViewName("admin/disapproved");

        // ***************************HOD******************************************
        registry.addViewController("/homehod").setViewName("hod/homehod");
        registry.addViewController("/reportAuthority").setViewName("hod/reportAuthority");
        registry.addViewController("/incompleteDataAuthority").setViewName("hod/incompleteCommute");
        registry.addViewController("/attendance").setViewName("hod/attendance");
        registry.addViewController("/viewlistuser").setViewName("hod/viewuser");

        // ***************************FINACE******************************************
        registry.addViewController("/homefinance").setViewName("finance/homefinance");
        registry.addViewController("/disapproved_finance").setViewName("finance/disapprove");
        registry.addViewController("/finance_approve").setViewName("finance/finance_approve");
        registry.addViewController("/paidreport").setViewName("finance/paidreport");
        registry.addViewController("/commute_advance").setViewName("finance/commute_advance");
        registry.addViewController("/payable_commute_amount").setViewName("finance/payable_commute");
        registry.addViewController("/attendance_approval").setViewName("hr/attendance_approval");
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");
        registry.addResourceHandler("/uploadfile/**").addResourceLocations("file:c:/upload/");
    }

}
