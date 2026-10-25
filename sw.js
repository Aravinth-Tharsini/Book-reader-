const CACHE = "bookreader-cache";
const FILES = ["./","index.html","pdf.min.js","pdf.worker.min.js","page-flip.browser.js"];
self.addEventListener("install", e => { self.skipWaiting(); e.waitUntil(caches.open(CACHE).then(c => c.addAll(FILES))); });
self.addEventListener("activate", e => e.waitUntil(self.clients.claim()));
// Network first: online = always newest version from GitHub; offline = saved copy
self.addEventListener("fetch", e => {
  if (e.request.method !== "GET") return;
  e.respondWith(
    fetch(e.request,{cache:"no-cache"}).then(r => {
      const copy = r.clone();
      caches.open(CACHE).then(c => c.put(e.request, copy));
      return r;
    }).catch(() => caches.match(e.request))
  );
});
