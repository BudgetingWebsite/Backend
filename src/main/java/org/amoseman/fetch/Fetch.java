package org.amoseman.fetch;

import org.apache.http.HttpHost;
import org.apache.http.RequestLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Fetch {
    public static class Builder {
        private String domain;
        private String username;
        private String password;

        public Builder setDomain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Fetch build() {
            return new Fetch(domain, username, password);
        }
    }

    private final String domain;
    private final CloseableHttpClient client;

    private Fetch(String domain, String username, String password) {
        this.domain = domain;
        if (null != username && null != password) {
            BasicCredentialsProvider provider = new BasicCredentialsProvider();
            AuthScope scope = new AuthScope(new HttpHost(domain));
            provider.setCredentials(scope, new UsernamePasswordCredentials(username, password));
            this.client = HttpClientBuilder.create()
                    .setDefaultCredentialsProvider(provider)
                    .build();
        }
        else {
            this.client = HttpClientBuilder.create().build();
        }
    }

    public void close() {
        try {
            client.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String execute(HttpRequestBase request, ResponseHandler<String> handler) {
        try {
            return client.execute(request, handler);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String uri(String path) {
        return String.format("%s%s", domain, path);
    }

    private StringEntity asEntity(String contents) {
        if (null == contents) {
            return null;
        }
        try {
            return new StringEntity(contents);
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpRequestBase toBase(String uri, String method, StringEntity entity) {
        return switch (method) {
            case "GET" -> {
                if (null != entity) {
                    throw new RuntimeException(String.format("Cannot have entity with %s", method));
                }
                yield new HttpGet(uri);
            }
            case "POST" -> {
                HttpPost post = new HttpPost(uri);
                post.setEntity(entity);
                yield post;
            }
            case "PUT" -> {
                HttpPut put = new  HttpPut(uri);
                put.setEntity(entity);
                yield put;
            }
            case "DELETE" -> {
                if (null != entity) {
                    throw new RuntimeException(String.format("Cannot have entity with %s", method));
                }
                yield new HttpDelete(uri);
            }
            default -> throw new RuntimeException(String.format("%s is not a valid HTTP verb", method));
        };
    }

    public String request(String path, String method, String contents, ResponseHandler<String> handler) {
        StringEntity entity = asEntity(contents);
        String uri = uri(path);
        HttpRequestBase base = toBase(uri, method, entity);
        return execute(base, handler);
    }
}