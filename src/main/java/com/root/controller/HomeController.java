package com.root.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.root.entity.User;
import com.root.repository.UserRepo;
import com.root.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepo userRepo;

	@ModelAttribute
	public void commonUser(Principal p, Model m) {

		if (p != null) {
			String email = p.getName();
			User user = userRepo.findByEmail(email);
			m.addAttribute("user", user);
		}
	}

	@GetMapping("/")
	public String indexPage() {

		return "index";
	}

	/*
	 * @GetMapping("/user/home") public String homePage() {
	 * 
	 * return "home"; }
	 * 
	 * 
	 * @GetMapping("/user/profile") public String profilePage(Principal p, Model m)
	 * {
	 * 
	 * String email = p.getName();
	 * 
	 * User user = userRepo.findByEmail(email);
	 * 
	 * m.addAttribute("user", user);
	 * 
	 * return "profile"; }
	 */

	@GetMapping("/register")
	public String registerPage() {

		return "register";
	}

	@GetMapping("/signin")
	public String aboutPage() {

		return "login";
	}

	@PostMapping("/userCreated")
	public String saveUser(@ModelAttribute User user, HttpSession session, HttpServletRequest request) {

		String url = request.getRequestURL().toString();

		// System.out.println(url); http://localhost:8080/userCreated
		url = url.replace(request.getServletPath(), " ");
		/*
		 * System.out.println(url);
		 * http://localhost:8080/verify?code=55dsf4dsfdsf4sdfv4sd5
		 */

		User u = userService.saveUser(user, url);

		if (u != null) { // System.out.println("user Save sucessfully");
			session.setAttribute("msg", "Register Successfully");
		} else { //
			System.out.println("Error in Server");
			session.setAttribute("msg", "something wrong on server");
		}

		return "redirect:/register";
	}

	@GetMapping("/verify")
	public String verifyAccount(@Param("code") String code, Model m) {

		boolean f = userService.verifyAccount(code);

		if (f) {
			m.addAttribute("msg", "Successfully Your account is verified");
		}else {
			m.addAttribute("msg","may be your verification code is incorrect or already verify");
		}

		return "message";
	}
}
