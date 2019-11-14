package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.rabbitmqSend.MQSender;

@RestController
@RequestMapping(value="mqTest")
public class MQTestController {

	@Autowired
	private MQSender mqSender;
	
	@RequestMapping(value="/sendDirectMsg", method=RequestMethod.POST)  
	public ModelAndView sendDirectMsg() { 
		ModelAndView mv = new ModelAndView();
		mqSender.sendDirectMsg("不露声色~~~~~~~~~~~~~~~~~~~"+Math.random());
		mv.addObject("msg","消息已发送~~~~~~~~~~~~~~~");
		mv.setViewName("result");
		return mv;  
	}  
	
	@RequestMapping(value="/sendTopicMsg", method=RequestMethod.POST)  
	public ModelAndView sendTopicMsg() { 
		ModelAndView mv = new ModelAndView();
		mqSender.sendTopicMsg("不露声色~~~~~~~~~~~~~~~~~~~"+Math.random());
		mv.addObject("msg","消息已发送~~~~~~~~~~~~~~~");
		mv.setViewName("result");
		return mv;  
	}  
	
	@RequestMapping(value="/sendFanoutMsg", method=RequestMethod.POST)  
	public ModelAndView sendFanoutMsg() { 
		ModelAndView mv = new ModelAndView();
		mqSender.sendFanoutMsg("不露声色~~~~~~~~~~~~~~~~~~~"+Math.random());
		mv.addObject("msg","消息已发送~~~~~~~~~~~~~~~");
		mv.setViewName("result");
		return mv;  
	}  
}
