package com.substring_chat.chat_app.service;

import com.substring_chat.chat_app.entities.Room;
import com.substring_chat.chat_app.repositories.RoomRepository;
import org.springframework.stereotype.Service;

@Service
public class RoomService {

    RoomRepository roomRepository;
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }
    public void deleteRoom(String roomId) {
        roomRepository.deleteByRoomId(roomId);
    }
    public Room roomExists(String roomId) {
        return roomRepository.findByRoomId(roomId);
    }
    public boolean createRoom(String roomId, String roomName) {
        if (roomExists(roomId) == null) {
            Room room = new Room();
            room.setRoomId(roomId);
            room.setRoomName(roomName);
            roomRepository.save(room);
            return true;
        }
        return false;
    }
    public Room getRoom(String roomId) {
        Room room = roomRepository.findByRoomId(roomId);
        if (room == null) {
            throw new IllegalArgumentException("Room with ID " + roomId + " does not exist.");
        }
        return room;
    }
}
