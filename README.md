Thandril
=======================
[![Build Status](https://snap-ci.com/AKSW/Thandril/branch/master/build_image)](https://snap-ci.com/AKSW/Thandril/branch/master)
[![License](https://img.shields.io/badge/license-Apache v2-blue.svg)](https://github.com/HTWK-App/BuildingsService/blob/master/LICENSE)
[![Language](https://img.shields.io/badge/language-Scala%20(2.11.7)-blue.svg)](http://www.scala-lang.org/)
[![Framework](https://img.shields.io/badge/framework-PlayFramework%20(2.3.9)-blue.svg)](https://www.playframework.com/)

Thandril is a Web-Application in which users are abel to create workflow pipelines for Linux command line programs.

![Screenshot of Thandril](https://github.com/AKSW/Thandril/raw/master/screenshot.png)

### Using Thandril ###

Once your Server is running all you need to do is open your browser pointing to the host/port you just published and play with the dashboard at your wish. I hope that you have a lot of fun with this application and that it serves it's purpose of making your life easier.

### Environment ###

Thandril was built with a variety of Frameworks and is mainly written in Scala, HTML, JS and CSS. The Webserver itself was built with the [Scala Play-Framework](//www.playframework.com/). The Website was built mainly with [Twitter Bootstrap](//getbootstrap.com/), [JQuery](//jquery.com/) and [FontAwesome](//fortawesome.github.io/Font-Awesome/)

### Compilation/Running the Server  ###

All you have to do is to switch into the projects main directory and subsequent execute the following commands:

- ./activator update
- ./activator run

If you wanna package the Application and deploy it to some kind of Server-System, please have a look at the [Activator documentation](//typesafe.com/activator/docs).

### Tested Platforms ###

Thandril is developed under Fedora 22 and thereby well tested on this platform. It should also run on all other Linux based distributions, in which installed programs are located at /usr/bin.
