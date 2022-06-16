package com.openai.board;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.openai.board.dto.MemberDto;
import com.openai.service.MemberService;

@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private MemberService mServ;
	
	@GetMapping("/")
	public String home() {
		logger.info("home()");
		/*
		logger.debug("debug msg");
		logger.info("info msg");
		logger.warn("warn msg");
		logger.error("error msg");
		*/
		return "home";
	}
	
	@GetMapping("/loginFrm")
	public String loginFrm() {
		logger.info("loginFrm()");
		
		return "loginFrm";
	}
	
	@PostMapping("/loginProc")
	public String loginProc(MemberDto mem, HttpSession session,
			RedirectAttributes rttr) {
		logger.info("loginProc()");
		
		String view = mServ.loginProc(mem, session, rttr);
		
		return view;
	}
	
	@GetMapping("/joinFrm")
	public String joinFrm() {
		logger.info("joinFrm()");
		
		return "joinFrm";
	}
	
	@GetMapping(value = "/idCheck", 
			produces = "application/text; charset=UTF-8")
	@ResponseBody
	public String idCheck(String mid) {
		logger.info("idCheck() - mid : " + mid);
		
		String res = mServ.idCheck(mid);
		//DB에 같은 아이디가 있는지 여부 확인
		
		return res;
	}
	
	@PostMapping("/memInsert")
	public String memberJoin(MemberDto member, 
			RedirectAttributes rttr) {
		logger.info("memberJoin()");
		
		String view = mServ.memberInsert(member, rttr);
		
		return view;
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		logger.info("logout()");
		
		String view = mServ.logout(session);
		
		return view;
	}
}//class end



