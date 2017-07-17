---
layout: section
title: General Configuration
---

#### General

#### Default Query Parameters
Default query parameters can be adjusted under `WEB-INF/classes/config-general.json`. For example

{::options parse_block_html="true" /}
{: .n52-example-block}
<div>
<div class="btn n52-example-caption n52-example-toggler active" type="button" data-toggle="button">
Example for a general configuration
</div>
```json
{
  "timeformat": "YYYY-MM-dd, HH:mm",
  "generalizer": {
    "defaultGeneralizer": "lttb",
    "noDataGapThreshold": 5
  },
  "grid": false
}
```
</div>

{:.n52-callout .n52-callout-info}
Changing API defaults may lead to unexpected results to Web clients. Only change defaults 
if you know what you are doing! A use case when to override defaults may be usable for those
parameters which has impact on chart output, e.g. `grid`, `legend`, etc. 

##### Enabling CORS
[CORS](https://enable-cors.org/index.html) can be enabled via third party filter (to stay 
independend from a concrete Servlet container which may not ship/support such filter) within a Web 
application's `web.xml` file. A simple example (which allows all requests) may look like this:

{::options parse_block_html="true" /}
{: .n52-example-block}
<div>
<div class="btn n52-example-caption n52-example-toggler active" type="button" data-toggle="button">
Example for "allowing all" CORS configuration
</div>
```xml
<filter>
    <filter-name>CORS</filter-name>
    <filter-class>com.thetransactioncompany.cors.CORSFilter</filter-class>
    <init-param>
        <param-name>cors.allowOrigin</param-name>
        <param-value>*</param-value>
    </init-param>
    <init-param>
        <param-name>cors.allowGenericHttpRequests</param-name>
        <param-value>true</param-value>
    </init-param>
    <init-param>
        <param-name>cors.supportedMethods</param-name>
        <param-value>GET, POST, HEAD, PUT, DELETE, OPTIONS</param-value>
    </init-param>
    <init-param>
        <param-name>cors.supportedHeaders</param-name>
        <param-value>Content-Type, Content-Encoding, Accept</param-value>
    </init-param>
    <init-param>
        <param-name>cors.exposedHeaders</param-name>
        <param-value>Content-Type, Content-Encoding</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>CORS</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```
</div>

{:.n52-callout .n52-callout-info}
You can [test your CORS config](http://www.test-cors.org/) if configured correctly.
