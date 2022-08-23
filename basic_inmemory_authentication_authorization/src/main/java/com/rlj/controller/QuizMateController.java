package com.rlj.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuizMateController
{

	@GetMapping("/")
	public String rootEndPoint()
	{
		return "<h1>Welcome</h1>";
	}

	@GetMapping("/user")
	public String userEndPoint()
	{
		return "<h1>Welcome User</h1>";
	}


	@GetMapping("/admin")
	public String adminEndPoint()
	{
		return "<h1>Welcome Admin</h1>";
	}
}
