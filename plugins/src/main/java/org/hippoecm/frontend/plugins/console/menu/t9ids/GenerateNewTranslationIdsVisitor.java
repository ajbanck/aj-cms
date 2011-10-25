/*
 *  Copyright 2011 Hippo.
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
package org.hippoecm.frontend.plugins.console.menu.t9ids;

import java.util.UUID;

import javax.jcr.ItemNotFoundException;
import javax.jcr.ItemVisitor;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.Property;
import javax.jcr.RepositoryException;

import org.hippoecm.repository.api.HippoNode;

public class GenerateNewTranslationIdsVisitor implements ItemVisitor {

    @Override
    public void visit(Property property) throws RepositoryException {
        if (property.getName().equals("hippotranslation:id")) {
            property.setValue(UUID.randomUUID().toString());
        }
    }

    @Override
    public void visit(Node node) throws RepositoryException {
        if (!isVirtual(node)) {
            if (node.hasProperty("hippotranslation:id")) {
                visit(node.getProperty("hippotranslation:id"));
            }
            NodeIterator children = node.getNodes();
            while (children.hasNext()) {
                children.nextNode().accept(this);
            }
        }
    }
    
    private boolean isVirtual(Node node) {
        if (!(node instanceof HippoNode)) {
            return false;
        }
        try {
            HippoNode hippoNode = (HippoNode) node;
            Node canonical = hippoNode.getCanonicalNode();
            if (canonical == null) {
                return true;
            }
            return !canonical.isSame(hippoNode);
        } catch (ItemNotFoundException e) {
            // canonical node no longer exists
            return true;
        } catch (RepositoryException e) {
            return false;
        }
    }

}
