package com.redis.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.redis.common.JSONP;
import com.redis.entity.Member;
import com.redis.services.IMember;

/**
 *
 * @author liyc
 * @date 2017年8月14日 下午2:42:46
 */
@Controller
@RequestMapping("/member")
public class MemberCtrl {
	@Autowired
	public IMember memberService;
	private Logger logger = Logger.getLogger(this.getClass());

	@ResponseBody
	@RequestMapping("/add")
	public JSONP<String> addMember(
			@RequestParam(value = "callback", required = false) String callback, Member member) {
		Map<String, Object> map = new HashMap<String, Object>();
		logger.info(member);
		map.put("message", "成功添加数据到库," + member);
		memberService.add(member);
		return new JSONP<String>(callback, "添加成功");
	}

	@ResponseBody
	@RequestMapping("/{id:\\d+}/query")
	public JSONP<Member> queryMember(
			@RequestParam(value = "callback", required = false) String callback, @PathVariable("id") String id) {
		JSONP<Member> jsonp = new JSONP<>(callback, "查询成功", true);
		logger.info(id);
		Member member = this.memberService.get(id);
		if (null != member) {
			jsonp.setCont(member);
			logger.info(member.getNickname());
		} else {
			return new JSONP<>(callback, "查询失败", false);
		}

		jsonp.setCont(member);
		return jsonp;
	}
}
