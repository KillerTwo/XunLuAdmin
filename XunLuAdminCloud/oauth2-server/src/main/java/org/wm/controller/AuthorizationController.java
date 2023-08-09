/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wm.controller;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;



import org.wm.commons.utils.StringUtils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Joe Grandja
 * @since 0.0.1
 */
@RequestMapping("/oauth2")
@Controller
public class AuthorizationController {


	@GetMapping(value = "/login")
	public String authorizationCodeGrant(Model model, HttpServletRequest request) {
		// var err = model.getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		var error = request.getSession().getAttribute("SPRING_SECURITY_LAST_EXCEPTION");
		if (StringUtils.isNotNull(error)) {
			if(UsernameNotFoundException.class.isAssignableFrom(error.getClass()) && ((UsernameNotFoundException) error).getMessage().equals("validate code error")) {
				model.addAttribute("error", "验证码错误");
			} else {
				model.addAttribute("error", "用户名或密码错误");
			}
		}
		return "loginPage";
	}
}
