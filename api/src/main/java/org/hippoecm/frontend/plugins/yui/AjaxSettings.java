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

import java.util.Map;

public class AjaxSettings implements IAjaxSettings {

    private String callbackUrl;
    private JsFunction callbackFunction;
    private Map<String, ?> callbackParameters;

    public void setCallbackUrl(String url) {
        this.callbackUrl = url;
    }

    public void setCallbackFunction(JsFunction function) {
        this.callbackFunction = function;
    }

    public void setCallbackParameters(Map<String, Object> map) {
        this.callbackParameters = map;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public Map<String, ?> getCallbackParameters() {
        return callbackParameters;
    }

    public JsFunction getCallbackFunction() {
        return callbackFunction;
    }}
