package nl.klrnbk.daan.appiecal.packages.common.responses.error

class ErrorResponse(
    val status: Int,
    val type: String,
    val message: String,
    val detail: String,
    val instance: String,
) {
    companion object {
        const val UNAUTHORIZED = """
            {
              "status": 401,
              "type": "UNAUTHORIZED",
              "message": "You have to be logged in to do this.",
              "detail": "No x-authorization header is present.",
              "instance": "/v1/example/endpoint"
            }
        """

        const val BAD_REQUEST = """
            {
              "status": 400,
              "type": "BAD_REQUEST",
              "message": "Client sent a bad request.",
              "detail": "Client sent a bad request.",
              "instance": "/v1/example/endpoint"
            }
        """

        const val NOT_FOUND = """
            {
                "status": 404,
                "type": "NOT_FOUND",
                "message": "Resource not found.",
                "detail": "Resource not found.",
                "instance": "/v1/example/endpoint"
            }
        """

        const val INTERNAL_SERVER_ERROR = """
            {
                "status": 500,
                "type": "INTERNAL_SERVER_ERROR",
                "message": "An unexpected error occurred.",
                "detail": "An unexpected error occurred.",
                "instance": "/v1/example/endpoint"
            }
        """

        const val BAD_GATEWAY = """
            {
                "status": 502,
                "type": "BAD_GATEWAY",
                "message": "Bad gateway.",
                "detail": "Bad gateway.",
                "instance": "/v1/example/endpoint"
            }
        """

        const val METHOD_NOT_ALLOWED = """
            {
                "status": 405,
                "type": "METHOD_NOT_ALLOWED",
                "message": "Method not allowed.",
                "detail": "Method not allowed.",
                "instance": "/v1/example/endpoint"
            }
        """

        const val NOT_ACCEPTABLE = """
            {
                "status": 406,
                "type": "NOT_ACCEPTABLE",
                "message": "Not acceptable",
                "detail": "Not acceptable",
                "instance": "/v1/example/endpoint"
            }
        """

        const val CONFLICT = """
            {
                "type": "CONFLICT",
                "status": 409,
                "message": "Conflict",
                "detail": "Conflict",
                "instance": "/v1/example/endpoint"
            }
        """

        const val UNSUPPORTED_MEDIA_TYPE = """
            {
                "status": 415,
                "type": "UNSUPPORTED_MEDIA_TYPE",
                "message": "Unsupported media type",
                "detail": "Unsupported media type",
                "instance": "/v1/example/endpoint"
            }
        """

        const val RATE_LIMIT = """
            {
                "status": 429,
                "type": "RATE_LIMIT",
                "message": "Pace your requests. Read the Rate limit guide.",
                "detail": "Rate limit reached for requests",
                "instance": "/v1/example/endpoint"
            }
        """

        const val SERVER_ERROR = """
            {
                "status": 500,
                "type": "SERVER_ERROR",
                "message": "Retry your request after a brief wait and contact us if the issue persists. Check the status page.",
                "detail": "The server had an error while processing your request",
                "instance": "/v1/example/endpoint"
            }
        """

        const val ENGINE_OVERLOADED = """
            {
                "status": 503,
                "type": "ENGINE_OVERLOADED",
                "message": "Our servers are experiencing high traffic. Please retry your requests after a brief wait.",
                "detail": "The engine is currently overloaded, please try again later",
                "instance": "/v1/example/endpoint"
            }
        """
    }
}
