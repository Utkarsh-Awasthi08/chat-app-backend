package com.substring_chat.chat_app.controller;

import com.substring_chat.chat_app.dto.RoomRequest;
import com.substring_chat.chat_app.entities.Message;
import com.substring_chat.chat_app.entities.Room;
import com.substring_chat.chat_app.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/v1/rooms")
public class RoomController {

    RoomService roomService;
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody RoomRequest roomRequest) {
        if(roomService.createRoom(roomRequest.getRoomId(), roomRequest.getRoomName())) {;
            return ResponseEntity.ok().body("Room with ID " + roomRequest.getRoomId() + " created successfully.");
        } else {
            return ResponseEntity.badRequest().body("Room with ID " + roomRequest.getRoomId()+ " already exists.");
        }
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<Room> getRoom(@PathVariable String roomId) {
        try {
            Room room = roomService.getRoom(roomId);
            if(room == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(room);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{roomId}/messages")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String roomId,
                                             @RequestParam(value = "page", defaultValue="0", required = false) int page,
                                             @RequestParam(value = "size", defaultValue="20", required = false) int size) {
        Room room = roomService.roomExists(roomId);
        if (room == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Message> messages = room.getMessages();
        int start = Math.max(0, messages.size() - (page + 1) * size);
        int end = Math.min (messages.size(), start + size);
        List<Message> paginatedMessages = messages.subList(start, end);
        return ResponseEntity.ok(paginatedMessages);
    }
}
