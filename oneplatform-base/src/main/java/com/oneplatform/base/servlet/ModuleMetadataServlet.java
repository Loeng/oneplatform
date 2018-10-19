/**
 * 
 */
package com.oneplatform.base.servlet;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.CharStreams;
import com.jeesuite.springweb.utils.WebUtils;
import com.oneplatform.base.util.ApiInfoHolder;

/**
 * @description <br>
 * @author <a href="mailto:vakinge@gmail.com">vakin</a>
 * @date 2017年3月19日
 */
@WebServlet(urlPatterns = "/metadata", description = "获取模块元数据信息")
public class ModuleMetadataServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static String metadata;
	
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		JSONObject metadataJSON = new JSONObject();
		metadataJSON.put("apis", ApiInfoHolder.getApiInfos());
		try {
			Resource resource = ResourcePatternUtils.getResourcePatternResolver(new DefaultResourceLoader()).getResource("classpath:metadata.json");
			String contents = CharStreams.toString(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
			JSONObject jsonObject = JSON.parseObject(contents);
			jsonObject.keySet().forEach(key -> {
				metadataJSON.put(key, jsonObject.get(key));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		metadata = metadataJSON.toJSONString();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		WebUtils.responseOutJson(resp, metadata);
	}
	
	

	@Override
	public void destroy() {
		super.destroy();
	}
}
