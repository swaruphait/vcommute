package com.vareli.tecsoft.vcommute_business.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("login");
		registry.addViewController("/login").setViewName("login");
		registry.addViewController("/logout").setViewName("login");

        // *********************FOR COMPANY*****************************
		registry.addViewController("/homeCompany").setViewName("parent/home");
       	registry.addViewController("/companyManagment").setViewName("parent/company");
        registry.addViewController("/superadmin").setViewName("parent/superadmin");
        registry.addViewController("/planMaster").setViewName("parent/planmaster");
        registry.addViewController("/countrymaster").setViewName("parent/county");
        registry.addViewController("/statemaster").setViewName("parent/state");
        registry.addViewController("/citymaster").setViewName("parent/city");

        // *********************FOR SUPERADMIN*****************************
        registry.addViewController("/homeSuperAdmin").setViewName("superadmin/home");
        registry.addViewController("/subcompany").setViewName("superadmin/subcompany");
        registry.addViewController("/usermanage").setViewName("superadmin/usermaster");
        registry.addViewController("/customermaster").setViewName("superadmin/customer");
        registry.addViewController("/rolemaster").setViewName("superadmin/role");
        registry.addViewController("/departmentmaster").setViewName("superadmin/department");       
        registry.addViewController("/designationmaster").setViewName("superadmin/designation");
        registry.addViewController("/holidaymaster").setViewName("superadmin/holiday");
        registry.addViewController("/leavemaster").setViewName("superadmin/leave");
        registry.addViewController("/approvalmaster").setViewName("superadmin/approval");       
        registry.addViewController("/grademaster").setViewName("superadmin/grade");
        registry.addViewController("/clustermaster").setViewName("superadmin/cluster");
        registry.addViewController("/transportdesc").setViewName("superadmin/transportdesc");
        registry.addViewController("/modemaster").setViewName("superadmin/mode");
        registry.addViewController("/subcription").setViewName("superadmin/subcription");


        // *********************FOR ADMIN*****************************
        registry.addViewController("/home").setViewName("admin/home");
        registry.addViewController("/customer").setViewName("admin/customer");
        registry.addViewController("/role").setViewName("admin/role");
        registry.addViewController("/department").setViewName("admin/department");       
        registry.addViewController("/designation").setViewName("admin/designation");
        registry.addViewController("/holiday").setViewName("admin/holiday");
        registry.addViewController("/leave").setViewName("admin/leave");
        registry.addViewController("/approval").setViewName("admin/approval");       
        registry.addViewController("/grade").setViewName("admin/grade");
        registry.addViewController("/cluster").setViewName("admin/cluster");
        registry.addViewController("/transport").setViewName("admin/transportdesc");
        registry.addViewController("/mode").setViewName("admin/mode");
        registry.addViewController("/userreg").setViewName("admin/userreg");
        registry.addViewController("/viewuser").setViewName("admin/viewuser");
        registry.addViewController("/commute_approval").setViewName("admin/commuteappvl");
        registry.addViewController("/commute_disapproved").setViewName("admin/commutedisappvl");
        registry.addViewController("/report_attendance").setViewName("admin/reportattend");
        registry.addViewController("/commute_report").setViewName("admin/reportcommute");
        registry.addViewController("/leave_report").setViewName("admin/reportleave");
        registry.addViewController("/leave_approval").setViewName("admin/leaveappvl");

        // *********************FOR HOD*****************************
        
        registry.addViewController("/userlist").setViewName("hod/userlist");
        registry.addViewController("/report_attendance_hod").setViewName("hod/reportattendhod");
        registry.addViewController("/commute_report_hod").setViewName("hod/reportcommutehod");
        registry.addViewController("/leave_report_hod").setViewName("hod/reportleavehod");

        // *********************FOR FINANCE*****************************
          registry.addViewController("/claimsettlement").setViewName("finance/claimsettlement");
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:uploads/");
    }
}
