package vn.vme.common;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

/**
 * @m-tech
 */
public class Dummy {
	protected static Logger log = LoggerFactory.getLogger(Dummy.class.getName());
	@Autowired
	protected Environment env;
	//Test info dummy 
	protected long USER_ID = 1;
	protected long MERCHANT_ID = 1;
	
	
	public static String accessToken ="Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbjk5QGdtYWlsLmNvbSIsInNjb3BlIjpbIndlYmNsaWVudCJdLCJleHAiOjE5MTA1MjcyNDMsInVzZXIiOnsidXNlck5hbWUiOiJhZG1pbjk5IiwicGhvdG8iOiJhdmF0YXIucG5nIiwibGV2ZWwiOnsia2V5IjoiQkFTSUMiLCJ2YWx1ZSI6IktIw4FDSCJ9LCJmdWxsTmFtZSI6IkFkbWluIFZpZXRMaW5rIiwiY2l0eSI6IkjDoCBO4buZaSIsImlkIjo5NSwiZW1haWwiOiJhZG1pbjk5QGdtYWlsLmNvbSIsInBob25lIjoiMDk4MTA2ODk5OSIsImJpcnRoRGF0ZSI6IjIyLTA1LTE5ODUiLCJwaG9uZVN0YXR1cyI6bnVsbCwic3NuIjpudWxsLCJzc25EYXRlIjoiMjAwOS0wNC0xMSIsImdlbmRlciI6Ik0iLCJhZGRyZXNzIjoiMTEgTXkgRGluaCIsImRpc3RyaWN0IjoiQ2F1IEdpYXkiLCJmaXJlYmFzZUlkIjpudWxsLCJkZXZpY2VJZCI6bnVsbCwiY291bnRyeSI6IlZOIiwicG9zdENvZGUiOm51bGwsIm5vdGlmeSI6dHJ1ZSwicHJvdmlkZXIiOm51bGwsImV4dGVybmFsSWQiOm51bGwsInR5cGUiOiJQRVJTT05BTCIsInBheW1lbnRJbmZvIjpudWxsLCJiYWxhbmNlIjowLCJzdGF0dXMiOnsia2V5IjoiQUNUSVZFIiwidmFsdWUiOiLEkEFORyBIT-G6oFQgxJDhu5hORyJ9LCJjcmVhdGVEYXRlIjoxNTk1MTY3MjA4NDg1LCJ1cGRhdGVEYXRlIjoxNTk1MTY3MjA4NDg1LCJyb2xlcyI6W3siaWQiOjIsIm5hbWUiOiJBRE1JTiIsInR5cGUiOiJBRE1JTiJ9LHsiaWQiOjMsIm5hbWUiOiJFRElUT1IiLCJ0eXBlIjoiRURJVE9SIn0seyJpZCI6NCwibmFtZSI6Ik1DIiwidHlwZSI6Ik1DIn1dfSwiYXV0aG9yaXRpZXMiOlsiRURJVE9SIiwiTUMiLCJBRE1JTiJdLCJqdGkiOiI5YTg2NDc1ZS03NmQ3LTRhMDAtYjQ0OC1lNjk0ODI3ZmIwYTYiLCJjbGllbnRfaWQiOiIxMjMifQ.BxpIyBNThxcz6sEPe-xhXAsceLtNR9h60kJ5ZtTtYxs";
	protected Principal principal;
	
	@Value("${spring.datasource.driver-class-name}")
	String driver;
	@Value("${spring.datasource.url}")
	String url;
	@Value("${spring.datasource.username}")
	String username;
	@Value("${spring.datasource.password}")
	String password;
	
	protected int page = Integer.parseInt(JConstants.PAGE);
	protected int size = Integer.parseInt(JConstants.SIZE);
}
