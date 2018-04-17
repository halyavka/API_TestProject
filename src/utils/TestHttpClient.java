package utils;

import com.google.gson.JsonParser;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestHttpClient {

	private static final Logger log = Logger.getLogger(ClassNameUtil.getCurrentClassName());
	private CloseableHttpClient client;
	private Map<String, String> headers;
	private HttpRequestBase request;
	private int code;
	private int additionalCode;
	private Object requestEntity;
	private CookieStore cookieStore = new BasicCookieStore();

	public TestHttpClient() {
		this((CredentialsProvider) null);
	}

	public TestHttpClient(CredentialsProvider credentialsProvider) {
		try {
			client = initClient(credentialsProvider);
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	private CloseableHttpClient initClient(CredentialsProvider credentialsProvider) throws Exception {
		HttpClientBuilder httpClientBuilder = HttpClients.custom()
				.setSSLSocketFactory(SSLUtils.getInsecureSSLConnectionSocketFactory())
				.setDefaultCookieStore(cookieStore);
		if (credentialsProvider != null) {
			httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
		}
		return httpClientBuilder.build();
	}

	public TestHttpClient get(String url, int _code, int ... additionalCodes) {
		headers = new HashMap<>();
		request = new HttpGet(url);
		code = _code;
		additionalCode = (additionalCodes.length == 0) ? 0 : additionalCodes[0];
		return this;
	}

	public TestHttpClient options(String url, int _code) {
		headers = new HashMap<>();
		request = new HttpOptions(url);
		code = _code;
		return this;
	}

	public TestHttpClient post(String url, List<BasicNameValuePair> basicNameValuePairs, int _code) {
		headers = new HashMap<>();
		request = new HttpPost(url);
		code = _code;
		this.requestEntity = basicNameValuePairs;
		return this;
	}

	public TestHttpClient post(String url, String jsonString, int _code) {
		StringEntity stringEntity = new StringEntity(jsonString, ContentType.APPLICATION_JSON);
		headers = new HashMap<>();
		request = new HttpPost(url);
		code = _code;
		this.requestEntity = stringEntity;
		return this;
	}

	public Object execute() {
		StringBuilder stringBuilder = new StringBuilder();

		HttpContext localContext = new BasicHttpContext();
		localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);

		for (String key : headers.keySet()) {
			request.addHeader(key, headers.get(key));
		}
		try {
			if (request instanceof HttpPost) {
				if (requestEntity instanceof StringEntity) {
					((HttpPost)request).setEntity((StringEntity)requestEntity);
				} else {
					((HttpPost)request).setEntity(new UrlEncodedFormEntity((List<NameValuePair>) requestEntity));
				}
			}
			HttpResponse response = client.execute(request, localContext);
			cookieStore = (CookieStore) localContext.getAttribute(HttpClientContext.COOKIE_STORE);
			int statusCode = response.getStatusLine().getStatusCode();

			if (additionalCode != 0 && statusCode == additionalCode) {
				return null;
			}
			Assert.assertEquals(statusCode, code, "Wrong HTTP status code!");

			log.info("\tRequest - " + request.toString() + "\n\tStatus Code = " + statusCode);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line).append("\n");
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} finally {
			request.abort();
		}
		return responseParser(stringBuilder);
	}

	public TestHttpClient setHeader(String name, String value) {
		headers.put(name, value);
		return this;
	}

	private Object responseParser(StringBuilder input) {
		Object object;
		try {
			object = new JsonParser().parse(input.toString());
			log.debug("Object type is JSON.");
		} catch (Exception e) {
			object = input.toString();
			log.debug("Object type is String.");
		}
		return object;
	}



}


