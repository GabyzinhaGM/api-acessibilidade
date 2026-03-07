package com.gaby.api_acessibilidade.exception

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException::class)
    fun tratarRecursoNaoEncontrado(
            ex: RecursoNaoEncontradoException,
            request: HttpServletRequest
    ): ResponseEntity<ErroResponse> {
        val erro = ErroResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.NOT_FOUND.value(),
                erro = HttpStatus.NOT_FOUND.reasonPhrase,
                mensagem = ex.message ?: "Recurso não encontrado",
                caminho = request.requestURI
        )

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun tratarErroValidacao(
            ex: MethodArgumentNotValidException,
            request: HttpServletRequest
    ): ResponseEntity<ErroResponse> {
        val mensagem = ex.bindingResult.fieldErrors
                .joinToString("; ") { "${it.field}: ${it.defaultMessage}" }

        val erro = ErroResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                erro = HttpStatus.BAD_REQUEST.reasonPhrase,
                mensagem = mensagem,
                caminho = request.requestURI
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro)
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun tratarConstraintViolation(
            ex: ConstraintViolationException,
            request: HttpServletRequest
    ): ResponseEntity<ErroResponse> {
        val mensagem = ex.constraintViolations
                .joinToString("; ") { it.message }

        val erro = ErroResponse(
                timestamp = java.time.LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                erro = HttpStatus.BAD_REQUEST.reasonPhrase,
                mensagem = mensagem,
                caminho = request.requestURI
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro)
    }

    @ExceptionHandler(ParametroInvalidoException::class)
    fun tratarParametroInvalido(
            ex: ParametroInvalidoException,
            request: HttpServletRequest
    ): ResponseEntity<ErroResponse> {
        val erro = ErroResponse(
                timestamp = java.time.LocalDateTime.now(),
                status = HttpStatus.BAD_REQUEST.value(),
                erro = HttpStatus.BAD_REQUEST.reasonPhrase,
                mensagem = ex.message ?: "Parâmetro inválido",
                caminho = request.requestURI
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro)
    }

    @ExceptionHandler(Exception::class)
    fun tratarErroGenerico(
            ex: Exception,
            request: HttpServletRequest
    ): ResponseEntity<ErroResponse> {
        val erro = ErroResponse(
                timestamp = LocalDateTime.now(),
                status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
                erro = HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase,
                mensagem = ex.message ?: "Erro interno no servidor",
                caminho = request.requestURI
        )

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro)
    }
}