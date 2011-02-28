/*
 *  Copyright 2011 Hippo.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.hippoecm.frontend.plugins.console.menu.export;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.hippoecm.frontend.plugin.IPluginContext;
import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.service.render.RenderPlugin;
import org.hippoecm.frontend.session.UserSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoExportPlugin extends RenderPlugin<Node> {

    private static final long serialVersionUID = 1L;
    private static final String CONFIG_NODE_PATH = "/hippo:configuration/hippo:modules/autoexport/hippo:moduleconfig";
    private static final Logger log = LoggerFactory.getLogger("org.hippoecm.repository.export");

    public AutoExportPlugin(IPluginContext context, IPluginConfig config) {
        super(context, config);
        
        final String location = getExportLocation();
        
        // enable / disable link
        final Label label = new Label("link-text", new Model<String>() {
            private static final long serialVersionUID = 1L;
            
            @Override public String getObject() {
                if (location == null) {
                    return "Auto export unavailable";
                }
                return isExportEnabled() ? "Disable Auto Export" : "Enable Auto Export";
            }
        });
        label.setOutputMarkupId(true);
        label.add(new AttributeModifier("style", true, new Model<String>() {
            private static final long serialVersionUID = 1L;
            @Override
            public String getObject() {
                if (location == null) {
                    return "color:red";
                }
                return isExportEnabled() ? "color:green" : "color:red";
            }
        }));
        AjaxLink<Void> link = new AjaxLink<Void>("link") {

            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                setExportEnabled(!isExportEnabled());
                target.addComponent(label);
            }
            
        };
        link.add(label);
        link.setEnabled(location != null);
        add(link);
    }

    private boolean isExportEnabled() {
        boolean enabled = true;
        try {
            Node node = getJcrSession().getNode(CONFIG_NODE_PATH);
            enabled = node.getProperty("hipposys:enabled").getValue().getBoolean();
        } catch (PathNotFoundException e) {
            log.warn("No such item: " + CONFIG_NODE_PATH + "/hipposys:enabled");
        } catch (RepositoryException e) {
            log.error("An error occurred reading export enabled flag", e);
        }
        return enabled;
    }
    
    private void setExportEnabled(boolean enabled) {
        Session session = getJcrSession();
        try {
            // we use a separate session in order that other changes made in the console
            // don't get persisted upon save() here
            session = session.impersonate(new SimpleCredentials(session.getUserID(),new char[] {}));
            Node node = session.getNode(CONFIG_NODE_PATH);
            node.setProperty("hipposys:enabled", enabled);
            session.save();
            session.logout();
        } catch (PathNotFoundException e) {
            log.warn("No such item: " + CONFIG_NODE_PATH + "/hipposys:enabled");
        } catch (RepositoryException e) {
            log.error("An error occurred trying to set export enabled flag", e);
        }
    }

    private String getExportLocation() {
        String location = null;
        try {
            Node node = getJcrSession().getNode(CONFIG_NODE_PATH);
            location = node.getProperty("hipposys:location").getString();
        } catch (PathNotFoundException e) {
            log.debug("No such item: " + CONFIG_NODE_PATH + "/hipposys:location");
        } catch (RepositoryException e) {
            log.error("An error occurred looking up export location", e);
        }
        return location;
    }
    
    private Session getJcrSession() {
        Session session = ((UserSession) org.apache.wicket.Session.get()).getJcrSession();
        return session;
    }
}
