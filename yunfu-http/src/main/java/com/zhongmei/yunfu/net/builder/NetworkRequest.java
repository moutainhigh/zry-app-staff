package com.zhongmei.yunfu.net.builder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NetworkRequest {
    private int method;     private String url;     private Object content;     private Map<String, String> header;     private int timeout;     private ResponseProcessor processor;     private OnSuccessListener successListener;     private OnErrorListener errorListener;     private Type responseContentType;     private Object tag;     private List<RequestInterceptor> requestInterceptors = new ArrayList<>();     private boolean interceptEnable;
    public NetworkRequest(Builder builder) {
        this.method = builder.method;
        this.url = builder.url;
        this.content = builder.content;
        this.header = builder.header;
        this.timeout = builder.timeout;
        this.processor = builder.processor;
        this.successListener = builder.successListener;
        this.errorListener = builder.errorListener;
        this.responseContentType = builder.responseContentType;
        this.tag = builder.tag;
        this.interceptEnable = builder.interceptEnable;
    }

    public int getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Object getContent() {
        return content;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public int getTimeout() {
        return timeout;
    }

    public ResponseProcessor getProcessor() {
        return processor;
    }

    public OnSuccessListener getSuccessListener() {
        return successListener;
    }

    public OnErrorListener getErrorListener() {
        return errorListener;
    }

    public Type getResponseContentType() {
        return responseContentType;
    }

    public Object getTag() {
        return tag;
    }

    public List<RequestInterceptor> getRequestInterceptors() {
        return requestInterceptors;
    }

    public NetworkRequest addRequestInterceptor(RequestInterceptor interceptor) {
        this.requestInterceptors.add(interceptor);
        return this;
    }

    public boolean isInterceptEnable() {
        return this.interceptEnable;
    }

    public static class Builder {
        private int method = HttpMethod.POST;         private String url;
        private Object content;
        private HashMap<String, String> header;
        private int timeout = -1;
        private ResponseProcessor processor;         private OnSuccessListener successListener;         private OnErrorListener errorListener;         private Type responseContentType;         private Object tag;
        private boolean interceptEnable;
        public NetworkRequest build() {
            return new NetworkRequest(this);
        }

        public Builder interceptEnable(boolean enable) {
            this.interceptEnable = enable;
            return this;
        }

        public Builder httpMethod(int method) {
            this.method = method;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder content(Object content) {
            this.content = content;
            return this;
        }

        public Builder header(HashMap<String, String> header) {
            this.header = header;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder responseProcessor(ResponseProcessor processor) {
            this.processor = processor;
            return this;
        }

        public Builder successListener(OnSuccessListener successListener) {
            this.successListener = successListener;
            return this;
        }

        public Builder errorListener(OnErrorListener errorListener) {
            this.errorListener = errorListener;
            return this;
        }

        public Builder responseType(Type responseType) {
            this.responseContentType = responseType;
            return this;
        }

        public Builder tag(Object tag) {
            this.tag = tag;
            return this;
        }
    }


    public interface ResponseProcessor<R> {
        R process(R response);
    }


    public interface OnSuccessListener<T> {
        void onSuccess(T data);
    }


    public interface OnErrorListener {
        void onError(NetError error);
    }


    public interface RequestInterceptor {
        NetworkRequest intercept(NetworkRequest request);
    }


    public interface HttpMethod {
        int DEPRECATED_GET_OR_POST = -1;
        int GET = 0;
        int POST = 1;
        int PUT = 2;
        int DELETE = 3;
        int HEAD = 4;
        int OPTIONS = 5;
        int TRACE = 6;
        int PATCH = 7;
    }
}
