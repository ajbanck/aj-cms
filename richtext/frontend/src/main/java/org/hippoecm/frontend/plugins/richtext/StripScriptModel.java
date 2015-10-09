/*
 *  Copyright 2010-2015 Hippo B.V. (http://www.onehippo.com)
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
package org.hippoecm.frontend.plugins.richtext;

import java.util.regex.Pattern;

import org.apache.wicket.model.IModel;

public class StripScriptModel extends AbstractStringTransformingModel {

    private static Pattern SCRIPT_PATTERN = Pattern.compile("<script.*?((>.*?</script>)|(/>))",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    public StripScriptModel(IModel<String> valueModel) {
        super(valueModel);
    }

    @Override
    protected String transform(final String string) {
        return SCRIPT_PATTERN.matcher(string).replaceAll("");
    }

}
