/*
 *  Copyright 2010-2013 Hippo B.V. (http://www.onehippo.com)
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
package org.hippoecm.frontend.plugins.standards.diff;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextDiffModelTest {

    @Test
    public void testTextDiffModel() {
        assertEquals("aap <span class=\"hippo-diff-added\">noot</span>", doTest("aap", "aap noot"));
        assertEquals("<span class=\"hippo-diff-added\">aap</span> noot", doTest("noot", "aap noot"));
        assertEquals("<span class=\"hippo-diff-removed\">aap</span> noot", doTest("aap noot", "noot"));
        assertEquals("aap <span class=\"hippo-diff-removed\">noot</span>", doTest("aap noot", "aap"));
        assertEquals("<span class=\"hippo-diff-removed\">aap</span> <span class=\"hippo-diff-added\">noot</span>",
                doTest("aap", "noot"));
        assertEquals("n&lt;ot", doTest("n<ot", "n<ot"));
    }

    private String doTest(String original, String current) {
        TextDiffModel tdm = new TextDiffModel(new Model<String>(original), new Model<String>(current));
        return tdm.getObject();
    }

}
