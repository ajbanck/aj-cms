/*
 * Copyright 2014 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hippoecm.frontend.plugins.standards.image;

import java.io.IOException;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.io.IOUtils;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InlineSvg extends WebComponent {
    
    public static final Logger log = LoggerFactory.getLogger(InlineSvg.class);
    
    PackageResourceReference reference;
    
    public InlineSvg(final String id, final ResourceReference reference) {
        super(id);
        if (reference instanceof PackageResourceReference) {
            this.reference = (PackageResourceReference) reference;   
        } else {
            this.reference = new PackageResourceReference(reference.getKey());
        }
        setRenderBodyOnly(true);
    }

    @Override
    protected void onComponentTag(final ComponentTag tag) {
        try {
            final String svgData = svgAsString(reference);
            RequestCycle.get().getResponse().write(svgData);
        } catch (IOException | ResourceStreamNotFoundException e) {
            log.error(String.format("Failed to load svg image[%s]", reference.getName()), e);
        }
        super.onComponentTag(tag);
    }
    
    public static String svgAsString(PackageResourceReference reference) throws ResourceStreamNotFoundException, IOException {
        String data = IOUtils.toString(reference.getResource().getResourceStream().getInputStream());
        //skip everything (comments, xml declaration and dtd definition) before <svg element
        return data.substring(data.indexOf("<svg "));
    }
}