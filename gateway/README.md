# OpenMRS Gateway

The gateway service is a simple Nginx Docker container that routes requests either to the frontend or the backend as appropriate. Using a service like this enables us to largely ignore CORS issues since both the backend and frontend are served from the same origin.

The main configuration for the gateway can be found in the default.conf.template file. this file is processed at start-up by the NGinx Docker containers envsubst setup, which allows us to substitute  environment variables into the configuration.

## Supported Environment Variables

`FRAME_ANCESTORS`
: This should be a space separated list of origins that are allowed to embed OpenMRS in an IFRAME. For example "http://my.webpage/com http://my.webpage2.com". The syntax is described [on MDN](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy/frame-ancestors). By default, only pages served from the gateway can embed OpenMRS in an IFRAME.
