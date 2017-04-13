package com.scu.hufu.config;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.scu.hufu.enums.ResponseEnum;
import com.scu.hufu.exception.ServerExpection;
import com.scu.hufu.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.GenericFilterBean;


/*
    为所有的/token/*的请求设定过滤器
 */
public class JwtFilter extends GenericFilterBean {

	@Value("${serverSecret}")
	private String SERVER_SECRET;

	public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException {

		final HttpServletRequest request = (HttpServletRequest) req;
		final HttpServletResponse response = (HttpServletResponse) res;
		final String authHeader = request.getHeader("authorization");


			if (authHeader == null || !authHeader.startsWith("Bearer ")) {
				throw new ServerExpection(ResponseEnum.MISS_TOKEN);
			}

			final String token = authHeader.substring(7);

			try {

				//验证是不是我的服务器签名的token
				Algorithm algorithm = Algorithm.HMAC256(SERVER_SECRET);
				JWTVerifier verifier = JWT.require(algorithm)
						.withIssuer("tianff.net")
						.build(); //Reusable verifier instance
				DecodedJWT jwt = verifier.verify(token);

				if (jwt.getExpiresAt().before(new Date(System.currentTimeMillis()))){
					errReturn(res,ResponseEnum.OVER_TIME_TOKEN);
					return;
				}

				chain.doFilter(req, res);

			} catch (final JWTVerificationException e) {
				errReturn(res,ResponseEnum.TOKEN_INVALID);
			}
	}

	public void errReturn(ServletResponse servletResponse,ResponseEnum e) throws  IOException{
		servletResponse.setContentType("application/json;charset=utf-8");
		String res=new Gson().toJson(ResponseUtil.error(e));
		servletResponse.getWriter().append(res);
	}

}
