package com.arianthox.predictor.adapter;


import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.common.EntityStreamingSupport;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.*;
import akka.http.javadsl.model.headers.Accept;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
//import com.brainbreaker.core.engine.entity.JSDrawData;
//import com.brainbreaker.core.engine.repository.DrawRepository
import com.arianthox.predictor.model.DrawDataDTO;
import com.arianthox.predictor.model.DrawDataVO;
import com.arianthox.predictor.repository.DrawRepository;
import com.arianthox.predictor.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.concurrent.CompletionStage;


@Component
public class DrawConnectorAdapter {

    private HttpRequest httpRequest = HttpRequest.create().withUri(
            "http://data.ny.gov/resource/d6yy-54nr.json")
            .withHeaders(Collections.singleton(Accept.create(MediaRanges.ALL_TEXT)));

    private ActorSystem system = ActorSystem.create();
    private Materializer materializer = ActorMaterializer.create(system);
    private Http http = Http.get(system);
    private EntityStreamingSupport support = EntityStreamingSupport.json();
    private Unmarshaller unmarshal = Jackson.byteStringUnmarshaller(DrawDataDTO.class);

    @Autowired
    private DrawRepository drawRepository;


    @Transactional
    public void upload() {

        System.out.println("Uploading...");
        Source<DrawDataDTO, NotUsed> source = Source.single(httpRequest).mapAsync(1, param -> http.singleRequest(param))
                .flatMapConcat(param -> this.extractEntityData(param))
                .via(support.getFramingDecoder())
                .mapAsync(10, it ->
                        (CompletionStage<DrawDataDTO>) unmarshal.unmarshal(it, materializer)
                );

        CompletionStage completion=source.runForeach(param -> {
            DrawDataVO drawDataVO = param.toDrawData();
            System.out.println(drawDataVO);
            drawRepository.save(drawDataVO);
        },materializer);

        completion.thenAccept(o -> {
            System.out.println("Terminating ActorSystem");
            //system.terminate();
        });

    }

    private Source<ByteString, ?> extractEntityData(HttpResponse httpResponse) {
        System.out.println("Extract Data...");
        return (httpResponse.status() == StatusCodes.OK) ? httpResponse.entity().getDataBytes() : null;
    }

}



