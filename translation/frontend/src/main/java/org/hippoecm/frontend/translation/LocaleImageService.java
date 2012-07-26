/*
 *  Copyright 2010 Hippo.
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
package org.hippoecm.frontend.translation;

import org.apache.wicket.Application;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.request.target.resource.ResourceStreamRequestTarget;
import org.hippoecm.frontend.service.IconSize;
import org.hippoecm.frontend.translation.ILocaleProvider.LocaleState;
import org.wicketstuff.js.ext.data.AbstractExtBehavior;

public final class LocaleImageService extends AbstractExtBehavior {

    private static final long serialVersionUID = 1L;
    
    private ILocaleProvider provider;

    public LocaleImageService(ILocaleProvider provider) {
        this.provider = provider;
    }

    public void onRequest() {
        if (provider == null) {
            throw new WicketRuntimeException("No locale provider available");
        }
        RequestCycle rc = RequestCycle.get();
        String language = rc.getRequest().getParameter("lang");
        ResourceReference resourceRef = provider.getLocale(language).getIcon(IconSize.TINY, LocaleState.EXISTS);
        resourceRef.bind(Application.get());
        rc.setRequestTarget(new ResourceStreamRequestTarget(resourceRef.getResource().getResourceStream()));
    }

}
