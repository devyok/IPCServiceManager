package com.devyok.ipc.gradleplugin

import com.github.javaparser.JavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.PackageDeclaration
import com.github.javaparser.ast.body.TypeDeclaration
import com.github.javaparser.ast.expr.AnnotationExpr
import com.github.javaparser.ast.expr.MemberValuePair
import com.github.javaparser.ast.expr.NormalAnnotationExpr

class AnnotationParser {

    ArrayList classAnnotationDeclarations = new ArrayList()

    public List getResult(){
        return classAnnotationDeclarations
    }

    def static parserAnnotations(File dir){
        AnnotationParser annotationParser = new AnnotationParser()
        annotationParser.parseAnnotations(dir,"IPCConfig")

        def list = annotationParser.getResult()

        list.each {
            Logger.debug "******* ClassAnnotationDeclaration = " + it
        }
        return list;
    }

    public AnnotationDeclaration parseAnnotations(File file, String annotationName) throws Exception {

        if(!file.isDirectory()) {
            String fileName = file.getName()

            Logger.debug "parseAnnotations file = ${fileName}"

            if(fileName.contains(".java")){
                def classAnnotationDeclaration = parseAnnotation(file,annotationName)
                Logger.debug "classAnnotationDeclaration type = " + classAnnotationDeclaration.getClass().getName()
                Logger.debug "classAnnotationDeclaration = " + classAnnotationDeclaration

                try {
                    if(classAnnotationDeclaration!=null){
                        classAnnotationDeclarations.add(classAnnotationDeclaration)
                    }
                } catch(Exception e){
                    //ignore
                }

            }

        } else if(file.isDirectory()){

            File[] files = file.listFiles()

            for(int i = 0;i < files.length;i++){
                parseAnnotations(files[i],annotationName)
            }

        }
    }

    public AnnotationDeclaration parseAnnotation(File javaFile, String annotationName) throws Exception {

        AnnotationDeclaration serviceDeclaration = new AnnotationDeclaration()

        CompilationUnit compileUnit = JavaParser.parse(javaFile)

        if(compileUnit == null){
            Logger.info "unable parse java file"
            return null;
        }

        serviceDeclaration.compileUnit = compileUnit;

        PackageDeclaration packageDeclaration = compileUnit.getPackage()

        if(packageDeclaration==null){
            Logger.info "unable parse java package"
            return null;
        }

        String pacakgeName = packageDeclaration.getPackageName()
        serviceDeclaration.packageName = pacakgeName;

        Logger.debug "annotation packageName = ${pacakgeName}"

        List<TypeDeclaration> list = compileUnit.getTypes()

        if(list == null){
            Logger.info "unable parse TypeDeclaration"
            return null
        }

        for (int i = 0; i < list.size(); i++) {

            TypeDeclaration typeDeclaration = list.get(i)

            String className = typeDeclaration.getName()
            serviceDeclaration.className = className

            List<AnnotationExpr> annotationExprs = typeDeclaration.getAnnotations()

            if(annotationExprs == null){
                Logger.info "annotation not found"
                return null;
            }

            for(AnnotationExpr ae : annotationExprs){

                if(ae instanceof NormalAnnotationExpr) {
                    NormalAnnotationExpr normalAnnotationExpr = (NormalAnnotationExpr) ae

                    if(normalAnnotationExpr.getName().getName().equals(annotationName)){
                        List<MemberValuePair> memberValuePairs = normalAnnotationExpr.getPairs()
                        serviceDeclaration.memberValuePairs =  memberValuePairs
                        return serviceDeclaration
                    }
                }
            }
        }

        if(serviceDeclaration.memberValuePairs==null || serviceDeclaration.memberValuePairs.size()==0){
            return null
        }

        return serviceDeclaration
    }

}