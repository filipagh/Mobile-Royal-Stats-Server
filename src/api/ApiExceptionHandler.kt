package api

import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
open class ApiExceptionHandler : ExceptionMapper<ApiException> {
    override fun toResponse(exception: ApiException?): Response {
        if (exception != null) {
            return Response.status(exception.code).entity("{\"message\":\"$exception\"}").build()
        }
        return Response.serverError().build()
    }
}