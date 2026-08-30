package com.security.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1/api/rooms")
public class RoomController {

    @PostMapping("/addRoom")
    public String addRoom(){
        return "Room added.";
    }

    @GetMapping("/{id}")
    public String getRoomById(@PathVariable Long id){
        return "Room fetched for id: "+id;
    }

    @GetMapping
    public String getRooms(){
        return "All rooms.";
    }
}
