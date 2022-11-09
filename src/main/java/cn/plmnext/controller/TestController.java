package cn.plmnext.controller;

import cn.plmnext.Session;
import com.teamcenter.hello.HomeFolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

	@Autowired
	private Session session;

	@RequestMapping("/hello")
	@ResponseBody
	public String testDemo()
	{
		return "Hello,world!";
	}

	@RequestMapping("/home")
	@ResponseBody
	public String getUser()
	{
		HomeFolder homeFolder = new HomeFolder();
		homeFolder.listHomeFolder(session.getUser());
		return "请查看后台打印数据.";
	}

}
