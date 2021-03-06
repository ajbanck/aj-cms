/*
 * Copyright 2012-2016 Hippo B.V. (http://www.onehippo.com)
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
package org.hippoecm.frontend.plugins.console.menu.help;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.PropertyListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.hippoecm.frontend.dialog.AbstractDialog;
import org.hippoecm.frontend.plugins.console.Shortcuts;

/**
 * Console help dialog
 */
public class HelpDialog extends AbstractDialog<Void> {

    public HelpDialog() {
        add(new Label("label", getTitle()));
        add(new PropertyListView<HelpDescription>("listview", Shortcuts.HELP_ITEMS) {
            protected void populateItem(ListItem<HelpDescription> item) {
                HelpDescription user = item.getModelObject();
                item.add(new Label("name", user.getName()));
                item.add(new Label("description", user.getDescription()));
                item.add(new Label("shortcut", user.getShortcutName()));
            }
        });

        setCancelVisible(false);
    }

    @Override
    public IModel<String> getTitle() {
        return new Model<>("Shortcut descriptions");
    }
}
