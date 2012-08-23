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
package org.hippoecm.frontend.plugins.yui;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class YuiWicketApplication extends WebApplication {

    @Override
    public String getConfigurationType() {
        // suppress development mode warning from test output
        return Application.DEPLOYMENT;
    }

    @Override
    protected void init() {
        getDebugSettings().setOutputComponentPath(true);
        super.init();
    }
    
    @Override
    public Class<? extends Page> getHomePage() {
        try {
            return (Class<? extends Page>) getClass().getClassLoader().loadClass(getInitParameter("test-page-class"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
