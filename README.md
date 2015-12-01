Galeb - Router Service
===========================
[![Circle CI](https://circleci.com/gh/galeb/galeb-router.svg?style=svg)](https://circleci.com/gh/galeb/galeb-router)

Galeb is a dynamic software router built on JBOSS Undertow and XNIO.<br/>
It's a massively parallel routing system running a shared-nothing architecture.

Its main features are:
* Open Source
* API REST (management)
* Allows dynamically change routes and configuration without having to restart or reload
* Highly scalable
* Masterless (SNA - Shared nothing architecture)
* Sends metrics to external counters (eg statsd)

Using
-----

<code>
$ mvn clean install
</code><br/>
<code>
$ mvn exec:exec
</code><br/>

