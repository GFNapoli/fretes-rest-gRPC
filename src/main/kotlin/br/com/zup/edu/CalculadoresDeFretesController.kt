package br.com.zup.edu

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import javax.inject.Inject

@Controller
class CalculadoresDeFretesController(@Inject val grpcClient: FretesServiceGrpc.FretesServiceBlockingStub) {

    @Get("/calcula/frete")
    fun calculaFreste(@QueryValue cep: String): FreteResponse{

        val request = CalculaFreteRequest.newBuilder()
            .setCep(cep)
            .build()

        val response = grpcClient.calculaFrete(request)

        return FreteResponse(response.cep, response.valor)
    }
}

class FreteResponse(val cep: String, val valor: Double)