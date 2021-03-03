# zuul2-wms-bbox-gateway

## Layer 7 gateway

Parse BBOX Parameter and do load balancing geoserver nodes.

This example not use Eureka.

## WMS request

You can find WMS URI specification [here](https://docs.geoserver.org/stable/en/user/services/wms/reference.html).

### Query String Parameters

Example request parameters are 

    SERVICE: WMS
    VERSION: 1.3.0
    REQUEST: GetMap
    FORMAT: image/png
    TRANSPARENT: true
    LAYERS: test_workspace:test_layer
    TILED: true
    viewparams: 
    env: 
    WIDTH: 256
    HEIGHT: 256
    CRS: EPSG:3857
    STYLES: 
    BBOX: 14164752.18857422,4505052.996284179,14169644.158383789,4509944.96609375

### How can i distribute geospatial request?

You can edit [application.properties](#) file, if you want to add routing nodes. 

like this.

` geoserver-0.ribbon.client.NIWSServerListClassName=com.netflix.loadbalancer.ConfigurationBasedServerList`

` geoserver-1.ribbon.client.NIWSServerListClassName=com.netflix.loadbalancer.ConfigurationBasedServerList`

This example routing nodes by `x min(xm)` geospatial parameter values on CRS `EPSG:3857`

For example,

` xm > 14151299 ? geoserver-0 : geoserver-1`

    /geoserver
    ├─ /geoserver-0
    └─ /geoserver-1

