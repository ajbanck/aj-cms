/*
 * Copyright 2008-2015 Hippo B.V. (http://www.onehippo.com)
 *
 * Licensed under the Apache License, Version 2.0 (the  "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

(function () {
  'use strict';

  var TOP_BAR_HEIGHT = 41,
    BOTTOM_BAR_HEIGHT = 40,
    CONTEXT_LINK_SIZE = 16;

  function byClass (name, tag, root) {
    var found = YAHOO.util.Dom.getElementsByClassName(name, tag, root);
    if (!YAHOO.lang.isUndefined(found.length) && found.length > 0) {
      return found[0];
    }
    return null;
  }

  var Hippo = window.Hippo || {};

  Hippo.Set = function () {
    this.entries = [];
  };

  Hippo.Set.prototype = {
    add: function (entry) {
      this.entries.push(entry);
    },

    remove: function (entry) {
      if (this.entries.length === 0) {
        return null;
      }

      var index = this._getIndex(entry);
      if (index >= 0) {
        var part1 = this.entries.slice(0, index);
        var part2 = this.entries.slice(index + 1);

        this.entries = part1.concat(part2);
        return index;
      }
      return null;
    },

    contains: function (entry) {
      return this._getIndex(entry) >= 0;
    },

    _getIndex: function (entry) {
      for (var i = 0; i < this.entries.length; i++) {
        if (this.entries[i] === entry) {
          return i;
        }
      }
      return -1;
    }
  };

  var menus = new Hippo.Set();

  Hippo.ContextMenu = {
    currentContextLink: null,
    currentContextXY: [0, 0],
    isShowing: false
  };

  Hippo.ContextMenu.init = function () {
    if (document.getElementById('context-menu-container') === null) {
      var x = document.createElement('div');
      x.id = 'context-menu-container';
      document.body.appendChild(x);
    }
  };

  Hippo.ContextMenu.show = function (id) {
    menus.add(id);
  };

  Hippo.ContextMenu.hide = function (id) {
    menus.remove(id);
    var YUID = YAHOO.util.Dom;
    var el = YUID.get('context-menu-container');
    el.innerHTML = '';
    YUID.setXY(el, [-100, -100]);
  };

  Hippo.ContextMenu.isShown = function (id) {
    return menus.contains(id);
  };

  Hippo.ContextMenu.renderInTree = function (id) {
    var x = this.currentContextXY[0] + CONTEXT_LINK_SIZE,
      y = this.currentContextXY[1];
    this.renderAtPosition(id, x, y);
  };

  Hippo.ContextMenu.renderAtPosition = function (id, posX, posY) {
    var YUID = YAHOO.util.Dom;
    var container = YUID.get('context-menu-container');
    var menu = YUID.get(id);
    var ul = YUID.getElementsByClassName('hippo-toolbar-menu-item', 'ul', menu);

    // reset container and append menu for correct size calculation
    YUID.removeClass(menu, 'scrollable-context-menu');
    YUID.setStyle(menu, 'visibility', 'hidden');
    container.innerHTML = '';
    container.appendChild(menu);

    var viewWidth = YUID.getViewportWidth();
    var viewHeight = YUID.getViewportHeight() - BOTTOM_BAR_HEIGHT;
    var region = YUID.getRegion(ul);
    var menuWidth = region[0].width;
    var menuHeight = region[0].height;
    var menuBottom = posY + menuHeight;

    if (menuBottom > viewHeight) {
      posY -= menuBottom - viewHeight;
    }

    if (posY < TOP_BAR_HEIGHT) {
      posY = TOP_BAR_HEIGHT;
      YUID.addClass(menu, 'scrollable-context-menu');
    }

    if (posX + menuWidth > viewWidth) {
      posX -= menuWidth;
    }

    if (posX < 0) {
      posX = 0;
    }

    YUID.setXY(container, [posX, posY]);
    YUID.setStyle(id, 'visibility', 'visible');
  };

  Hippo.ContextMenu.showContextLink = function (id) {
    var YUID = YAHOO.util.Dom;

    if (this.isShowing) {
      return;
    }

    var el = byClass('hippo-tree-dropdown-icon-container', 'a', id);
    if (el !== null) {
      YUID.addClass(el, 'container-selected');

      if (!YUID._canPosition(el)) {
        return;
      }
      var pos = this.getContextPosition(id);
      YUID.setXY(el, pos);
      this.currentContextLink = el;
      this.currentContextXY = pos;
    }
    this.isShowing = true;
  };

  Hippo.ContextMenu.hideContextLink = function (id) {
    var el = this.currentContextLink || byClass('hippo-tree-dropdown-icon-container', 'a', id);
    if (el !== null) {
      YAHOO.util.Dom.removeClass(el, 'container-selected');
    }
    this.isShowing = false;
    this.currentContextLink = null;
  };

  Hippo.ContextMenu.getContextPosition = function (id) {
    var YUID = YAHOO.util.Dom;
    var el = YUID.get(id);

    var unit = this.getLayoutUnit(el);
    if (unit !== null) {
      var layoutRegion = YUID.getRegion(unit.get('element'));
      var myY = YUID.getRegion(el).top + 2;
      var myX = layoutRegion.right - 24;
      var layout = YUID.getAncestorByClassName(el, 'section-center');

      if (layout.scrollHeight > layout.clientHeight) {
        myX -= (YAHOO.hippo.HippoAjax.getScrollbarWidth() - 4);
      }

      return [myX, myY];
    }
  };

  Hippo.ContextMenu.getLayoutUnit = function (el) {
    return YAHOO.hippo.LayoutManager.findLayoutUnit(el);
  };

  // Render the context menu at the stored position
  Hippo.ContextMenu.redraw = function () {
    if (Hippo.ContextMenu.currentContextLink !== null) {
      YAHOO.util.Dom.setX(Hippo.ContextMenu.currentContextLink, Hippo.ContextMenu.currentContextXY[0]);
    }
  };

})();
