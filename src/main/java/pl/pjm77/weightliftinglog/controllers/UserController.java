package pl.pjm77.weightliftinglog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import pl.pjm77.weightliftinglog.models.User;
import pl.pjm77.weightliftinglog.validators.UpdatePasswordValidator;
import pl.pjm77.weightliftinglog.services.UserService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;

import static pl.pjm77.weightliftinglog.services.UserService.getLoggedInUserName;

@Controller
public class UserController {

    private final UserService userService;
    private final UpdatePasswordValidator updatePasswordValidator;

    @Autowired
    public UserController(UserService userService,
                          UpdatePasswordValidator updatePasswordValidator) {
        this.userService = userService;
        this.updatePasswordValidator = updatePasswordValidator;
    }

    @InitBinder("user")
    protected void initBinder(final WebDataBinder binder) {
        binder.addValidators(updatePasswordValidator);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @RequestMapping("/user")
    public String user(Model model) {

        // Get the name of the user who is currently logged in and check for his admin rights

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userName;
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
        if (principal instanceof UserDetails) {
            userName = ((UserDetails) principal).getUsername();
            authorities = ((UserDetails) principal).getAuthorities();
        } else {
            userName = principal.toString();
        }
        for (GrantedAuthority grantedAuthority : authorities) {
            if (("ROLE_ADMIN").equals(grantedAuthority.getAuthority())) {
                model.addAttribute("adminRights", true);
            }
        }

        // pass the user name to view

        model.addAttribute("userGreeting", "Hello " + userName + "!");
        model.addAttribute("page", "fragments.html :: user-panel");
        return "home";
    }

    @GetMapping("/user/update")
    public String editUserDetails(Model model) {
        String userName = getLoggedInUserName();
        User user = userService.findUserByName(userName);
//        user.setPassword("");
        model.addAttribute("user", user);
        model.addAttribute("page", "fragments.html :: update-user");
        return "home";
    }

    @PostMapping("/user/update")
    public String registerPost(@Valid @ModelAttribute("user") User user,
                               BindingResult bindingResult, Model model) {
        model.addAttribute("page", "fragments.html :: update-user");
        if (!bindingResult.hasErrors()) {
            try {
                userService.saveUser(user);
                model.addAttribute("page", "fragments.html :: update-user-success");
            }catch(DataIntegrityViolationException e){
                model.addAttribute
                        ("emailExists", "    This email already exists in our database!");
            }
        }
        return "home";
    }
}