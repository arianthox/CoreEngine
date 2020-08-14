package com.arianthox.predictor.adapter;


import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.common.EntityStreamingSupport;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.MediaRanges;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.model.headers.Accept;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import com.arianthox.predictor.model.DrawDataDTO;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Component
@Log
public class DrawConnectorAdapter {

    @Value("${draw.url}")
    private String url;

    private ActorSystem system = ActorSystem.create();
    private Materializer materializer = ActorMaterializer.create(system);
    private Http http = Http.get(system);
    private EntityStreamingSupport support = EntityStreamingSupport.json();
    private Unmarshaller unmarshal = Jackson.byteStringUnmarshaller(DrawDataDTO.class);


    @Transactional
    public CompletionStage<List<DrawDataDTO>> pull() {

        log.info("Pulling information from:" +url);
        HttpRequest httpRequest = HttpRequest.create().withUri(
                url)
                .withHeaders(Collections.singleton(Accept.create(MediaRanges.ALL_TEXT)));
        Source<DrawDataDTO, NotUsed> source = Source.single(httpRequest).mapAsync(1, param -> http.singleRequest(param))
                .flatMapConcat(param -> this.extractEntityData(param))
                .via(support.getFramingDecoder())
                .mapAsync(1, it ->
                        (CompletionStage<DrawDataDTO>) unmarshal.unmarshal(it, materializer)
                );

        return source.runWith(Sink.seq(), materializer);
    }

    private Source<ByteString, ?> extractEntityData(HttpResponse httpResponse) {
        return (httpResponse.status() == StatusCodes.OK) ? httpResponse.entity().getDataBytes() : null;
    }

}



