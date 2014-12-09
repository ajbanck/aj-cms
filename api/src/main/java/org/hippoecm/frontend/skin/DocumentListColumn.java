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
package org.hippoecm.frontend.skin;

import org.apache.wicket.behavior.AttributeAppender;

/**
 * References to document list columns.
 */
public enum DocumentListColumn {

    ICON("doclisting-icon"),
    NAME("doclisting-name"),
    TYPE("doclisting-type"),
    STATE("doclisting-state"),
    DATE("doclisting-date"),
    LAST_MODIFIED_BY("doclisting-lastmodified-by"),
    TRANSLATIONS("doclisting-translations"),
    SIZE("doclisting-size"),
    MIME_TYPE("doclisting-mimetype");

    public static final String DOCUMENT_LIST_CSS_CLASS = "hippo-list-documents";

    private final String cssClass;

    private DocumentListColumn(String cssClass) {
        this.cssClass = cssClass;
    }

    public String getCssClass() {
        return cssClass;
    }

}
