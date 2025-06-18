package nl.klrnbk.daan.appiecal.packages.security.idp.config.customizer

import io.swagger.v3.oas.models.Operation
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.method.HandlerMethod

class ExpressionCustomizer : OperationCustomizer {
    companion object {
        private const val LINE_BREAK = "<br/>"
        private const val REQUIRED_SCOPES_SUMMARY = "Required scopes expression: <strong>%s</strong>"
    }

    override fun customize(
        operation: Operation,
        handlerMethod: HandlerMethod,
    ): Operation {
        val handlerMethodAnnotation = AnnotationUtils.getAnnotation(handlerMethod.method, PreAuthorize::class.java)
        val beanAnnotation = AnnotationUtils.getAnnotation(handlerMethod.beanType, PreAuthorize::class.java)

        val scopesExpr = handlerMethodAnnotation?.getScopesExpression() ?: beanAnnotation?.getScopesExpression()

        if (scopesExpr != null) {
            val scopesSummary = REQUIRED_SCOPES_SUMMARY.format(scopesExpr)
            if (operation.description != null) {
                operation.description += LINE_BREAK + scopesSummary
            } else {
                operation.description = scopesSummary
            }
        }

        return operation
    }

    private fun PreAuthorize.getScopesExpression(): String =
        value
            .replace(Regex("\\w+,\\s*"), "")
            .replace(Regex("@[a-zA-Z0-1]+\\."), "")
            .replace("'", "")
}
