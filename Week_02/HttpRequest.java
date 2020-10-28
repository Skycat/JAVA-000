package io.github.skycat.geekstudy.java.week02;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * HttpRequest
 * @author Skycat
 * @email skycat2004@gmail.com
 * @version 1.0 2020-10-28 00:20:42
 */
public class HttpRequest {
	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) {
		HttpGet httpGet = new HttpGet("http://127.0.0.1:8801/");
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(5000)
				.setSocketTimeout(5000).setRedirectsEnabled(true).build();
		httpGet.setConfig(requestConfig);
		try (
			CloseableHttpClient httpClient = HttpClientBuilder.create().build();
			CloseableHttpResponse response = httpClient.execute(httpGet);
		) {
			HttpEntity responseEntity = response.getEntity();
			System.out.println("statue code: " + response.getStatusLine());
			if (null != responseEntity) {
				System.out.println(EntityUtils.toString(responseEntity));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
