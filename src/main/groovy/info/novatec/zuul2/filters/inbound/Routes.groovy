package info.novatec.zuul2.filters.inbound

import com.netflix.zuul.context.SessionContext
import com.netflix.zuul.filters.http.HttpInboundSyncFilter
import com.netflix.zuul.message.http.HttpRequestMessage
import com.netflix.zuul.netty.filter.ZuulEndPointRunner
import info.novatec.zuul2.filters.endpoint.NotFoundEndpoint

import org.apache.commons.lang3.StringUtils

/**
 * Routing filter on base of HttpInboundSyncFilter.<br/>
 * URIs of the proxied backend services are defined in {@code application.properties}
 */
class Routes extends HttpInboundSyncFilter {

    @Override
    HttpRequestMessage apply(HttpRequestMessage request) {
        SessionContext context = request.getContext()


        String uri = request.getPath()

        System.out.println(uri)

        Integer num = uri.indexOf("/", 1)
        if(num > 0) {
            uri  = uri.substring(0, uri.indexOf("/", 1))
        }

        switch (uri) {
            case "/some-service-1":
                context.setEndpoint(ZuulEndPointRunner.PROXY_ENDPOINT_FILTER_NAME)
                context.setRouteVIP("some-service-1")
                break

            case "/some-service-2":
                context.setEndpoint(ZuulEndPointRunner.PROXY_ENDPOINT_FILTER_NAME)
                context.setRouteVIP("some-service-2")
                break

            case "/geoserver":
                context.setEndpoint(ZuulEndPointRunner.PROXY_ENDPOINT_FILTER_NAME)

                String paramStr = request.getQueryParams().getFirst("BBOX")

                if(StringUtils.isBlank(paramStr)) {
                    context.setEndpoint(NotFoundEndpoint.class.getCanonicalName())
                    return request
                }

                String [] paramArr = paramStr.split(",");
                Float xm = Float.parseFloat(paramArr[0]);
                Float ym = Float.parseFloat(paramArr[1]);
                Float xM = Float.parseFloat(paramArr[2]);
                Float yM = Float.parseFloat(paramArr[3]);

                if(xm > 14151299) {
                    context.setRouteVIP("geoserver-0")
                } else {
                    context.setRouteVIP("geoserver-1")
                }

                System.out.println(uri)

                break

            default:
                context.setEndpoint(NotFoundEndpoint.class.getCanonicalName())
        }
        return request
    }

    @Override
    int filterOrder() {
        return 0
    }

    @Override
    boolean shouldFilter(HttpRequestMessage request) {
        return true
    }
}
