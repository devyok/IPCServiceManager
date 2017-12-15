package com.devyok.ipc.gradleplugin

import com.android.build.gradle.AppExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

public class IPCPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        def android = project.extensions.getByType(AppExtension)

        android.applicationVariants.all { variant ->

            variant.outputs.each { output ->

                def newManifest = ''

                output.processManifest.doFirst {

                    Logger.DEBUG = project.ipc.debug

                    variant.sourceSets.each {
                        sourceProvider ->
                            long start = System.currentTimeMillis()
                            println "start parse annotations"
                            sourceProvider.getJavaDirectories().each {
                                file ->

                                    Logger.debug("******* generateComponentResult = ${file.getAbsolutePath()} , variant = ${variant.getName()}")

                                    def list = AnnotationParser.parserAnnotations(file)

                                    if(list!=null && list.size() > 0){
                                        def generateComponentResult = ComponentsGenerator.generateComponent(null,list)

                                        if(generateComponentResult!=null){
                                            newManifest = newManifest + generateComponentResult
                                        }

                                        Logger.debug("******* generateComponentResult = ${generateComponentResult}")
                                    }

                            }

                            def testJavaDir = project.ipc.testJavaDir

                            Logger.debug("******* test java dir = " + testJavaDir)

                            if(testJavaDir!=null){
                                AnnotationParser.parserAnnotations(new File(testJavaDir))
                            }

                            long end = System.currentTimeMillis()
                            def timeResult = end - start
                            Logger.info "parser annotation use time ${timeResult}ms"
                    }
                }

                output.processManifest.doLast {

                    def list = new ArrayList()
                    list.add(createDefaultIPCServer())
                    def generateComponentResult = ComponentsGenerator.generateComponent(null,list)

                    if(generateComponentResult!=null){
                        newManifest = newManifest+generateComponentResult
                    }

                    Logger.debug("******* will be written to manifest file")

                    if(newManifest == null || "".equals(newManifest)) {

                        Logger.info "nothing ipc service write to manifest file"

                        return
                    }

                    output.processManifest.outputs.files.each { File file ->
                        def manifestFile = null;
                        if ((file.name.equalsIgnoreCase("AndroidManifest.xml") && !file.isDirectory()) || file.isDirectory()) {
                            if (file.isDirectory()) {
                                manifestFile = new File(file, "AndroidManifest.xml")
                            } else {
                                manifestFile = file
                            }
                            if (manifestFile != null && manifestFile.exists()) {
                                Logger.debug("******* manifest: ${manifestFile}")
                                def updatedContent = manifestFile.getText("UTF-8").replaceAll("</application>", newManifest + "</application>")
                                manifestFile.write(updatedContent, 'UTF-8')
                            }
                        }
                    }
                }
            }

        }

        project.getExtensions().create('ipc',IPCExtension.class)

        Logger.info("******* IPCPlugin enter")

    }

    def createDefaultIPCServer(){
        def server = new AnnotationDeclaration()
        server.isExported = true
        server.serviceName = 'ipc_servicemanager'
        server.processName = 'svcmgr'
        server.packageName = 'com.devyok.ipc'
        server.className = 'ServiceManagerProvider'
        return server
    }

}