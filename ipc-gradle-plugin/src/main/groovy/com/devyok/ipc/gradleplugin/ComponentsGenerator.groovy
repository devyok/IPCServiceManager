package com.devyok.ipc.gradleplugin

import groovy.xml.MarkupBuilder

class ComponentsGenerator {

    def static final androidName = 'android:name'
    def static final androidProcess = 'android:process'
    def static final androidValue = 'android:value'
    def static final androidExported = 'android:exported'
    def static final androidAuthorities = 'android:authorities'

    def static generateComponent(def config, def annotationDecList){

        def writer = new StringWriter()
        def xml = new MarkupBuilder(writer)

        xml.application {

            annotationDecList.each {

                AnnotationDeclaration cad = it;

                def className = "${cad.packageName}.${cad.className}"
                def processName = cad.getProcessName()
                def serviceName = cad.getServiceName()
                def isExported = cad.isExported()

                println "provider = {${className},${processName},${serviceName},${isExported}"

                provider(
                        "${androidName}":className,
                        "${androidProcess}":":${processName}",
                        "${androidExported}":isExported,
                        "${androidAuthorities}":serviceName) {

                    metadata(
                            "${androidName}":"ipc",
                            "${androidValue}":"service")
                }
            }

        }

        def xmlString = writer.toString()

        Logger.debug "xml result = ${xmlString}"

        def result = xmlString.replace("<application>", "").replace("</application>", "")

        result = result.replace("metadata", "meta-data")

        Logger.debug "replace xml final result = ${result}"

        return result

    }

}