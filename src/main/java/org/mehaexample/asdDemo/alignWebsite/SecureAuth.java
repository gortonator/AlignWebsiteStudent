package org.mehaexample.asdDemo.alignWebsite;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwa.AlgorithmConstraints.ConstraintType;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.mehaexample.asdDemo.dao.alignprivate.StudentLoginsDao;
import org.mehaexample.asdDemo.dao.alignprivate.StudentsDao;
import org.mehaexample.asdDemo.model.alignprivate.StudentLogins;
import org.mehaexample.asdDemo.model.alignprivate.Students;

@Provider
public class SecureAuth implements ContainerRequestFilter{

	private static final String AUTHOIRIZATION_HEADER = "token";
	private static final String LOGIN_URL = "login";
	private static final String REGISTER_URL = "registration";
	private static final String RESET_URL = "password-reset";
	private static final String CREATE_URL = "password-create";

	@Context
	private HttpServletRequest sr;

	StudentLoginsDao studentLoginsDao = new StudentLoginsDao();
	StudentsDao studentDao = new StudentsDao();

	@Override
	public void filter(ContainerRequestContext requestContext)
			throws IOException {
		if(!requestContext.getUriInfo().getPath().contains(LOGIN_URL) && 
				!requestContext.getUriInfo().getPath().contains(REGISTER_URL) &&
				!requestContext.getUriInfo().getPath().contains(CREATE_URL) && 
				!requestContext.getUriInfo().getPath().contains(RESET_URL)){
			try {
				String method = requestContext.getMethod();
				if (method.equals("OPTIONS")){
					return;
				}
				List<String> authHeader =  requestContext.getHeaders().get(AUTHOIRIZATION_HEADER);
				if(authHeader.size() > 0){
					try {
						String authToken = authHeader.get(0);			
						String ip = sr.getRemoteAddr();
						String secretKey = ip+"sEcR3t_nsA-K3y";
						byte[] key = secretKey.getBytes();
						key = Arrays.copyOf(key, 32);
						AesKey keyMain = new AesKey(key);

						JsonWebEncryption receiverJwe = new JsonWebEncryption();
						AlgorithmConstraints algConstraints = new AlgorithmConstraints(ConstraintType.WHITELIST,KeyManagementAlgorithmIdentifiers.DIRECT);
						receiverJwe.setAlgorithmConstraints(algConstraints);
						AlgorithmConstraints encConstraints = new AlgorithmConstraints(ConstraintType.WHITELIST, ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
						receiverJwe.setContentEncryptionAlgorithmConstraints(encConstraints);
						receiverJwe.setCompactSerialization(authToken);
						receiverJwe.setKey(keyMain);
						String plaintext;
						plaintext = receiverJwe.getPlaintextString();
						StringTokenizer tokenData = new StringTokenizer(plaintext,"*#*");
						String email = tokenData.nextToken();
						String ipAddress = tokenData.nextToken();
						String timeValid = tokenData.nextToken();
						String tokenCheck = timeValid.substring(0,timeValid.length()-6);
						StudentLogins studentLogins = studentLoginsDao.findStudentLoginsByEmail(email);

						if(studentLogins == null){
							requestContext.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE).
									entity("Token not valid. Please login again.").build());
						}else if (method.equals("PUT") || method.equals("DELETE")){
							Students studentDetail = studentDao.getStudentRecordByEmailId(email);
							if(!requestContext.getUriInfo().getPath().contains(new String(Base64.getEncoder().encode(studentDetail.getNeuId().getBytes())))){
								requestContext.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE).
										entity("You cannot edit other student's info.").build());
							}
						}
						String loginTime = studentLogins.getLoginTime().toString();
						String expireTime = studentLogins.getKeyExpiration().toString();
						Timestamp now = new Timestamp(System.currentTimeMillis());
						Timestamp valid = Timestamp.valueOf(now.toString());
						Timestamp expire = Timestamp.valueOf(expireTime);
						String timeLogin = loginTime.substring(0,loginTime.length()-4);
						if(ip.equals(ipAddress) && timeLogin.equals(tokenCheck) && valid.before(expire)) {
							if (method.equals("POST")){
								Students studentDetail = studentDao.getStudentRecordByEmailId(email);
								String encodedNuid = new String(Base64.getEncoder().encode(studentDetail.getNeuId().getBytes()));
								if(!requestContext.getUriInfo().getPath().contains(encodedNuid+"/extraexperiences") && 
										!requestContext.getUriInfo().getPath().contains(encodedNuid+"/projects")	&&
										!requestContext.getUriInfo().getPath().contains(encodedNuid+"/privacies") ){
									if(!requestContext.getUriInfo().getPath().equals("students") &&
											!requestContext.getUriInfo().getPath().equals("students/") &&
											!requestContext.getUriInfo().getPath().equals("autofill-search") &&
											!requestContext.getUriInfo().getPath().equals("autofill-search/") ){
										requestContext.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE).
												entity("You cannot edit other student's info.").build());
									}
								}
							}
							if (method.equals("GET")){
								Students studentDetail = studentDao.getStudentRecordByEmailId(email);
								String encodedNuid = new String(Base64.getEncoder().encode(studentDetail.getNeuId().getBytes()));
								if(requestContext.getUriInfo().getPath().contains("myProfile/")) {
									if(!requestContext.getUriInfo().getPath().contains("myProfile/"+encodedNuid)){
										requestContext.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE).
												entity("fetch retricted. You can't access other students confidential info.").build());
									}
								}

							}
							Timestamp keyExpiration = new Timestamp(System.currentTimeMillis()+15*60*1000);
							studentLogins.setKeyExpiration(keyExpiration);
							studentLoginsDao.updateStudentLogin(studentLogins);
							return;
						} else {
							requestContext.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE).
									entity("Token expired. Please login again.").build());
						}
					} catch (Exception e) {
						requestContext.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE).
								entity("Token Tampered. Please login again.").build());
					}
				} else {
					requestContext.abortWith(Response.status(Response.Status.NOT_ACCEPTABLE).
							entity("Please include authentication token in the Header.").build()); 
				}
			}
			catch (Exception e) {
				requestContext.abortWith(Response.status(Response.Status.BAD_REQUEST).
						entity("Bad Request.").build());
			}
		} else {
			return;
		}
	}
}