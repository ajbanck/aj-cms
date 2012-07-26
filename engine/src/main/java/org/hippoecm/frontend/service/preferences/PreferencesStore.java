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

package org.hippoecm.frontend.service.preferences;

import org.hippoecm.frontend.plugin.config.IPluginConfig;
import org.hippoecm.frontend.plugin.config.impl.JavaPluginConfig;


public class PreferencesStore implements IPreferencesStore {
    private static final long serialVersionUID = 1L;
    
    
    private IPluginConfig store;
    
    public PreferencesStore() {
        store = new JavaPluginConfig();
    }

    public boolean getBoolean(String context, String name) {
        return getPrefs(context).getBoolean(name);
    }

    public int getInt(String context, String name) {
        return getPrefs(context).getInt(name);
    }

    public String getString(String context, String name) {
        return getPrefs(context).getString(name);
    }

    public void set(String context, String name, String value) {
        getPrefs(context).put(name, value);
    }

    public void set(String context, String name, int value) {
        getPrefs(context).put(name, value);
    }

    public void set(String context, String name, boolean value) {
        getPrefs(context).put(name, value);
    }
    
    private IPluginConfig getPrefs(String context) {
        if(!store.containsKey(context)) {
            store.put(context, new JavaPluginConfig());
        }
        return store.getPluginConfig(context);
    }

}

