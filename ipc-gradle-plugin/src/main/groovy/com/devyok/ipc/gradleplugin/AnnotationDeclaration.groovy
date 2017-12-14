package com.devyok.ipc.gradleplugin

import com.github.javaparser.ast.expr.MemberValuePair

class AnnotationDeclaration {

    def packageName
    def className
    def memberValuePairs
    def processName
    def serviceName
    def compileUnit
    def isExported

    def getProcessName(){

        if(processName!=null){
            return processName
        }

        this.processName = getValue("processName")

        return this.processName
    }

    def getServiceName(){

        if(serviceName!=null){
            return serviceName
        }

        this.serviceName = getValue("serviceName")

        if(this.serviceName == null){
            return false
        }

        return this.serviceName
    }

    def removeQuotes(def value) {
        if(value.length()==2){
            return "";
        }
        return value.substring(1, value.length()-1);
    }

    def isExported(){
        if(isExported!=null){
            return isExported
        }
        this.isExported = getValue("isExported")

        if(this.isExported == null){
            return false
        }

        return this.isExported
    }

    def getValue(String methodName){
        for (MemberValuePair memberValuePair : memberValuePairs) {
            String name = memberValuePair.getName()
            String value = memberValuePair.getValue().toString()

            if(methodName.equals(name)) {
                int result = value.indexOf("\"")
                if(result!=-1){
                    value = removeQuotes(value)
                }
                return value
            }
        }

        return null
    }

    public String toString(){

        List<MemberValuePair> list = memberValuePairs
        String result = ""
        if(list!=null){
            for (MemberValuePair memberValuePair : list) {
                String name = memberValuePair.getName()
                String value = memberValuePair.getValue().toString()
                result+="name="+name+",value="+value+";"
            }
        }

        return "packageName=${packageName},className=${className},pairs={${result}},processName=${processName},serviceName=${serviceName}"
    }

}
