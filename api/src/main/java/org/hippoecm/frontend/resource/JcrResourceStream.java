/*
 *  Copyright 2008 Hippo.
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
package org.hippoecm.frontend.resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.wicket.model.IModel;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.apache.wicket.util.time.Time;
import org.hippoecm.frontend.model.JcrNodeModel;
import org.hippoecm.frontend.model.NodeModelWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JcrResourceStream extends NodeModelWrapper<Void> implements IResourceStream {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(JcrResourceStream.class);

    private transient InputStream stream;

    /**
     * @deprecated
     */
    public JcrResourceStream(Node node) {
        this(new JcrNodeModel(node));
    }

    public JcrResourceStream(IModel<Node> model) {
        super(model);
    }

    public void close() throws IOException {
        if (stream != null) {
            stream.close();
            stream = null;
        }
    }

    public String getContentType() {
        try {
            Node node = getNode();
            if (node != null) {
                return node.getProperty("jcr:mimeType").getString();
            } else {
                return "unknown";
            }
        } catch (RepositoryException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    public InputStream getInputStream() throws ResourceStreamNotFoundException {
        try {
            if (stream != null) {
                stream.close();
            }
            Node node = getNode();
            if (node != null) {
                stream = node.getProperty("jcr:data").getStream();
            } else {
                stream = new ByteArrayInputStream(new byte[0]);
            }
        } catch (RepositoryException ex) {
            throw new ResourceStreamNotFoundException(ex);
        } catch (IOException ex) {
            throw new ResourceStreamNotFoundException(ex);
        }
        return stream;
    }

    public Locale getLocale() {
        // TODO Auto-generated method stub
        return null;
    }

    public long length() {
        long length = -1;
        try {
            Node node = getNode();
            if (node == null) {
                return 0;
            }
            length = node.getProperty("jcr:data").getLength();
        } catch (RepositoryException e) {
            log.error(e.getMessage());
        }
        return length;
    }

    public void setLocale(Locale locale) {
        // TODO Auto-generated method stub
    }

    public Time lastModifiedTime() {
        try {
            Node node = getNode();
            Calendar date;
            if (node != null) {
                date = node.getProperty("jcr:lastModified").getDate();
            } else {
                date = Calendar.getInstance();
            }
            return Time.valueOf(date.getTime());
        } catch (RepositoryException ex) {
            log.error(ex.getMessage());
        }
        return null;
    }
}
