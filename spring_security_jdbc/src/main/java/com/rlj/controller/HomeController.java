package com.rlj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController
{

	@GetMapping("/")
	public String home()
	{
		return "<h1>Welocme</h1>";
	}

	@GetMapping("/user")
	public String homeUser()
	{
		return "<h1>Welocme User</h1>";
	}

	@GetMapping("/admin")
	public String homeAdmin()
	{
		return "<h1>Welocme Admin</h1>";
	}

}
