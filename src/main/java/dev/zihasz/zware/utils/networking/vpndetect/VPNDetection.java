package dev.zihasz.zware.utils.networking.vpndetect;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Allows you to detect whether or not a specified IPv4 Address belongs to a
 * hosting or vpn / proxy organization.
 * <p>
 * This class facilitates and simplifies using the web API, and allows you to
 * easily implement the functionality in your applications.
 * <p>
 * API Homepage: https://vpnblocker.net
 *
 * @author HiddenMotives
 */
public final class VPNDetection {

	private String apiKey;
	private String apiUrl = "http://api.vpnblocker.net/v2/json/";
	private int apiTimeout = 5000;

	public VPNDetection() {
		this.apiKey = null;
	}
	public VPNDetection(String key) {
		this.apiKey = key;
	}
	public VPNDetection(String key, int timeout) {
		this.apiKey = key;
		this.apiTimeout = timeout;
	}

	public void setApiKey(String key) {
		this.apiKey = key;
	}
	public void setApiTimeout(int timeout) {
		this.apiTimeout = timeout;
	}
	public void useSSL() {
		this.apiUrl = this.apiUrl.replace("http://", "https://");
	}

	public Response resolve(String ip) throws IOException {
		String query_url = this.getQueryUrl(ip);
		String query_result = this.query(query_url, this.apiTimeout, "Java-VPNDetection Library");
		return new Gson().fromJson(query_result, Response.class);
	}

	public String getQueryUrl(String ip) {
		String query_url;
		if (this.apiKey == null) {
			query_url = this.apiUrl + ip;
		} else {
			query_url = this.apiUrl + ip + "/" + this.apiKey;
		}
		return query_url;
	}
	public String query(String url, int timeout, String userAgent) throws IOException {
		StringBuilder response = new StringBuilder();
		URL website = new URL(url);
		URLConnection connection = website.openConnection();
		connection.setConnectTimeout(timeout);
		connection.setRequestProperty("User-Agent", userAgent);
		try (BufferedReader in = new BufferedReader(
				new InputStreamReader(
						connection.getInputStream()))) {
			while ((url = in.readLine()) != null) {
				response.append(url);
			}
		}
		return response.toString();
	}

}
