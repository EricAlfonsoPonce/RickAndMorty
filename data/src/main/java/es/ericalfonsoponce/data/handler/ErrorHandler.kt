package es.ericalfonsoponce.data.handler

interface ErrorHandler {
    fun handle(exception: Exception): Exception
}