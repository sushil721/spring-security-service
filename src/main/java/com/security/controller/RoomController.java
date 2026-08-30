package com.security.controller;

import com.security.model.Room;
import org.springframework.security.access.prepost.PostAuthorize;
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
    @PostAuthorize("returnObject.assignedTo == authentication.name")
    public Room getRoomById(@PathVariable Long id){
        return new Room(id, "jyoti");
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public String getRooms(){
        return "All rooms.";
    }
}
