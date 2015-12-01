/*
 * Copyright 2015 Hippo B.V. (http://www.onehippo.com)
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
(function () {
  "use strict";

  var MAX_INACTIVE_INTERVAL_MS = parseInt("${maxInactiveIntervalSeconds}", 10) * 1000,
    AJAX_ATTR_SYSTEM_ACTIVITY = "${ajaxAttrSystemActivity}",
    lastActive = Date.now(),
    callbacks = [];

  Hippo.UserActivity = {

    report: function () {
      lastActive = Date.now();
    },

    onInactive: function(callback) {
      callbacks.push(callback);
    },

    unInactive: function(callback) {
      var index = callbacks.indexOf(callback);
      if (index >= 0) {
        callbacks.splice(index, 1);
      }
    }

  };

  function isUserActivity(ajaxOptions) {
    return ajaxOptions[AJAX_ATTR_SYSTEM_ACTIVITY] !== true;
  }

  function monitorWicketAjaxUserCalls () {
    Wicket.Event.subscribe(Wicket.Event.Topic.AJAX_CALL_BEFORE_SEND, function (attrs, jqXHR) {
      if (isUserActivity(jqXHR)) {
        Hippo.UserActivity.report();
      }
    });
  }

  function monitorExtAjaxUserCalls() {
    Ext.Ajax.on('beforerequest', function (component, ajaxOptions) {
      if (ajaxOptions.request && isUserActivity(ajaxOptions.request.arg)) {
        Hippo.UserActivity.report();
      }
    });
  }

  function reportInactivity() {
    callbacks.forEach(function(callback) {
      try {
        callback();
      } catch (e) {
        console.warn("Error while executing user inactivity callback", e, callback);
      }
    })
  }

  function observeInactivity() {
    var now = Date.now(),
      msSinceLastActivity = now - lastActive,
      msToNextCheck;

    if (msSinceLastActivity > MAX_INACTIVE_INTERVAL_MS) {
      reportInactivity();
    } else {
      msToNextCheck = MAX_INACTIVE_INTERVAL_MS - msSinceLastActivity;
      window.setTimeout(observeInactivity, msToNextCheck);
    }
  }

  monitorWicketAjaxUserCalls();
  monitorExtAjaxUserCalls();
  observeInactivity();

}());
