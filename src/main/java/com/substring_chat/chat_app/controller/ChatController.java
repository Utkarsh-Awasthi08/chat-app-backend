package com.substring_chat.chat_app.controller;

import com.substring_chat.chat_app.entities.Message;
import com.substring_chat.chat_app.entities.Room;
import com.substring_chat.chat_app.payloa.MessageRequest;
import com.substring_chat.chat_app.repositories.RoomRepository;
import com.substring_chat.chat_app.service.RoomService;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Controller
@CrossOrigin("*")
public class ChatController {

    private RoomService roomService;
    private RoomRepository roomRepository;
    public ChatController(RoomService roomService, RoomRepository roomRepository) {
        this.roomService = roomService;
        this.roomRepository = roomRepository;
    }

    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(@DestinationVariable String roomId, @RequestBody MessageRequest request){
        System.out.println("ROOM ID FROM CLIENT: " + roomId);
        Room room = roomService.roomExists(roomId);
        System.out.println("ROOM FOUND: " + (room != null));
        Message message=new Message();
            message.setContent(request.getContent());
            message.setSender(request.getSender());
            message.setTimeStamp(LocalDateTime.now());

            if(room != null) {
                room.getMessages().add(message);
                roomRepository.save(room);
            }
            else
                throw new RuntimeException("room not found !!");
        return message;
}
    @MessageMapping("/join/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message joinRoom(@DestinationVariable String roomId, Message message) {
        Room room = roomService.roomExists(roomId);
        if (room == null) throw new RuntimeException("room not found !!");

        message.setContent(message.getSender() + " joined the room");
        message.setType("SYSTEM");
        message.setTimeStamp(LocalDateTime.now());
        room.getMessages().add(message);
        roomRepository.save(room);

        return message;
    }

    @MessageMapping("/leave/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message leaveRoom(@DestinationVariable String roomId, Message message) {
        Room room = roomService.roomExists(roomId);
        if (room == null) throw new RuntimeException("room not found !!");

        message.setContent(message.getSender() + " left the room");
        message.setType("SYSTEM");
        message.setTimeStamp(LocalDateTime.now());

        room.getMessages().add(message);
        roomRepository.save(room);

        return message;
    }
}
