package com.weatherbackend.weatherapp.web;

import org.springframework.beans.factory.annotation.Autowired;
import com.weatherbackend.weatherapp.domain.Weather;
import com.weatherbackend.weatherapp.domain.WeatherRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WeatherRestController {

    @Autowired
    WeatherRepository weatherRepository;

    @GetMapping("/weathers")
    public ResponseEntity<List<Weather>> getWeathers(//TODO: Return only (1) latest record?
        @RequestParam(defaultValue = "0") int page, // This is set to 0 so that the first group of records is returned 
        @RequestParam(defaultValue = "20") int size  // This defines how many records are returned
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Weather> latestWeathers = weatherRepository.findAllByOrderByDateDescTimeDesc(pageRequest);

        List<Weather> weatherList = latestWeathers.getContent();

        return new ResponseEntity<>(weatherList, HttpStatus.OK);
    }

    @GetMapping("/weathers/{id}")
    public ResponseEntity<?> getWeatherById(@PathVariable("id") Long id) {
        Optional<Weather> weather = weatherRepository.findById(id);
        if (weather.isPresent()) {
            return new ResponseEntity<>(weather.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Weather record not found", HttpStatus.NOT_FOUND);
        }
    }
}