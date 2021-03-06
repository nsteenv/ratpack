package org.ratpackframework.groovy.templating.internal;

import java.util.Map;

public interface NestedRenderer {

  void render(String templateName, Map<String, ?> model) throws Exception;

}
