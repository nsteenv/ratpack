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

import org.ratpackframework.app.Endpoint;
import org.ratpackframework.app.Request;
import org.ratpackframework.app.Response;

public class ErrorHandlingEndpointDecorator implements Endpoint {

  private final Endpoint delegate;

  public ErrorHandlingEndpointDecorator(Endpoint delegate) {
    this.delegate = delegate;
  }

  @Override
  public void respond(Request request, Response response) {
    try {
      delegate.respond(request, response);
    } catch (Exception e) {
      response.error(e);
    }
  }

}
