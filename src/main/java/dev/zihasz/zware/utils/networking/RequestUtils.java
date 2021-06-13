package dev.zihasz.zware.utils.networking;

import dev.zihasz.zware.utils.Util;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

public class RequestUtils implements Util {

	private static CloseableHttpClient httpClient = HttpClients.createDefault();

	public static List<String> get(String url, String userAgent) throws IOException {
		HttpGet request = new HttpGet(url);

		request.addHeader("User-Agent", userAgent);
		request.addHeader("Content-Type", "text/plain");

		HttpResponse response = httpClient.execute(request);
		return IOUtils.readLines(response.getEntity().getContent(), Charset.defaultCharset());
	}
	public static JSONObject getJson(String url, String userAgent) throws IOException, ParseException {
		HttpGet request = new HttpGet(url);

		request.addHeader("User-Agent", userAgent);
		request.addHeader("Content-Type", "application/json");

		HttpResponse response = httpClient.execute(request);
		JSONParser parser = new JSONParser();
		return (JSONObject) parser.parse(new InputStreamReader(response.getEntity().getContent()));
	}

	public static HttpResponse post(String url, String message, String userAgent) throws IOException {
		HttpPost request = new HttpPost(url);
		StringEntity entity = new StringEntity(message);

		request.addHeader("User-Agent", userAgent);
		request.addHeader("Content-Type", "application/json");
		request.setEntity(entity);

		return httpClient.execute(request);
	}
	public static HttpResponse postJson(String url, String json, String userAgent) throws IOException {
		HttpPost request = new HttpPost(url);
		StringEntity entity = new StringEntity(json);

		request.addHeader("User-Agent", userAgent);
		request.addHeader("Content-Type", "application/json");
		request.setEntity(entity);

		return httpClient.execute(request);
	}

}
