package org.ratpackframework.session.store;

import org.ratpackframework.app.Request;

import java.util.concurrent.ConcurrentMap;

public interface MapSessionStore {
  ConcurrentMap<String, Object> get(Request request);
  long size();
}
