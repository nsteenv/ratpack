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

package org.ratpackframework.groovy.config;

import com.google.inject.Module;
import org.ratpackframework.assets.StaticAssetsConfig;
import org.ratpackframework.config.AddressConfig;
import org.ratpackframework.groovy.app.RoutingConfig;
import org.ratpackframework.groovy.templating.TemplatingConfig;
import org.ratpackframework.session.SessionCookieConfig;

import java.io.File;
import java.util.List;

public interface Config {

  AddressConfig getDeployment();

  StaticAssetsConfig getStaticAssets();

  TemplatingConfig getTemplating();

  RoutingConfig getRouting();

  SessionCookieConfig getSessionCookie();

  List<Module> getModules();

}