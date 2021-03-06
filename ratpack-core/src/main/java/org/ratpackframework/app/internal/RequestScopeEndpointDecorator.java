package org.ratpackframework.app.internal;

import org.ratpackframework.app.Endpoint;
import org.ratpackframework.app.Request;
import org.ratpackframework.app.Response;

public class RequestScopeEndpointDecorator implements Endpoint {

  private final RequestScope scope;
  private final Endpoint delegate;

  public RequestScopeEndpointDecorator(RequestScope scope, Endpoint delegate) {
    this.scope = scope;
    this.delegate = delegate;
  }

  @Override
  public void respond(Request request, Response response) {
    scope.enter();
    try {
      scope.seed(Request.class, request);
      scope.seed(Response.class, response);
      delegate.respond(request, response);
    } finally {
      scope.exit();
    }
  }

}
