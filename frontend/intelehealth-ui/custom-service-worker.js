importScripts("./ngsw-worker.js");

(function () {
  "use strict";
  console.log("custom-worker-loaded.1.1");

  self.addEventListener("notificationclick", (event) => {
    console.log("event: ", event);
    if (clients.openWindow && event.notification.data.url) {
      event.waitUntil(
        clients
          .matchAll({
            type: "window",
          })
          .then(function (clientList) {
            const data = event.notification.data;
            if (data.url) {
              for (var i = 0; i < clientList.length; i++) {
                var client = clientList[i];
                if ("focus" in client) {
                  client.focus();
                  // try to navigate to the url in the focused client
                  client.navigate(data.url).then((response) => {
                    event.notification.close();
                  });
                  break;
                } else {
                  clients.openWindow(data.url);
                  event.notification.close();
                }
              }
              if (!clientList.length) {
                clients.openWindow(data.url);
              }
            }
          })
      );
    }
  });
})();
