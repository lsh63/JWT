package org.xxx.security.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/*
 * JwtToken工具类
 * */
@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -3301605591108950415L;

	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_AUDIENCE = "aud";
	static final String CLAIM_KEY_CREATED = "iat";

	static final String AUDIENCE_UNKNOWN = "unknown";
	static final String AUDIENCE_WEB = "web";
	static final String AUDIENCE_MOBILE = "mobile";
	static final String AUDIENCE_TABLET = "tablet";

	@SuppressFBWarnings(value = "SE_BAD_FIELD", justification = "It's okay here")//忽略警告
	private Clock clock = DefaultClock.INSTANCE;//计时器

	@Value("${jwt.secret}")
	private String secret;//jwt密匙

	@Value("${jwt.expiration}")
	private Long expiration;//jwt有效期

	//根据token返回username
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	//根据token返回通行证发布日期（创建日期）
	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	//根据token返回通行证过期时间
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public String getAudienceFromToken(String token) {
		return getClaimFromToken(token, Claims::getAudience);
	}

	//返回链集合
	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	//返回所有链
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(secret)
				.parseClaimsJws(token)
				.getBody();
	}

	//token是否过期
	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);//token到期时间
		return expiration.before(clock.now());//到期时间是否before当前时间
	}

	//判断token创建时间是否在密码重置之前
	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	private String generateAudience(Device device) {
		String audience = AUDIENCE_UNKNOWN;
		if (device.isNormal()) {
			audience = AUDIENCE_WEB;
		} else if (device.isTablet()) {
			audience = AUDIENCE_TABLET;
		} else if (device.isMobile()) {
			audience = AUDIENCE_MOBILE;
		}
		return audience;
	}

	//忽略token的过期时间
	private Boolean ignoreTokenExpiration(String token) {
		String audience = getAudienceFromToken(token);
		return (AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience));
	}

	//生成token
	public String generateToken(UserDetails userDetails, Device device) {
		Map<String, Object> claims = new HashMap<>();
		return doGenerateToken(claims, userDetails.getUsername(), generateAudience(device));
	}

	//生成token操作方法
	private String doGenerateToken(Map<String, Object> claims, String subject, String audience) {
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate);

		System.out.println("Token创建时间:" + createdDate);
		System.out.println("生成Token:"+
				Jwts.builder()
		.setClaims(claims)
		.setSubject(subject)
		.setAudience(audience)
		.setIssuedAt(createdDate)
		.setExpiration(expirationDate)
		.signWith(SignatureAlgorithm.HS512, secret)
		.compact());

		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setAudience(audience)
				.setIssuedAt(createdDate)
				.setExpiration(expirationDate)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	//判断token是否可以刷新
	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = getIssuedAtDateFromToken(token);
		return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
				&& (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	//刷新token
	public String refreshToken(String token) {
		final Date createdDate = clock.now();
		final Date expirationDate = calculateExpirationDate(createdDate);

		final Claims claims = getAllClaimsFromToken(token);
		claims.setIssuedAt(createdDate);
		claims.setExpiration(expirationDate);

		return Jwts.builder()
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS512, secret)
				.compact();
	}

	//判读token是否有效
	public Boolean validateToken(String token, UserDetails userDetails) {
		JwtUser user = (JwtUser) userDetails;
		final String username = getUsernameFromToken(token);
		final Date created = getIssuedAtDateFromToken(token);
		//final Date expiration = getExpirationDateFromToken(token);
		return (
				username.equals(user.getUsername())
				&& !isTokenExpired(token)
				&& !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())
				);
	}

	//生成到期时间
	private Date calculateExpirationDate(Date createdDate) {
		Date d = new Date(createdDate.getTime() + 360 * 1000);
		System.out.println("Token终止时间："+d);
		//return new Date(createdDate.getTime() + expiration * 1000);//7天有效期
		return d;//1小时有效期，36000毫秒*1000=36000秒=1个小时
	}
}
