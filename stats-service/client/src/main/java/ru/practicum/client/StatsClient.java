package ru.practicum.client;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.stats.CreateEndpointHitDto;
import ru.practicum.stats.EndpointHitDto;
import ru.practicum.stats.ViewStatsDto;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class StatsClient extends BaseClient {
    private static final String ENDPOINT_HIT_API_PREFIX = "/hit";
    private static final String STATS_API_PREFIX = "/stats";

    @Autowired
    public StatsClient(@Value("${services.stats-service.uri:http://localhost:9090}") String serviceUrl,
                       RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serviceUrl))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public List<ViewStatsDto> getEndpointHitStats(LocalDateTime start, LocalDateTime end, @Nullable String app,
                                                  @Nullable List<String> uris, @Nullable Boolean unique) {
        String encodedStart = encode(parseDate(start));
        String encodedEnd = encode(parseDate(end));

        StringBuilder sb = new StringBuilder();
        Map<String, Object> parameters = new HashMap<>();

        sb.append(STATS_API_PREFIX);
        sb.append("?start={start}&end={end}");
        parameters.put("start", encodedStart);
        parameters.put("end", encodedEnd);

        if (app != null) {
            sb.append("&app={app}");
            parameters.put("app", app);
        }
        if (uris != null && uris.size() != 0) {
            sb.append("&uris={uris}");
            parameters.put("uris", toPlainString(uris));
        }
        if (unique != null) {
            sb.append("&unique={unique}");
            parameters.put("unique", unique);
        }

        Gson gson = new Gson();
        String path = sb.toString();
        ResponseEntity<Object> response = get(path, parameters);
        String json = gson.toJson(response.getBody());
        Type listOfViewStatsDtoClass = new TypeToken<ArrayList<ViewStatsDto>>(){}.getType();
        return gson.fromJson(json, listOfViewStatsDtoClass);
    }

    public ResponseEntity<Object> addEndpointHit(CreateEndpointHitDto endpointHitDto) {
        return post(ENDPOINT_HIT_API_PREFIX, endpointHitDto);
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    private String parseDate(LocalDateTime date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return date.format(formatter);
    }

    private String toPlainString(List<String> list) {
        return String.join(",", list);
    }

}
