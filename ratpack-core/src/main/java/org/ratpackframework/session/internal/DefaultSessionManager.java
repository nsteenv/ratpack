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

package org.ratpackframework.session.internal;

import org.ratpackframework.session.SessionCookieConfig;
import org.ratpackframework.session.SessionIdGenerator;
import org.ratpackframework.session.SessionListener;
import org.ratpackframework.session.SessionManager;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class DefaultSessionManager implements SessionManager {

  private final SessionIdGenerator idGenerator;
  private final List<SessionListener> sessionListeners = new ArrayList<>(1);
  private final SessionCookieConfig sessionCookieConfig;

  @Inject
  public DefaultSessionManager(SessionIdGenerator idGenerator, SessionCookieConfig sessionCookieConfig) {
    this.idGenerator = idGenerator;
    this.sessionCookieConfig = sessionCookieConfig;
  }

  @Override
  public String getCookieDomain() {
    return sessionCookieConfig.getDomain();
  }

  @Override
  public String getCookiePath() {
    return sessionCookieConfig.getPath();
  }

  @Override
  public int getCookieExpiryMins() {
    return sessionCookieConfig.getExpiresMins();
  }

  @Override
  public SessionIdGenerator getIdGenerator() {
    return idGenerator;
  }

  @Override
  public void addSessionListener(SessionListener sessionListener) {
    sessionListeners.add(sessionListener);
  }

  @Override
  public void notifySessionInitiated(String sessionId) {
    for (SessionListener sessionListener : sessionListeners) {
      sessionListener.sessionInitiated(sessionId);
    }
  }

  @Override
  public void notifySessionTerminated(String sessionId) {
    for (SessionListener sessionListener : sessionListeners) {
      sessionListener.sessionTerminated(sessionId);
    }
  }

}
