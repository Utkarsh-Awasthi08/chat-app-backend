package com.substring_chat.chat_app.repositories;

import com.substring_chat.chat_app.entities.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRepository extends MongoRepository<Room, String> {

    Room findByRoomId(String roomId);

    void deleteByRoomId(String roomId);


}
