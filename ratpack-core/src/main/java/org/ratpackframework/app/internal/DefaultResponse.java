/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ratpackframework.app.internal;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.handler.codec.http.*;
import org.ratpackframework.app.Request;
import org.ratpackframework.app.Response;
import org.ratpackframework.http.HttpExchange;
import org.ratpackframework.handler.Result;
import org.ratpackframework.handler.ResultHandler;
import org.ratpackframework.http.MediaType;
import org.ratpackframework.http.MutableMediaType;
import org.ratpackframework.render.TextRenderer;
import org.ratpackframework.util.IoUtils;
import org.ratpackframework.templating.TemplateRenderer;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;

public class DefaultResponse implements Response {

  private final Request request;
  private final HttpExchange exchange;

  private final TemplateRenderer templateRenderer;
  private final HttpResponse response;
  private final TextRenderer textRenderer;

  public DefaultResponse(Request request, HttpExchange exchange, TemplateRenderer templateRenderer, TextRenderer textRenderer) {
    this.request = request;
    this.exchange = exchange;
    this.response = exchange.getResponse();
    this.templateRenderer = templateRenderer;
    this.textRenderer = textRenderer;
  }

  @Override
  public Request getRequest() {
    return this.request;
  }

  @Override
  public List<Map.Entry<String, String>> getHeaders() {
    return response.getHeaders();
  }

  @Override
  public int getStatus() {
    return response.getStatus().getCode();
  }

  @Override
  public void setStatus(int status) {
    response.setStatus(HttpResponseStatus.valueOf(status));
  }

  private void maybeSetUtf8ContentType(String contentTypeBase) {
    if (getHeader(CONTENT_TYPE) == null) {
      setHeader(CONTENT_TYPE, new MutableMediaType().utf8(contentTypeBase));
    }
  }

  public void render(String templateName) {
    render(Collections.<String, Object>emptyMap(), templateName);
  }

  public void render(Map<String, ?> model, String templateName) {
    maybeSetUtf8ContentType(MediaType.TEXT_HTML);
    templateRenderer.renderTemplate(templateName, model, new ResultHandler<ChannelBuffer>() {
      @Override
      public void handle(Result<ChannelBuffer> event) {
        if (event.isSuccess()) {
          end(event.getValue());
        } else {
          error(event.getFailure());
        }
      }
    });
  }

  public void text(Object text) {
    maybeSetUtf8ContentType(MediaType.TEXT_PLAIN);
    String str = textRenderer.render(text);
    byte[] bytes = IoUtils.utf8Bytes(str);
    setHeader(HttpHeaders.Names.CONTENT_LENGTH, bytes.length);
    end(IoUtils.channelBuffer(bytes));
  }

  @Override
  public void text(String contentTypeBase, Object str) {
    setHeader(CONTENT_TYPE, new MutableMediaType().utf8(contentTypeBase));
    text(str);
  }

  public void redirect(String location) {
    setHeader(HttpHeaders.Names.LOCATION, location);
    end(HttpResponseStatus.FOUND.getCode());
  }

  @Override
  public void redirect(int code, String location) {
    setHeader(HttpHeaders.Names.LOCATION, location);
    end(code);
  }

  public void error(Exception e) {
    exchange.error(e);
  }

  @Override
  public void end(ChannelBuffer buffer) {
    exchange.end(buffer);
  }

  @Override
  public void end() {
    end(null);
  }

  @Override
  public void end(int status) {
    exchange.end(HttpResponseStatus.valueOf(status));
  }

  @Override
  public void end(int status, String message) {
    exchange.end(new HttpResponseStatus(status, message));
  }

  @Override
  public String getHeader(String name) {
    return response.getHeader(name);
  }

  @Override
  public List<String> getHeaders(String name) {
    return response.getHeaders(name);
  }

  @Override
  public boolean containsHeader(String name) {
    return response.containsHeader(name);
  }

  @Override
  public Set<String> getHeaderNames() {
    return response.getHeaderNames();
  }

  @Override
  public void addHeader(String name, Object value) {
    response.addHeader(name, value);
  }

  @Override
  public void setHeader(String name, Object value) {
    response.setHeader(name, value);
  }

  @Override
  public void setHeader(String name, Iterable<?> values) {
    response.setHeader(name, values);
  }

  @Override
  public void removeHeader(String name) {
    response.removeHeader(name);
  }

  @Override
  public void clearHeaders() {
    response.clearHeaders();
  }

  @Override
  public Set<Cookie> getCookies() {
    return exchange.getOutgoingCookies();
  }

  @Override
  public Cookie cookie(String name, String value) {
    Cookie cookie = new DefaultCookie(name, value);
    getCookies().add(cookie);
    return cookie;
  }

  @Override
  public Cookie expireCookie(String name) {
    Cookie cookie = cookie(name, "");
    cookie.setMaxAge(0);
    return cookie;
  }

}
