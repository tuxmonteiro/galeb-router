OpenVRaaS - Core Libraries
===========================

OpenVRaaS is a dynamic software router built on JBOSS Undertow and XNIO.<br/>
It's a massively parallel routing system running a shared-nothing architecture.

Its main features are:
* Open Source
* API REST (management)
* Allows dynamically change routes and configuration without having to restart or reload
* Highly scalable
* Masterless (SNA - Shared nothing architecture)
* Sends metrics to external counters (eg statsd)
* Webhooks support

<strong>IMPORTANT</strong>: Different of the Galeb project, the OpenVRaaS project hasn't the pretension to be a solution for production use, but a laboratory concepts.

If you need a production solution, please visit the Galeb project:
http://galeb.io

Using
-----

<code>
$ mvn clean install
</code><br/>

