package com.quest.sample.restful;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.quest.sample.util.GlobalConfiguration;
import com.quest.sample.util.SecurityLoginType;

public abstract class HttpRequestUtil {
	
	private GlobalConfiguration globalConfiguration;
	
	public HttpRequestUtil(GlobalConfiguration globalConfiguration) {
		this.globalConfiguration = globalConfiguration;
	}
	
	public abstract String getPath();
	
	public Object getEntity() {
		return null;
	}
	
	public void securityLogin() throws IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
        	HttpPost httppost  = new HttpPost(globalConfiguration.getProtocol() + "://" + 
            		globalConfiguration.getServerUrl() + ":" + 
            		globalConfiguration.getServerPort() + "/api/" + 
            		globalConfiguration.getApiVersion() + "/security/login");
        	httppost.setHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        	List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            if (globalConfiguration.getSecurityLoginType().equals(SecurityLoginType.AUTHTOKEN)) {
            	parameters.add(new BasicNameValuePair("authToken", globalConfiguration.getToken()));
            } else if (globalConfiguration.getSecurityLoginType().equals(SecurityLoginType.USERNAME_PASSWORD)) {
            	parameters.add(new BasicNameValuePair("username", globalConfiguration.getUsername()));
            	parameters.add(new BasicNameValuePair("pwd", globalConfiguration.getPassword()));
            }
            httppost.setEntity(new UrlEncodedFormEntity(parameters));
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                	JSONObject loginJson = new JSONObject(EntityUtils.toString(response.getEntity()));
                	int status = loginJson.getInt("status");
                	if (status == 1) {
                		globalConfiguration.setToken(loginJson.getJSONObject("data").getString("token"));
                	} else {
                		System.err.println("Request for security login recieves failed status.");
                	}
                } else {
                	System.err.println("Requested Failed with status " + response.getStatusLine().getStatusCode());
                }
            } finally {
            	if (response != null) {
            		response.close();
            	}
            }
        } finally {
        	if (httpclient != null) {
        		httpclient.close();
        	}
        }
	}
	
	public String get() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(globalConfiguration.getProtocol() + "://" + 
            		globalConfiguration.getServerUrl() + ":" + globalConfiguration.getServerPort() + 
            		"/api/" + globalConfiguration.getApiVersion() + 
            		getPath());
            httpget.setHeader(HttpHeaders.ACCEPT, "application/json");
            httpget.setHeader("Auth-Token", globalConfiguration.getToken());
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                	String responseText = EntityUtils.toString(response.getEntity());
                	JSONObject responseJson = new JSONObject(responseText);
                	if (responseJson.getInt("status") == 1) {
                		return responseText;
                	} else {
                		System.err.println("Request for get resource recieves failed status.");
                	}
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED){
                	securityLogin();
                	return get();
                } else {
                	System.err.println("Requested Failed with status " + response.getStatusLine().getStatusCode());
                }
            } finally {
            	if (response != null) {
            		response.close();
            	}
            }
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
            	if (httpclient != null) {
            		httpclient.close();
            	}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return null;
	}
	
	public String post() {
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httppost = new HttpPost(globalConfiguration.getProtocol() + "://" + 
            		globalConfiguration.getServerUrl() + ":" + globalConfiguration.getServerPort() + 
            		"/api/" + globalConfiguration.getApiVersion() + 
            		getPath());
            httppost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httppost.setHeader(HttpHeaders.ACCEPT, "application/json");
            httppost.setHeader("Auth-Token", globalConfiguration.getToken());
            Object entity = getEntity();
            if (entity != null) {
            	if (entity instanceof AbstractBean) {
            		httppost.setEntity(new StringEntity(new Gson().toJson(entity)));
            	} else if (entity instanceof String) {
            		httppost.setEntity(new StringEntity(entity.toString()));
            	}
            }
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                	String responseText = EntityUtils.toString(response.getEntity());
                	JSONObject responseJson = new JSONObject(responseText);
                	if (responseJson.getInt("status") == 1) {
                		return responseText;
                	} else {
                		System.err.println("Request for post resource recieves failed status.");
                	}
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED){
                	securityLogin();
                	return post();
                } else {
                	System.err.println("Requested Failed with status " + response.getStatusLine().getStatusCode());
                }
            } finally {
            	if (response != null) {
            		response.close();
            	}
            }
        } catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            try {
            	if (httpclient != null) {
            		httpclient.close();
            	}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return null;
	}
	
	
	
}
