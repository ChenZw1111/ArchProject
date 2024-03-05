package com.example.asproj.restful

import android.util.Log
import com.example.asproj.restful.annotation.BaseUrl
import com.example.asproj.restful.annotation.CacheStrategy
import com.example.asproj.restful.annotation.DELETE
import com.example.asproj.restful.annotation.Filed
import com.example.asproj.restful.annotation.GET
import com.example.asproj.restful.annotation.Headers
import com.example.asproj.restful.annotation.HiCall
import com.example.asproj.restful.annotation.POST
import com.example.asproj.restful.annotation.PUT
import com.example.asproj.restful.annotation.Path
import java.lang.IllegalStateException
import java.lang.reflect.*


class MethodParser(
    private val baseUrl: String,
    method: Method,
    args: Array<Any>
) {
    private var replaceRelativeUrl: String? = null
    private var cacheStrategy: Int = CacheStrategy.NET_ONLY
    private var domainUrl: String? = null
    private var formPost: Boolean = true
    private var httpMethod: Int = -1
    private lateinit var relativeUrl: String
    private lateinit var returnType: Type
    private var headers: MutableMap<String, String> = mutableMapOf()
    private var paramters: MutableMap<String, String> = mutableMapOf()

    init {
        parseMethodAnnotations(method)

        parseMethodReturnType(method)
        parseMethodParameters(method, args)
    }

    private fun parseMethodAnnotations(method: Method) {
        val annotations = method.annotations
        for(annotation in annotations){
            if(annotation is GET){
                relativeUrl = annotation.value
                httpMethod = HiRequest.METHOD.GET
            }else if(annotation is POST){
                relativeUrl = annotation.value
                httpMethod = HiRequest.METHOD.POST
                formPost = annotation.formPost
            }else if(annotation is PUT){
                relativeUrl = annotation.value
                httpMethod = HiRequest.METHOD.PUT
                formPost = annotation.formPost
            }else if(annotation is DELETE){
                httpMethod = HiRequest.METHOD.DELETE
                relativeUrl = annotation.value
            }else if(annotation is Headers){
                val headersArray = annotation.value
                for(header in headersArray){
                    val colon = header.indexOf(":")
                    check(!(colon == 0 || colon == -1)){
                        String.format(
                            "@headers value must be in the form [name:value], but found [%s]",
                            header
                        )
                    }
                    val name = header.substring(0,colon)
                    val value = header.substring(colon + 1).trim()
                    headers[name] = value
                }
            }else if(annotation is BaseUrl){
                domainUrl = annotation.value
            }else if(annotation is CacheStrategy){
                cacheStrategy = annotation.value
            }else{
                throw IllegalStateException("cannot handle method annotation:" +
                annotation.javaClass.toString())
            }
        }

        require((httpMethod == HiRequest.METHOD.GET)
                ||(httpMethod == HiRequest.METHOD.POST)
                ||(httpMethod == HiRequest.METHOD.PUT)
                ||(httpMethod == HiRequest.METHOD.DELETE)){
            String.format("method %s must has one of GET,POST,PUT,DELETE",method.name)
        }

        if(domainUrl == null){
            domainUrl = baseUrl
        }
    }

    private fun parseMethodReturnType(method: Method) {
        if (method.returnType != HiCall::class.java) {
            throw IllegalStateException(
                String.format(
                    "method %s must be type of HiCall.class",
                    method.name
                )
            )
        }
        val genericReturnType = method.genericReturnType
        if (genericReturnType is ParameterizedType) {
            val actualTypeArguments = genericReturnType.actualTypeArguments
            require(actualTypeArguments.size == 1) {
                "method %s can only has one generic return type"
            }
            val argument = actualTypeArguments[0]
            require(validateGenericType(argument)){
                String.format("method %s generic return type must not be an unknown type."+
                method.name)
            }
            Log.e("Exception",argument.toString())
            returnType = argument
        }else{
            throw IllegalStateException(
                String.format("method %s must has one generic return type",method.name)
            )
        }
    }

    private fun validateGenericType(type: Type): Boolean {
        if (type is GenericArrayType) {
            return validateGenericType(type.genericComponentType)
        }
        if (type is TypeVariable<*>) {
            return false
        }
        if (type is WildcardType) {
            return false
        }
        return true
    }

    private fun isPrimitive(value:Any):Boolean{
        if(value.javaClass == String::class.java){
            return true
        }
        try{
            val field = value.javaClass.getField("TYPE")
            val clazz = field[null] as Class<*>
            if(clazz.isPrimitive) return true
        }catch (e:IllegalAccessException){
            e.printStackTrace()
        }catch (e:NoSuchFileException){
            e.printStackTrace()
        }
        return false
    }

    fun newRequest(method:Method,args:Array<out Any>?):HiRequest{
        val arguments: Array<Any> = args as Array<Any>? ?: arrayOf()
//        parseMethodParam
        val request = HiRequest()
        request.domainUrl = domainUrl
        request.returnType = returnType
        request.relativeUrl = replaceRelativeUrl?:relativeUrl
        request.parameters = paramters
        request.headers = headers
        request.httpMethod = httpMethod
        request.formPost = formPost
        request.cacheStrategy = cacheStrategy
        return request
    }

    private fun parseMethodParameters(method: Method,args:Array<Any>){
        paramters.clear()

        //@Path("province") province: Int,@Filed("page") page: Int
        val parameterAnnotations = method.parameterAnnotations
        require(parameterAnnotations.size == args.size){
            String.format(
                "arguments annotations count %s dont match expect count %s",
                parameterAnnotations.size,
                args.size
            )
        }

        for(index in args.indices){
            val annotations = parameterAnnotations[index]
            require(annotations.size <=1){
                "filed can only has one annotation :index =$index"
            }

            val value = args[index]
            require(isPrimitive(value)){
                "8 basic types are supported for now,index=$index"
            }

            val annotation = annotations[0]
            if(annotation is Filed){
                val key = annotation.value
                val value = args[index]
                paramters[key] = value.toString()
                Log.e("paramters",paramters.toString())
            }else if(annotation is Path){
                val replaceName = annotation.value
                val replacement = value.toString()
                if(replacement != null && replaceName != null){
                    replaceRelativeUrl = relativeUrl.replace("{$replaceName}",replacement)
                }
            }else if(annotation is CacheStrategy){
                cacheStrategy = value as Int
            }else{
                throw IllegalStateException("cannot handle parameter annotation:"+
                annotation.javaClass.toString())
            }
        }

    }

    companion object{
        fun parse(baseUrl: String,method: Method,args: Array<Any>):MethodParser{
            return MethodParser(baseUrl, method,args)
        }
    }
}
