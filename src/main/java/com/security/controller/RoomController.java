package com.security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/v1/api/rooms")
public class RoomController {

    @PostMapping("/addRoom")
    @PreAuthorize("hasRole('ADMIN')")
    public String addRoom(){
        return "Room added.";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF', 'GUEST')")
    public String getRoomById(@PathVariable Long id){
        return "Room fetched for id: "+id;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public String getRooms(){
        return "All rooms.";
    }
}
