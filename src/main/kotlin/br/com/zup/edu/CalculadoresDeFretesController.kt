package br.com.zup.edu


import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.protobuf.StatusProto
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.http.exceptions.HttpStatusException
import javax.inject.Inject

@Controller
class CalculadoresDeFretesController(@Inject val grpcClient: FretesServiceGrpc.FretesServiceBlockingStub) {

    @Get("/calcula/frete")
    fun calculaFreste(@QueryValue cep: String): FreteResponse{

        val request = CalculaFreteRequest.newBuilder()
            .setCep(cep)
            .build()

        try {
            val response = grpcClient.calculaFrete(request)

            return FreteResponse(response.cep, response.valor)
        }catch (e: StatusRuntimeException){
            val status = e.status
            val statusCode = status.code
            val description = status.description

            if (statusCode == Status.Code.INVALID_ARGUMENT){
                throw HttpStatusException(HttpStatus.BAD_REQUEST, description)
            }
            if (statusCode == Status.Code.PERMISSION_DENIED){
                val statusProto =
                    StatusProto.fromThrowable(e) ?: throw HttpStatusException(HttpStatus.FORBIDDEN, description)

                val anyDetails = statusProto.detailsList?.get(0)
                val erroDetails = anyDetails!!.unpack(ErroDatails::class.java)

                throw HttpStatusException(HttpStatus.FORBIDDEN, "${erroDetails.code}: ${erroDetails.message}")
            }
            throw HttpStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.message)
        }
    }
}

class FreteResponse(val cep: String, val valor: Double)