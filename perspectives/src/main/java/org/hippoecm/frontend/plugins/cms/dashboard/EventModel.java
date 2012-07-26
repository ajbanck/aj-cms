/*
 *  Copyright 2009 Hippo.
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
package org.hippoecm.frontend.plugins.cms.dashboard;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.MissingResourceException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.model.IComponentAssignedModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.IWrapModel;
import org.apache.wicket.model.StringResourceModel;
import org.hippoecm.frontend.model.JcrItemModel;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventModel implements IComponentAssignedModel<String> {

    private static final long ONE_MINUTE = 60 * 1000;
    private static final long FIVE_MINUTES = 5 * 60 * 1000;
    private static final long TEN_MINUTES = 10 * 60 * 1000;
    private static final long HALF_AN_HOUR = 30 * 60 * 1000;
    private static final long ONE_HOUR = 60 * 60 * 1000;
    private static final long ONE_DAY = 24 * 60 * 60 * 1000;

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(EventModel.class);

    private DateFormat dateFormat;
    private String time;
    private String method;
    private String user;
    private IModel<String> nameModel;

    public EventModel(JcrNodeModel eventNode) {
        this(eventNode, null, DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT));
    }

    public EventModel(JcrNodeModel eventNode, IModel<String> nameModel, DateFormat dateFormat) {
        Node node = eventNode.getNode();
        try {
            if (node == null || !node.isNodeType("hippolog:item")) {
                throw new IllegalArgumentException(
                        "CurrentActivityPlugin can only process Nodes of type hippolog:item.");
            }

            this.dateFormat = dateFormat;

            this.time = relativeTime(node.getProperty("hippolog:timestamp").getLong());

            // add eventClass to resolve workflow resource bundle
            this.method = node.getProperty("hippolog:eventMethod").getString() + ",class="
                    + node.getProperty("hippolog:eventClass").getString();
            this.user = node.getProperty("hippolog:eventUser").getString();
            this.nameModel = nameModel;
        } catch (RepositoryException ex) {
            JcrItemModel itemModel = eventNode.getItemModel();
            if (itemModel.exists()) {
                log.error("Could not parse event node " + itemModel.getPath());
            } else {
                log.warn("Event node retrieved that no longer exists");
            }
        }
    }

    @Override
    public IWrapModel<String> wrapOnAssignment(Component component) {
        return new AssignmentWrapper(component);
    }

    @Override
    public String getObject() {
        throw new UnsupportedOperationException("Model " + getClass() + " does not support getObject(Object)");
    }

    @Override
    public void setObject(String object) {
        throw new UnsupportedOperationException("Model " + getClass() + " does not support setObject(Object)");
    }

    @Override
    public void detach() {
        if (nameModel != null) {
            nameModel.detach();
        }
    }

    private String relativeTime(final long then) {

        final long now = System.currentTimeMillis();

        if (then > now-ONE_MINUTE) {
            return "one-minute";
        }
        if (then > now-FIVE_MINUTES) {
            return "five-minutes";
        }
        if (then > now-TEN_MINUTES) {
            return "ten-minutes";
        }
        if (then > now-HALF_AN_HOUR) {
            return "half-hour";
        }
        if (then > now-ONE_HOUR) {
            return "hour";
        }

        final Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        final long today = cal.getTimeInMillis();

        if (then > today) {
            cal.set(Calendar.HOUR, 12);
            final long thisMorning = cal.getTimeInMillis();
            if (then < thisMorning) {
                return "morning";
            }

            cal.set(Calendar.HOUR, 18);
            final long thisAfternoon = cal.getTimeInMillis();
            if (then < thisAfternoon) {
                return "afternoon";
            }

            final long tomorrow = today + ONE_DAY;
            if (then < tomorrow) {
                return "evening";
            }
        }

        final long yesterday = today - ONE_DAY;

        if (then > yesterday) {
            return "yesterday";
        }

        return dateFormat.format(new Date(then));
    }

    private class AssignmentWrapper implements IWrapModel<String> {
        private static final long serialVersionUID = 1L;

        private final Component component;

        public AssignmentWrapper(Component component) {
            this.component = component;
        }

        @Override
        public IModel<String> getWrappedModel() {
            return EventModel.this;
        }

        @Override
        public String getObject() {
            try {
                if (nameModel != null) {
                    String name = nameModel.getObject();
                    StringResourceModel operationModel;
                    if(name != null && name.startsWith("org.hippoecm.repository.api.Document[uuid=")) {
                        final String method2 = StringUtils.replaceOnce(method, "delete", "delete-unknown");
                        operationModel = new StringResourceModel(method2, component, null, new Object[]{user});
                    } else {
                        name = StringEscapeUtils.escapeHtml(name);
                        operationModel = new StringResourceModel(method, component, null,
                                                                                     new Object[]{user, name});
                    }
                    return new StringResourceModel(time, component, null, "").getString() + operationModel.getString();
                } else {
                    StringResourceModel operationModel = new StringResourceModel(method, component, null,
                                                                                 new Object[]{user});
                    return new StringResourceModel(time, component, null, "").getString() + operationModel.getString();
                }
            } catch (MissingResourceException mre) {
                return "Warning: could not translate Workflow operation " + method;
            }
        }

        @Override
        public void detach() {
            EventModel.this.detach();
        }

        @Override
        public void setObject(String object) {
            throw new UnsupportedOperationException("Model " + getClass() + " does not support setObject(Object)");
        }

    }

}
